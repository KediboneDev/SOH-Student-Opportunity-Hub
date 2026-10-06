import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class OpportunitiesScreen
{
    private VBox view;
    private ListView<Opportunity> listView;
    private TextField searchField;
    private Button btnShowAll, btnShowBursary, btnShowInternship;
    private String currentFilter;
    private OpportunityService service;
    private String studentUsername;
    private ApplicationService appService;
    private Label lblStats;
    
    public OpportunitiesScreen(OpportunityService service, String studentUsername, ApplicationService appService)
    {
        this.service = service;
        this.studentUsername = studentUsername;
        this.appService = appService;
        this.currentFilter = "ALL";
        
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Available Opportunities");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        lblStats = new Label();
        lblStats.setStyle("-fx-background-color: LIGHTGRAY; -fx-padding: 10;");
        
        // Type filter buttons
        HBox typeBox = new HBox(10);
        typeBox.getChildren().addAll(
            new Label("Filter: "),
            btnShowAll = new Button("All"),
            btnShowBursary = new Button("Bursaries"),
            btnShowInternship = new Button("Internships")
        );
        
        btnShowAll.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        btnShowBursary.setStyle("-fx-background-color: LIGHTGRAY;");
        btnShowInternship.setStyle("-fx-background-color: LIGHTGRAY;");
        
        btnShowAll.setOnAction(e -> {
            currentFilter = "ALL";
            updateButtons();
            loadOpportunities();
        });
        btnShowBursary.setOnAction(e -> {
            currentFilter = "BURSARY";
            updateButtons();
            loadOpportunities();
        });
        btnShowInternship.setOnAction(e -> {
            currentFilter = "INTERNSHIP";
            updateButtons();
            loadOpportunities();
        });
        
        // Search
        HBox searchBar = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(250);
        Button btnSearch = new Button("Search");
        Button btnReset = new Button("Reset");
        
        btnSearch.setOnAction(e -> loadOpportunities());
        btnReset.setOnAction(e -> {
            searchField.clear();
            currentFilter = "ALL";
            updateButtons();
            loadOpportunities();
        });
        
        searchBar.getChildren().addAll(searchField, btnSearch, btnReset);
        
        listView = new ListView<>();
        listView.setPrefHeight(400);
        
        Button btnApply = new Button("Apply");
        btnApply.setStyle("-fx-background-color: LIMEGREEN; -fx-text-fill: WHITE;");
        btnApply.setOnAction(e -> applyToSelected());
        
        view.getChildren().addAll(title, lblStats, typeBox, searchBar, listView, btnApply);
        loadOpportunities();
    }
    
    private void updateButtons()
    {
        btnShowAll.setStyle("-fx-background-color: LIGHTGRAY;");
        btnShowBursary.setStyle("-fx-background-color: LIGHTGRAY;");
        btnShowInternship.setStyle("-fx-background-color: LIGHTGRAY;");
        
        if (currentFilter.equals("ALL")) {
            btnShowAll.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        } else if (currentFilter.equals("BURSARY")) {
            btnShowBursary.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        } else if (currentFilter.equals("INTERNSHIP")) {
            btnShowInternship.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        }
    }
    
    private void loadOpportunities()
    {
        ArrayList<Opportunity> results = service.search(searchField.getText());
        
        // Filter by type
        if (currentFilter.equals("BURSARY")) {
            ArrayList<Opportunity> filtered = new ArrayList<>();
            for (Opportunity opp : results) {
                if (opp.getType().equals("Bursary")) filtered.add(opp);
            }
            results = filtered;
        } else if (currentFilter.equals("INTERNSHIP")) {
            ArrayList<Opportunity> filtered = new ArrayList<>();
            for (Opportunity opp : results) {
                if (opp.getType().equals("Internship")) filtered.add(opp);
            }
            results = filtered;
        }
        
        // Only show open opportunities
        ArrayList<Opportunity> openResults = new ArrayList<>();
        for (Opportunity opp : results) {
            LocalDate closing = LocalDate.parse(opp.getClosingDate());
            if (!closing.isBefore(LocalDate.now())) {
                openResults.add(opp);
            }
        }
        results = openResults;
        
        // Sort by closing date
        Collections.sort(results);
        
        Student student = (Student) AuthService.getUser(studentUsername);
        double studentAvg = (student != null) ? student.getAverage() : 0;
        
        listView.getItems().clear();
        for (Opportunity opp : results) {
            listView.getItems().add(opp);
        }
        
        // Display each opportunity
        listView.setCellFactory(lv -> new ListCell<Opportunity>() {
            @Override
            protected void updateItem(Opportunity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    LocalDate closing = LocalDate.parse(item.getClosingDate());
                    LocalDate today = LocalDate.now();
                    boolean eligible = item.isEligible(studentAvg);
                    
                    String color;
                    if (closing.equals(today)) {
                        color = "PEACHPUFF";
                    } else if (closing.isBefore(today.plusDays(7))) {
                        color = "LIGHTYELLOW";
                    } else if (eligible) {
                        color = "LIGHTGREEN";
                    } else {
                        color = "MISTYROSE";
                    }
                    
                    String info = "";
                    if (item.getType().equals("Bursary")) {
                        Bursary b = (Bursary) item;
                        info = " | " + b.getFundingType();
                    } else {
                        Internship i = (Internship) item;
                        info = " | " + i.getDurationMonths() + " months";
                    }
                    
                    setText(item.getTitle() + " (" + item.getType() + ")\n" +
                           "   Company: " + item.getCompany() + 
                           " | Min GPA: " + (int)item.getMinAverage() + "%" +
                           " | Location: " + item.getLocation() +
                           " | Closing: " + item.getClosingDate() + info);
                    
                    setStyle("-fx-background-color: " + color + "; -fx-padding: 8;");
                }
            }
        });
        
        int eligibleCount = 0;
        for (Opportunity opp : results) {
            if (opp.isEligible(studentAvg)) eligibleCount++;
        }
        
        lblStats.setText("Showing " + results.size() + " opportunities | You qualify for " + eligibleCount + 
                        " | Your GPA: " + studentAvg + "%");
    }
    
    private void applyToSelected()
    {
        Opportunity selected = listView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select an opportunity first.");
            return;
        }
        
        Student student = (Student) AuthService.getUser(studentUsername);
        
        LocalDate closing = LocalDate.parse(selected.getClosingDate());
        if (closing.isBefore(LocalDate.now())) {
            showAlert("This opportunity has closed.");
            return;
        }
        
        if (!selected.isEligible(student.getAverage())) {
            showAlert("You need " + (int)selected.getMinAverage() + "% but you have " + student.getAverage() + "%");
            return;
        }
        
        if (appService.hasApplied(studentUsername, selected.getTitle())) {
            showAlert("You already applied for this.");
            return;
        }
        
        appService.submitApplication(studentUsername, selected.getTitle(), student.getAverage());
        FileManager.saveApplications(appService.getAllApplications());
        showAlert("Application submitted!");
        loadOpportunities();
    }
    
    private void showAlert(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    public VBox getView() { return view; }
}