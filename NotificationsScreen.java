import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class NotificationsScreen
{
    private VBox view;
    private ListView<Notification> listView;
    
    public NotificationsScreen(String username, NotificationService service)
    {
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Notifications");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        listView = new ListView<>();
        listView.setPrefHeight(400);
        
        ArrayList<Notification> notifs = service.getNotificationsForUser(username);
        for (Notification n : notifs) {
            listView.getItems().add(n);
        }
        
        listView.setCellFactory(lv -> new ListCell<Notification>() {
            @Override
            protected void updateItem(Notification item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    if (item.isRead()) {
                        setStyle("-fx-background-color: WHITE; -fx-padding: 8;");
                        setText("[" + item.getTimestamp() + "] " + item.getMessage());
                    } else {
                        setStyle("-fx-background-color: LIGHTYELLOW; -fx-padding: 8; -fx-font-weight: bold;");
                        setText("NEW - [" + item.getTimestamp() + "] " + item.getMessage());
                    }
                }
            }
        });
        
        Button btnMarkRead = new Button("Mark as Read");
        btnMarkRead.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        btnMarkRead.setOnAction(e -> {
            Notification selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setRead(true);
                listView.refresh();
            }
        });
        
        view.getChildren().addAll(title, listView, btnMarkRead);
    }
    
    public VBox getView() { return view; }
}