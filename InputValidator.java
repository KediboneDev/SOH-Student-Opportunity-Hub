
/**
 * Write a description of class InputValidator here.
 *
 * @author Sinqobile Mabaso 
 * @version 3 May 2026
 */
public class InputValidator
{
    // Field length constraints
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_EMAIL_LENGTH = 100;
    private static final int MAX_USERNAME_LENGTH = 30;
    private static final int MIN_USERNAME_LENGTH = 3;

    // Percentage range
    private static final int MIN_PERCENTAGE = 0;
    private static final int MAX_PERCENTAGE = 100;

    // Constructor (prevents object creation)
    private InputValidator()
    {
        throw new UnsupportedOperationException("This is a utility class.");
    }

    // NULL OR EMPTY CHECK
    public static boolean isNullOrEmpty(String value)
    {
        if (value == null)
        {
            return true;
        }

        String trimmed = value.trim();

        if (trimmed.length() == 0)
        {
            return true;
        }

        return false;
    }

    // EMAIL VALIDATION
    public static boolean isValidEmail(String email)
    {

        if (isNullOrEmpty(email)) 
        {
            return false;
        }

        email = email.trim();

        if (email.length() > MAX_EMAIL_LENGTH)
        {
            return false;
        }

        if (email.contains(" "))
        {
            return false;
        }

        int atIndex = email.indexOf('@');

        if (atIndex <= 0)
        {
            return false;
        }

        if (atIndex != email.lastIndexOf('@'))
        {
            return false;
        }

        String domain = email.substring(atIndex + 1);
        int dotIndex = domain.lastIndexOf('.');

        if (dotIndex <= 0)
        {
            return false;
        }

        if (dotIndex >= domain.length() - 1)
        {
            return false;
        }

        return true;
    }

    // USERNAME VALIDATION
    public static boolean isValidUsername(String username)
    {

        if (isNullOrEmpty(username))
        {
            return false;
        }

        username = username.trim();
        int length = username.length();

        if (length < MIN_USERNAME_LENGTH) 
        {
            return false;
        }

        if (length > MAX_USERNAME_LENGTH)
        {
            return false;
        }

        for (int i = 0; i < username.length(); i++)
        {

            char c = username.charAt(i);

            if (!Character.isLetterOrDigit(c) && c != '_' && c != '-')
            {
                return false;
            }
        }

        return true;
    }

    // NAME VALIDATION
    public static boolean isValidName(String name)
    {
        if (isNullOrEmpty(name)) 
        {
            return false;
        }

        name = name.trim();

        if (name.length() > MAX_NAME_LENGTH)
        {
            return false;
        }

        for (int i = 0; i < name.length(); i++) 
        {
            char c = name.charAt(i);
            
            if (!Character.isLetter(c) && c != ' ' && c != '-' && c != '\'')
            {
                return false;
            }
        }
        return true;
    }

    // PERCENTAGE VALIDATION
    public static boolean isValidPercentage(int percentage)
    {

        if(percentage < MIN_PERCENTAGE)
        {
            return false;
        }

        if(percentage > MAX_PERCENTAGE)
        {
            return false;
        }

        return true;
    }

    public static int parsePercentage(String text)
    {

        if(isNullOrEmpty(text))
        {
            return -1;
        }

        text = text.trim();

        try
        {
            int value = Integer.parseInt(text);

            if (isValidPercentage(value))
            {
                return value;
            } 
            else
            {
                return -1;
            }

        } 
        catch (NumberFormatException e)
        {
            return -1;
        }
    }
    
    // ROLE VALIDATION
    public static boolean isValidRole(String role)
    {

        if(isNullOrEmpty(role))
        {
            return false;
        }

        String r = role.trim().toUpperCase();

        if(r.equals("STUDENT"))
        {
            return true;
        }

        if(r.equals("COMPANY"))
        {
            return true;
        }

        return false;
    }
}