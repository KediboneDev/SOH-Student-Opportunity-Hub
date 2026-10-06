import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class MyApplicationsScreen
{
    private VBox view;
    private ListView<Applications> listView;
    private String studentUsername;
    private ApplicationService service;
    private Label lblStats;
    
    public MyApplicationsScreen(String studentUsername, ApplicationService service)
    {
        this.studentUsername = studentUsername;
        this.service = service;
        
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("My Applications");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        lblStats = new Label();
        lblStats.setStyle("-fx-background-color: LIGHTGRAY; -fx-padding: 10;");
        
        listView = new ListView<>();
        listView.setPrefHeight(400);
        
        Button btnRefresh = new Button("Refresh");
        btnRefresh.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        btnRefresh.setOnAction(e -> loadApplications());
        
        view.getChildren().addAll(title, lblStats, listView, btnRefresh);
        loadApplications();
    }
    
    private void loadApplications()
    {
        ArrayList<Applications> apps = service.getApplicationsForStudent(studentUsername);
        
        apps.sort((a1, a2) -> a2.getDateApplied().compareTo(a1.getDateApplied()));
        
        listView.getItems().clear();
        for (Applications app : apps) {
            listView.getItems().add(app);
        }
        
        listView.setCellFactory(lv -> new ListCell<Applications>() {
            @Override
            protected void updateItem(Applications item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    String color;
                    String statusPrefix = "";
                    
                    switch (item.getStatus()) {
                        case SELECTED:
                            color = "LIGHTGREEN";
                            statusPrefix = "ACCEPTED - ";
                            break;
                        case REJECTED:
                            color = "MISTYROSE";
                            statusPrefix = "REJECTED - ";
                            break;
                        case IN_REVIEW:
                            color = "PEACHPUFF";
                            statusPrefix = "IN REVIEW - ";
                            break;
                        default:
                            color = "LIGHTYELLOW";
                            statusPrefix = "PENDING - ";
                    }
                    
                    setStyle("-fx-background-color: " + color + "; -fx-padding: 8;");
                    setText(String.format("%s%s\n   Applied: %s | Your GPA: %.1f%%",
                        statusPrefix, item.getPost(), item.getDateApplied(), item.getGpa()));
                }
            }
        });
        
        int pending = 0, inReview = 0, selected = 0, rejected = 0;
        for (Applications app : apps) {
            switch (app.getStatus()) {
                case PENDING: pending++; break;
                case IN_REVIEW: inReview++; break;
                case SELECTED: selected++; break;
                case REJECTED: rejected++; break;
            }
        }
        
        lblStats.setText(String.format("Total: %d | Pending: %d | In Review: %d | Accepted: %d | Rejected: %d",
            apps.size(), pending, inReview, selected, rejected));
    }
    
    public VBox getView() { return view; }
}