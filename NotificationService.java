import java.util.ArrayList;

public class NotificationService
{
    private ArrayList<Notification> notifications;
    
    public NotificationService(ArrayList<Notification> notifications)
    {
        this.notifications = (notifications == null) ? new ArrayList<>() : notifications;
    }
    
    public void addNotification(Notification notif)
    {
        notifications.add(notif);
    }
    
    public ArrayList<Notification> getNotificationsForUser(String username)
    {
        ArrayList<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientUsername().equals(username)) {
                result.add(n);
            }
        }
        return result;
    }
    
    public ArrayList<Notification> getUnreadNotificationsForUser(String username)
    {
        ArrayList<Notification> result = new ArrayList<>();
        for (Notification n : notifications) {
            if (n.getRecipientUsername().equals(username) && !n.isRead()) {
                result.add(n);
            }
        }
        return result;
    }
    
    public void markAsRead(Notification notif)
    {
        notif.setRead(true);
    }
    
    public ArrayList<Notification> getAllNotifications()
    {
        return notifications;
    }
}