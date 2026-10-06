import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class ManageApplicantsScreen
{
    private VBox view;
    private ComboBox<String> cmbOpportunity;
    private TableView<Applications> tableView;
    private String companyUsername;
    private OpportunityService oppService;
    private ApplicationService appService;
    private Label lblStats;
    
    public ManageApplicantsScreen(String companyUsername, OpportunityService oppService, ApplicationService appService)
    {
        this.companyUsername = companyUsername;
        this.oppService = oppService;
        this.appService = appService;
        
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Manage Applicants");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        lblStats = new Label();
        lblStats.setStyle("-fx-background-color: LIGHTGRAY; -fx-padding: 10;");
        
        HBox selectorBox = new HBox(10);
        Label lblSelect = new Label("Select Opportunity:");
        cmbOpportunity = new ComboBox<>();
        cmbOpportunity.setPrefWidth(300);
        cmbOpportunity.setOnAction(e -> loadApplicants());
        
        selectorBox.getChildren().addAll(lblSelect, cmbOpportunity);
        
        tableView = new TableView<>();
        tableView.setPrefHeight(400);
        
        TableColumn<Applications, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStudentName()));
        studentCol.setPrefWidth(150);
        
        TableColumn<Applications, Double> gpaCol = new TableColumn<>("GPA (%)");
        gpaCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getGpa()).asObject());
        gpaCol.setPrefWidth(80);
        gpaCol.setSortable(false);
        
        TableColumn<Applications, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString()));
        statusCol.setPrefWidth(100);
        
        TableColumn<Applications, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<Applications, Void>() {
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Applications app = getTableView().getItems().get(getIndex());
                    HBox buttons = new HBox(5);
                    
                    if (app.getStatus() == ApplicationStatus.PENDING) {
                        Button btnReview = new Button("Start Review");
                        btnReview.setStyle("-fx-background-color: ORANGE; -fx-text-fill: WHITE; -fx-font-size: 11px;");
                        btnReview.setOnAction(e -> updateStatus(app, ApplicationStatus.IN_REVIEW));
                        buttons.getChildren().add(btnReview);
                        setGraphic(buttons);
                        
                    } else if (app.getStatus() == ApplicationStatus.IN_REVIEW) {
                        Button btnAccept = new Button("Accept");
                        btnAccept.setStyle("-fx-background-color: MEDIUMSEAGREEN; -fx-text-fill: WHITE; -fx-font-size: 11px;");
                        btnAccept.setOnAction(e -> updateStatus(app, ApplicationStatus.SELECTED));
                        
                        Button btnReject = new Button("Reject");
                        btnReject.setStyle("-fx-background-color: CRIMSON; -fx-text-fill: WHITE; -fx-font-size: 11px;");
                        btnReject.setOnAction(e -> updateStatus(app, ApplicationStatus.REJECTED));
                        
                        buttons.getChildren().addAll(btnAccept, btnReject);
                        setGraphic(buttons);
                        
                    } else if (app.getStatus() == ApplicationStatus.SELECTED) {
                        Label lbl = new Label("ACCEPTED");
                        lbl.setStyle("-fx-text-fill: GREEN; -fx-font-weight: bold; -fx-font-size: 12px;");
                        setGraphic(lbl);
                        
                    } else if (app.getStatus() == ApplicationStatus.REJECTED) {
                        Label lbl = new Label("REJECTED");
                        lbl.setStyle("-fx-text-fill: RED; -fx-font-weight: bold; -fx-font-size: 12px;");
                        setGraphic(lbl);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        actionCol.setPrefWidth(150);
        
        tableView.getColumns().addAll(studentCol, gpaCol, statusCol, actionCol);
        
        view.getChildren().addAll(title, lblStats, selectorBox, tableView);
        loadOpportunities();
    }
    
    private void loadOpportunities()
    {
        ArrayList<Opportunity> companyOpps = oppService.getOpportunitiesByCompany(companyUsername);
        cmbOpportunity.getItems().clear();
        for (Opportunity opp : companyOpps) {
            cmbOpportunity.getItems().add(opp.getTitle());
        }
        if (!cmbOpportunity.getItems().isEmpty()) {
            cmbOpportunity.setValue(cmbOpportunity.getItems().get(0));
        }
    }
    
    private void loadApplicants()
    {
        String selectedTitle = cmbOpportunity.getValue();
        if (selectedTitle == null) return;
        
        ArrayList<Applications> apps = appService.getApplicationsForOpportunity(selectedTitle);
        
        apps.sort((a1, a2) -> Double.compare(a2.getGpa(), a1.getGpa()));
        
        tableView.getItems().clear();
        tableView.getItems().addAll(apps);
        
        int total = apps.size();
        int selected = 0, rejected = 0, pending = 0, inReview = 0;
        for (Applications app : apps) {
            switch (app.getStatus()) {
                case SELECTED: selected++; break;
                case REJECTED: rejected++; break;
                case PENDING: pending++; break;
                case IN_REVIEW: inReview++; break;
                default: break;
            }
        }
        
        lblStats.setText(String.format("Total: %d | Accepted: %d | Rejected: %d | Pending: %d | In Review: %d",
            total, selected, rejected, pending, inReview));
    }
    
    private void updateStatus(Applications app, ApplicationStatus newStatus)
    {
        String message;
        String actionText;
        
        if (newStatus == ApplicationStatus.SELECTED) {
            message = "has been ACCEPTED. Congratulations!";
            actionText = "accepted";
        } else if (newStatus == ApplicationStatus.REJECTED) {
            message = "has been REJECTED. Thank you for your interest.";
            actionText = "rejected";
        } else if (newStatus == ApplicationStatus.IN_REVIEW) {
            message = "is now UNDER REVIEW.";
            actionText = "moved to In Review";
        } else {
            message = "status has been updated to " + newStatus;
            actionText = "updated";
        }
        
        app.setStatus(newStatus);
        appService.updateApplication(app);
        FileManager.saveApplications(appService.getAllApplications());
        
        Notification notif = new Notification(
            app.getStudentName(),
            "Application Status Update - " + app.getPost(),
            "Your application for '" + app.getPost() + "' " + message
        );
        
        ArrayList<Notification> notifs = FileManager.loadNotifications();
        notifs.add(notif);
        FileManager.saveNotifications(notifs);
        
        SecurityLogger.log(SecurityLogger.INFO, "APPLICATION_" + newStatus.toString(), companyUsername,
            actionText + " " + app.getStudentName() + "'s application for " + app.getPost());
        
        loadApplicants();
        
        String alertMessage;
        if (newStatus == ApplicationStatus.SELECTED) {
            alertMessage = "Candidate accepted. Notification sent.";
        } else if (newStatus == ApplicationStatus.REJECTED) {
            alertMessage = "Candidate rejected. Notification sent.";
        } else {
            alertMessage = "Application moved to In Review.";
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Manage Applicants");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
    
    public VBox getView() { return view; }
}