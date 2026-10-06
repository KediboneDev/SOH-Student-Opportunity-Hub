import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class MyOpportunitiesScreen
{
    private VBox view;
    private TableView<Opportunity> tableView;
    private String companyUsername;
    private OpportunityService service;
    private Label lblStats;
    
    public MyOpportunitiesScreen(String companyUsername, OpportunityService service)
    {
        this.companyUsername = companyUsername;
        this.service = service;
        
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("My Opportunities");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        lblStats = new Label();
        lblStats.setStyle("-fx-background-color: LIGHTGRAY; -fx-padding: 10;");
        
        tableView = new TableView<>();
        tableView.setPrefHeight(400);
        
        TableColumn<Opportunity, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        titleCol.setPrefWidth(180);
        
        TableColumn<Opportunity, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));
        typeCol.setPrefWidth(80);
        
        TableColumn<Opportunity, Double> avgCol = new TableColumn<>("Min GPA");
        avgCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getMinAverage()).asObject());
        avgCol.setPrefWidth(80);
        
        TableColumn<Opportunity, String> closingCol = new TableColumn<>("Closing Date");
        closingCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClosingDate()));
        closingCol.setPrefWidth(100);
        
        TableColumn<Opportunity, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            LocalDate closing = LocalDate.parse(cellData.getValue().getClosingDate());
            String status = closing.isBefore(LocalDate.now()) ? "CLOSED" : "OPEN";
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        statusCol.setPrefWidth(80);
        
        TableColumn<Opportunity, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setCellFactory(col -> new TableCell<Opportunity, Void>() {
            private final Button btnDelete = new Button("Delete");
            {
                btnDelete.setStyle("-fx-background-color: CRIMSON; -fx-text-fill: WHITE; -fx-font-size: 12px;");
                btnDelete.setOnAction(e -> {
                    Opportunity opp = getTableView().getItems().get(getIndex());
                    deleteOpportunity(opp);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnDelete);
            }
        });
        actionCol.setPrefWidth(80);
        
        tableView.getColumns().addAll(titleCol, typeCol, avgCol, closingCol, statusCol, actionCol);
        
        Button btnRefresh = new Button("Refresh");
        btnRefresh.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        btnRefresh.setOnAction(e -> loadOpportunities());
        
        view.getChildren().addAll(title, lblStats, tableView, btnRefresh);
        loadOpportunities();
    }
    
    private void loadOpportunities()
    {
        ArrayList<Opportunity> opps = service.getOpportunitiesByCompany(companyUsername);
        tableView.getItems().clear();
        tableView.getItems().addAll(opps);
        
        int total = opps.size();
        int open = 0, closed = 0;
        for (Opportunity opp : opps) {
            LocalDate closing = LocalDate.parse(opp.getClosingDate());
            if (closing.isBefore(LocalDate.now())) {
                closed++;
            } else {
                open++;
            }
        }
        
        lblStats.setText(String.format("Total: %d | Open: %d | Closed: %d", total, open, closed));
    }
    
    private void deleteOpportunity(Opportunity opp)
    {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Opportunity");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("Delete '" + opp.getTitle() + "'? This will also remove all applications.");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            service.deleteOpportunity(opp);
            FileManager.saveOpportunities(service.getAllOpportunities());
            
            ArrayList<Applications> allApps = FileManager.loadApplications();
            ArrayList<Applications> remainingApps = new ArrayList<>();
            for (Applications app : allApps) {
                if (!app.getPost().equals(opp.getTitle())) {
                    remainingApps.add(app);
                }
            }
            FileManager.saveApplications(remainingApps);
            
            SecurityLogger.log(SecurityLogger.INFO, "OPPORTUNITY_DELETED", companyUsername, 
                "Deleted: " + opp.getTitle());
            
            loadOpportunities();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Opportunity deleted.");
            alert.showAndWait();
        }
    }
    
    public VBox getView() { return view; }
}