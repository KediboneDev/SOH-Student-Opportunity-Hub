import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notification implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String recipientUsername;
    private String title;
    private String message;
    private String timestamp;
    private boolean isRead;
    
    public Notification(String recipientUsername, String title, String message)
    {
        this.recipientUsername = recipientUsername;
        this.title = title;
        this.message = message;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.isRead = false;
    }
    
    public String getRecipientUsername() { return recipientUsername; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    public boolean isRead() { return isRead; }
    
    public void setRead(boolean read) { isRead = read; }
    
    @Override
    public String toString()
    {
        return (isRead ? "" : "NEW - ") + "[" + timestamp + "] " + title + ": " + message;
    }
}