
/**
 * Write a description of class SecurityLogger here.
 *
 * @author Sinqobile Mabaso 
 * @version 30 April 2026
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SecurityLogger
{
    // Log file location
    private static final String LOG_FILE = "security_log.txt";
 
    // Date/time format for log entries
    private static final DateTimeFormatter FORMATTER =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 
    // Log levels
    public static final String INFO = "INFO ";
    public static final String WARN = "WARN ";
    public static final String ERROR = "ERROR";
 
    // Event type constants — use these in AuthService calls
    public static final String EVENT_LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String EVENT_LOGIN_FAIL = "LOGIN_FAIL";
    public static final String EVENT_LOGOUT = "LOGOUT";
    public static final String EVENT_LOCKOUT = "LOCKOUT";
    public static final String EVENT_REGISTER = "REGISTER";
    public static final String EVENT_PASSWORD_CHANGE = "PASSWORD_CHANGE";
    public static final String EVENT_SUSPICIOUS = "SUSPICIOUS";
    public static final String EVENT_ACCESS_DENIED = "ACCESS_DENIED";
 
    /**
     * Private constructor — static utility class.
     */
    private SecurityLogger() {
        throw new UnsupportedOperationException("SecurityLogger is a utility class.");
    }

    // Writes a security event to the log file. Appends to the file
    public static void log(String level, String event, String username, String detail) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String entry = String.format("[%s] [%s] [%s] %s — %s", timestamp, level, event, username, detail);
 
        // append=true keeps all previous log entries
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true)))
        {
            writer.println(entry);
        } 
        catch (IOException e)
        {
            // If logging fails, print to console so it's not silently lost
            System.err.println("[SecurityLogger] Failed to write log: " + e.getMessage());
            System.err.println("Missed entry: " + entry);
        }
    }

    // Logs a successful login.
    public static void logLoginSuccess(String username)
    {
        log(INFO, EVENT_LOGIN_SUCCESS, username, "Login successful.");
    }

    // Logs a failed login attempt.
    public static void logLoginFailure(String username, int attempt, int maxAttempts)
    {
        log(WARN, EVENT_LOGIN_FAIL, username, "Invalid password (attempt " + attempt + "/" + maxAttempts + ")");
    }

    // Logs an account lockout event.
    public static void logLockout(String username)
    {
        log(ERROR, EVENT_LOCKOUT, username, "Account locked after too many failed attempts.");
    }

    // Logs a user logout.
    public static void logLogout(String username)
    {
        log(INFO, EVENT_LOGOUT, username, "User logged out.");
    }

    // Logs a new user registration.
    public static void logRegistration(String username, String role) 
    {
        log(INFO, EVENT_REGISTER, username, "New account registered. Role: " + role);
    }

    // Logs an access denied event (e.g. wrong role trying to access a screen).
    public static void logAccessDenied(String username, String attemptedAction)
    {
        log(WARN, EVENT_ACCESS_DENIED, username,
            "Access denied for action: " + attemptedAction);
    }

    // Logs a suspicious activity event (e.g. malformed input, injection attempt).
    public static void logSuspicious(String username, String detail)
    {
        log(ERROR, EVENT_SUSPICIOUS, username, "Suspicious activity: " + detail);
    }
}