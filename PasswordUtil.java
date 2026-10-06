import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil
{
    private static final int BCRYPT_ROUNDS = 12;
    private static final int MIN_PASSWORD_LENGTH = 8;
    
    private PasswordUtil()
    {
        throw new UnsupportedOperationException("PasswordUtil is a utility class.");
    }
    
    // ==================== PASSWORD METHODS ====================
    
    public static String hashPassword(String plainTextPassword)
    {
        validatePassword(plainTextPassword);
        String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
        return BCrypt.hashpw(plainTextPassword, salt);
    }
    
    public static boolean verifyPassword(String plainTextPassword, String storedHash)
    {
        if (plainTextPassword == null || plainTextPassword.isEmpty())
        {
            return false;
        }
        if (storedHash == null || storedHash.isEmpty())
        {
            return false;
        }
        try
        {
            return BCrypt.checkpw(plainTextPassword, storedHash);
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("[PasswordUtil] Malformed hash: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean isValidPassword(String password)
    {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH)
        {
            return false;
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (int i = 0; i < password.length(); i++)
        {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
    
    public static String getPasswordValidationMessage(String password)
    {
        if (password == null || password.isEmpty())
        {
            return "Password cannot be empty.";
        }
        if (password.length() < MIN_PASSWORD_LENGTH)
        {
            return "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.";
        }
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (int i = 0; i < password.length(); i++)
        {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        if (!hasUpper) return "Password must contain at least one uppercase letter.";
        if (!hasLower) return "Password must contain at least one lowercase letter.";
        if (!hasDigit) return "Password must contain at least one digit (0-9).";
        if (!hasSpecial) return "Password must contain at least one special character (!@#$%^&*).";
        
        return "";
    }
    
    private static void validatePassword(String password)
    {
        if (password == null)
        {
            throw new IllegalArgumentException("Password cannot be null.");
        }
        if (password.isEmpty())
        {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (password.length() < MIN_PASSWORD_LENGTH)
        {
            throw new IllegalArgumentException("Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
    }
    
    // ==================== SECURITY ANSWER METHODS ====================
    
    public static String hashSecurityAnswer(String answer)
    {
        if (answer == null || answer.trim().isEmpty())
        {
            throw new IllegalArgumentException("Security answer cannot be empty.");
        }
        String normalized = answer.toLowerCase().trim();
        String salt = BCrypt.gensalt(BCRYPT_ROUNDS);
        return BCrypt.hashpw(normalized, salt);
    }
    
    public static boolean verifySecurityAnswer(String plainAnswer, String storedHash)
    {
        if (plainAnswer == null || plainAnswer.trim().isEmpty())
        {
            return false;
        }
        if (storedHash == null || storedHash.isEmpty())
        {
            return false;
        }
        try
        {
            String normalized = plainAnswer.toLowerCase().trim();
            return BCrypt.checkpw(normalized, storedHash);
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }
}