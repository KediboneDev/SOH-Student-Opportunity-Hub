import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AuthService
{
    public static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final long RATE_LIMIT_MS = 5000;
    
    private static ArrayList<User> allUsers = new ArrayList<>();
    private static Map<String, User> userMap = new HashMap<>();
    
    private static String currentUser = null;
    private static String currentRole = null;
    
    private static Map<String, Integer> failedAttempts = new HashMap<>();
    private static Map<String, Boolean> lockedAccounts = new HashMap<>();
    private static Map<String, Long> lastFailTime = new HashMap<>();
    
    static {
        loadUsersFromFile();
    }
    
    private static void loadUsersFromFile()
    {
        try {
            ArrayList<Student> students = FileManager.loadStudents();
            for (Student s : students) {
                allUsers.add(s);
                userMap.put(s.getUsername().toLowerCase(), s);
            }
            
            ArrayList<Company> companies = FileManager.loadCompanies();
            for (Company c : companies) {
                allUsers.add(c);
                userMap.put(c.getUsername().toLowerCase(), c);
            }
            
            System.out.println("[AuthService] Loaded " + allUsers.size() + " users.");
        } catch (Exception e) {
            System.err.println("[AuthService] Load error: " + e.getMessage());
        }
    }
    
    private static void saveUsersToFile()
    {
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Company> companies = new ArrayList<>();
        
        for (User u : allUsers) {
            if (u instanceof Student) {
                students.add((Student) u);
            } else if (u instanceof Company) {
                companies.add((Company) u);
            }
        }
        
        FileManager.saveStudents(students);
        FileManager.saveCompanies(companies);
    }
    
    public static boolean register(String username, String password, String role)
    {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        if (!InputValidator.isValidUsername(username)) {
            return false;
        }
        
        if (!PasswordUtil.isValidPassword(password)) {
            return false;
        }
        
        String key = username.toLowerCase();
        if (userMap.containsKey(key)) {
            return false;
        }
        
        String hash = PasswordUtil.hashPassword(password);
        
        User newUser;
        if (role.equalsIgnoreCase("STUDENT")) {
            newUser = new Student(username, hash, 0);
        } else if (role.equalsIgnoreCase("COMPANY")) {
            newUser = new Company(username, hash, username);
        } else {
            return false;
        }
        
        allUsers.add(newUser);
        userMap.put(key, newUser);
        saveUsersToFile();
        
        SecurityLogger.logRegistration(username, role);
        return true;
    }
    
    public static boolean login(String username, String password)
    {
        if (username == null || password == null) {
            return false;
        }
        
        String key = username.toLowerCase();
        
        if (lockedAccounts.getOrDefault(key, false)) {
            SecurityLogger.log(SecurityLogger.ERROR, "LOGIN_BLOCKED", username, "Account is locked.");
            return false;
        }
        
        Long lastFail = lastFailTime.get(key);
        if (lastFail != null && (System.currentTimeMillis() - lastFail) < RATE_LIMIT_MS) {
            SecurityLogger.log(SecurityLogger.WARN, "LOGIN_RATE_LIMIT", username, "Rate limit exceeded.");
            return false;
        }
        
        User user = userMap.get(key);
        if (user == null) {
            recordFailedAttempt(key);
            return false;
        }
        
        System.out.println("[AuthService] Verifying password for: " + username);
        boolean valid = PasswordUtil.verifyPassword(password, user.getPasswordHash());
        System.out.println("[AuthService] Password valid: " + valid);
        
        if (!valid) {
            recordFailedAttempt(key);
            int attempts = failedAttempts.getOrDefault(key, 0);
            if (attempts >= MAX_LOGIN_ATTEMPTS) {
                lockedAccounts.put(key, true);
                SecurityLogger.logLockout(username);
            }
            SecurityLogger.logLoginFailure(username, attempts + 1, MAX_LOGIN_ATTEMPTS);
            return false;
        }
        
        resetFailedAttempts(key);
        currentUser = user.getUsername();
        currentRole = user.getRole();
        SecurityLogger.logLoginSuccess(username);
        return true;
    }
    
    public static void logout()
    {
        if (currentUser != null) {
            SecurityLogger.logLogout(currentUser);
        }
        currentUser = null;
        currentRole = null;
    }
    
    public static boolean isLoggedIn() { return currentUser != null; }
    public static boolean hasRole(String role) { return role != null && role.equalsIgnoreCase(currentRole); }
    public static String getCurrentUser() { return currentUser; }
    public static String getCurrentRole() { return currentRole; }
    public static User getUser(String username) { return userMap.get(username.toLowerCase()); }
    
    public static void updateUser(User updatedUser)
    {
        String key = updatedUser.getUsername().toLowerCase();
        
        userMap.put(key, updatedUser);
        
        for (int i = 0; i < allUsers.size(); i++) {
            if (allUsers.get(i).getUsername().equalsIgnoreCase(key)) {
                allUsers.set(i, updatedUser);
                break;
            }
        }
        
        saveUsersToFile();
        System.out.println("[AuthService] User updated: " + updatedUser.getUsername());
    }
    
    public static boolean unlockAccount(String targetUsername)
    {
        if (!isLoggedIn() || !hasRole("COMPANY")) {
            SecurityLogger.logAccessDenied(getCurrentUser(), "unlockAccount");
            return false;
        }
        
        String key = targetUsername.toLowerCase();
        if (!userMap.containsKey(key)) {
            return false;
        }
        
        lockedAccounts.put(key, false);
        failedAttempts.put(key, 0);
        lastFailTime.remove(key);
        
        SecurityLogger.log(SecurityLogger.INFO, "ACCOUNT_UNLOCKED", getCurrentUser(), 
            "Unlocked account: " + targetUsername);
        
        return true;
    }
    
    private static void recordFailedAttempt(String key)
    {
        int count = failedAttempts.getOrDefault(key, 0);
        failedAttempts.put(key, count + 1);
        lastFailTime.put(key, System.currentTimeMillis());
    }
    
    private static void resetFailedAttempts(String key)
    {
        failedAttempts.remove(key);
        lastFailTime.remove(key);
        lockedAccounts.remove(key);
    }
}