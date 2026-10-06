import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.LocalDate;

public class PostOpportunityScreen
{
    private VBox view;
    private TextField txtTitle, txtMinAvg, txtLocation, txtQualification;
    private ComboBox<String> cmbType;
    private DatePicker dpClosingDate;
    private Label lblDateError;
    
    // Simple fields
    private TextField txtFundingType;
    private Label lblDurationValue;
    private Button btnDurationUp, btnDurationDown;
    private int currentDuration = 6;
    private Label lblExtraInfo;
    
    public PostOpportunityScreen(String companyUsername, OpportunityService service)
    {
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("Post New Opportunity");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        
        int row = 0;
        
        form.add(new Label("Title:"), 0, row);
        txtTitle = new TextField();
        txtTitle.setPrefWidth(300);
        form.add(txtTitle, 1, row++);
        
        form.add(new Label("Type:"), 0, row);
        cmbType = new ComboBox<>();
        cmbType.getItems().addAll("Bursary", "Internship");
        cmbType.setValue("Bursary");
        form.add(cmbType, 1, row++);
        
        form.add(new Label("Min Average (%):"), 0, row);
        txtMinAvg = new TextField();
        form.add(txtMinAvg, 1, row++);
        
        form.add(new Label("Location:"), 0, row);
        txtLocation = new TextField();
        form.add(txtLocation, 1, row++);
        
        form.add(new Label("Qualification:"), 0, row);
        txtQualification = new TextField();
        form.add(txtQualification, 1, row++);
        
        // Extra info section (changes based on type)
        lblExtraInfo = new Label("Funding Type:");
        txtFundingType = new TextField();
        txtFundingType.setPromptText("e.g., Full coverage, Tuition only");
        
        // Duration with up/down buttons
        lblDurationValue = new Label("6");
        lblDurationValue.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 5 15 5 15; -fx-border-color: LIGHTGRAY; -fx-border-width: 1;");
        lblDurationValue.setAlignment(Pos.CENTER);
        lblDurationValue.setPrefWidth(50);
        
        btnDurationUp = new Button("+");
        btnDurationDown = new Button("-");
        btnDurationUp.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE; -fx-font-weight: bold;");
        btnDurationDown.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE; -fx-font-weight: bold;");
        
        btnDurationUp.setOnAction(e -> {
            if (currentDuration < 24) {
                currentDuration++;
                lblDurationValue.setText(String.valueOf(currentDuration));
            }
        });
        
        btnDurationDown.setOnAction(e -> {
            if (currentDuration > 6) {
                currentDuration--;
                lblDurationValue.setText(String.valueOf(currentDuration));
            }
        });
        
        HBox durationBox = new HBox(5);
        durationBox.setAlignment(Pos.CENTER_LEFT);
        durationBox.getChildren().addAll(btnDurationDown, lblDurationValue, btnDurationUp);
        
        form.add(lblExtraInfo, 0, row);
        form.add(txtFundingType, 1, row++);
        
        // Hide/show based on type
        cmbType.setOnAction(e -> {
            if (cmbType.getValue().equals("Bursary")) {
                lblExtraInfo.setText("Funding Type:");
                txtFundingType.setVisible(true);
                durationBox.setVisible(false);
                txtFundingType.clear();
            } else {
                lblExtraInfo.setText("Duration (months):");
                txtFundingType.setVisible(false);
                durationBox.setVisible(true);
                currentDuration = 6;
                lblDurationValue.setText("6");
            }
        });
        
        // Add duration box to form after the extra info row
        form.add(durationBox, 1, row);
        row++;
        
        form.add(new Label("Closing Date:"), 0, row);
        dpClosingDate = new DatePicker(LocalDate.now().plusMonths(1));
        lblDateError = new Label();
        lblDateError.setStyle("-fx-text-fill: RED; -fx-font-size: 10px;");
        form.add(dpClosingDate, 1, row);
        form.add(lblDateError, 1, ++row);
        row++;
        
        // Set initial visibility
        txtFundingType.setVisible(true);
        durationBox.setVisible(false);
        
        dpClosingDate.setOnAction(e -> validateClosingDate());
        
        Button btnSubmit = new Button("Publish");
        btnSubmit.setStyle("-fx-background-color: LIMEGREEN; -fx-text-fill: WHITE;");
        btnSubmit.setOnAction(e -> {
            String titleText = txtTitle.getText().trim();
            String avgText = txtMinAvg.getText().trim();
            String location = txtLocation.getText().trim();
            String qualification = txtQualification.getText().trim();
            String type = cmbType.getValue();
            LocalDate closingDate = dpClosingDate.getValue();
            
            // Validation
            if (titleText.isEmpty()) {
                showAlert("Title is required.");
                return;
            }
            if (avgText.isEmpty()) {
                showAlert("Minimum average is required.");
                return;
            }
            
            double minAvg;
            try {
                minAvg = Double.parseDouble(avgText);
                if (minAvg < 0 || minAvg > 100) {
                    showAlert("Average must be 0-100.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showAlert("Average must be a number.");
                return;
            }
            
            if (location.isEmpty()) {
                showAlert("Location is required.");
                return;
            }
            if (qualification.isEmpty()) {
                showAlert("Qualification is required.");
                return;
            }
            if (closingDate == null) {
                showAlert("Closing date is required.");
                return;
            }
            if (closingDate.isBefore(LocalDate.now())) {
                showAlert("Closing date cannot be in the past.");
                return;
            }
            
            Company company = (Company) AuthService.getUser(companyUsername);
            String companyName = (company != null && company.getName() != null) ? company.getName() : companyUsername;
            
            Opportunity opp;
            if (type.equals("Bursary")) {
                String funding = txtFundingType.getText().trim();
                if (funding.isEmpty()) {
                    funding = "Not specified";
                }
                opp = new Bursary(minAvg, qualification, companyName, 
                    location, closingDate.toString(), titleText, funding);
            } else {
                int duration = currentDuration;
                opp = new Internship(minAvg, qualification, companyName, 
                    location, closingDate.toString(), titleText, duration);
            }
            
            service.addOpportunity(opp);
            FileManager.saveOpportunities(service.getAllOpportunities());
            
            showAlert("Opportunity posted!");
            
            // Clear form
            txtTitle.clear();
            txtMinAvg.clear();
            txtLocation.clear();
            txtQualification.clear();
            txtFundingType.clear();
            currentDuration = 6;
            lblDurationValue.setText("6");
            dpClosingDate.setValue(LocalDate.now().plusMonths(1));
            cmbType.setValue("Bursary");
        });
        
        form.add(btnSubmit, 1, row);
        view.getChildren().addAll(title, form);
    }
    
    private boolean validateClosingDate()
    {
        LocalDate selected = dpClosingDate.getValue();
        if (selected == null) return false;
        
        if (selected.isBefore(LocalDate.now())) {
            lblDateError.setText("Closing date cannot be in the past!");
            return false;
        } else {
            lblDateError.setText("");
            return true;
        }
    }
    
    private void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Post Opportunity");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() { return view; }
}