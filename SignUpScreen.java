import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SignUpScreen extends Application
{
    private TextField txtUsername;
    private PasswordField txtPassword;
    private PasswordField txtConfirm;
    private ComboBox<String> cmbRole;
    private TextField txtName;
    private TextField txtEmail;
    private TextField txtInstitution;
    private TextField txtQualification;
    private TextField txtAverage;
    private TextArea txtSkills;
    private TextField txtStudentId;
    private TextField txtCompanyId;
    private ComboBox<String> cmbSecurityQuestion;
    private PasswordField txtSecurityAnswer;
    private Label lblStatus;
    private Label lblStrength;
    
    @Override
    public void start(Stage primaryStage)
    {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setStyle("-fx-padding: 20; -fx-background-color: GAINSBORO;");
        
        Label lblTitle = new Label("Create New Account");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        txtUsername = new TextField();
        txtPassword = new PasswordField();
        txtConfirm = new PasswordField();
        cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("STUDENT", "COMPANY");
        cmbRole.setValue("STUDENT");
        txtName = new TextField();
        txtEmail = new TextField();
        txtInstitution = new TextField();
        txtQualification = new TextField();
        txtAverage = new TextField();
        txtSkills = new TextArea();
        txtSkills.setPrefHeight(60);
        txtSkills.setPrefWidth(250);
        txtStudentId = new TextField();
        txtCompanyId = new TextField();
        
        // Security Question
        cmbSecurityQuestion = new ComboBox<>();
        cmbSecurityQuestion.getItems().addAll(
            "What was your first pet's name?",
            "What is your mother's maiden name?",
            "What was the name of your first school?",
            "Who was your favourite primary school teacher?",
            "What is your next of kin's full name?",
            "What city were you born in?"
        );
        cmbSecurityQuestion.setValue("What was your first pet's name?");
        txtSecurityAnswer = new PasswordField();
        txtSecurityAnswer.setPromptText("Answer (case insensitive)");
        
        Label lblStudentId = new Label("Student/Government ID:*");
        Label lblCompanyId = new Label("Company Registration Number:*");
        
        lblStrength = new Label();
        lblStrength.setStyle("-fx-font-size: 9px;");
        
        txtPassword.textProperty().addListener((obs, old, newVal) -> {
            if (PasswordUtil.isValidPassword(newVal)) {
                lblStrength.setText("Strong password");
                lblStrength.setStyle("-fx-text-fill: GREEN;");
            } else {
                lblStrength.setText(PasswordUtil.getPasswordValidationMessage(newVal));
                lblStrength.setStyle("-fx-text-fill: ORANGE;");
            }
        });
        
        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-background-color: LIMEGREEN; -fx-text-fill: WHITE;");
        Button btnBack = new Button("Back to Login");
        btnBack.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        
        btnRegister.setOnAction(this::registerClick);
        btnBack.setOnAction(e -> {
            LoginScreen login = new LoginScreen();
            login.start(new Stage());
            primaryStage.close();
        });
        
        lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: RED;");
        
        int row = 0;
        pane.add(lblTitle, 0, row++, 2, 1);
        
        pane.add(new Label("Username:*"), 0, row);
        pane.add(txtUsername, 1, row++);
        
        pane.add(new Label("Full Name:*"), 0, row);
        pane.add(txtName, 1, row++);
        
        pane.add(new Label("Email:*"), 0, row);
        pane.add(txtEmail, 1, row++);
        
        pane.add(new Label("Role:*"), 0, row);
        pane.add(cmbRole, 1, row++);
        
        // Student fields
        pane.add(lblStudentId, 0, row);
        pane.add(txtStudentId, 1, row++);
        
        pane.add(new Label("Institution:*"), 0, row);
        pane.add(txtInstitution, 1, row++);
        
        pane.add(new Label("Qualification:*"), 0, row);
        pane.add(txtQualification, 1, row++);
        
        pane.add(new Label("Academic Average (%):*"), 0, row);
        pane.add(txtAverage, 1, row++);
        
        // Company fields
        pane.add(lblCompanyId, 0, row);
        pane.add(txtCompanyId, 1, row++);
        
        pane.add(new Label("Skills:"), 0, row);
        pane.add(txtSkills, 1, row++);
        
        // Security Questions (for both)
        pane.add(new Label("Security Question:*"), 0, row);
        pane.add(cmbSecurityQuestion, 1, row++);
        
        pane.add(new Label("Security Answer:*"), 0, row);
        pane.add(txtSecurityAnswer, 1, row++);
        
        pane.add(new Label("Password:*"), 0, row);
        pane.add(txtPassword, 1, row++);
        pane.add(lblStrength, 1, row++);
        
        pane.add(new Label("Confirm Password:*"), 0, row);
        pane.add(txtConfirm, 1, row++);
        
        pane.add(btnRegister, 0, row);
        pane.add(btnBack, 1, row++);
        pane.add(lblStatus, 0, row++, 2, 1);
        
        // Hide/show based on role
        cmbRole.setOnAction(e -> {
            boolean isStudent = cmbRole.getValue().equals("STUDENT");
            txtStudentId.setVisible(isStudent);
            txtInstitution.setVisible(isStudent);
            txtQualification.setVisible(isStudent);
            txtAverage.setVisible(isStudent);
            txtCompanyId.setVisible(!isStudent);
            lblStudentId.setVisible(isStudent);
            lblCompanyId.setVisible(!isStudent);
        });
        
        boolean isStudent = cmbRole.getValue().equals("STUDENT");
        txtStudentId.setVisible(isStudent);
        txtInstitution.setVisible(isStudent);
        txtQualification.setVisible(isStudent);
        txtAverage.setVisible(isStudent);
        txtCompanyId.setVisible(!isStudent);
        lblStudentId.setVisible(isStudent);
        lblCompanyId.setVisible(!isStudent);
        
        Scene scene = new Scene(pane, 550, 700);
        primaryStage.setTitle("SOH - Sign Up");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> primaryStage.close());
        primaryStage.show();
    }
    
    private void registerClick(ActionEvent event)
    {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirm = txtConfirm.getText();
        String role = cmbRole.getValue();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String securityQuestion = cmbSecurityQuestion.getValue();
        String securityAnswer = txtSecurityAnswer.getText().trim().toLowerCase();
        
        // Basic validation
        if (username.isEmpty() || name.isEmpty() || email.isEmpty()) {
            lblStatus.setText("All fields required.");
            return;
        }
        
        if (securityAnswer.isEmpty()) {
            lblStatus.setText("Security answer is required.");
            return;
        }
        
        if (!InputValidator.isValidUsername(username)) {
            lblStatus.setText("Username: 3-30 letters, numbers, _ or -");
            return;
        }
        
        if (!InputValidator.isValidEmail(email)) {
            lblStatus.setText("Invalid email format.");
            return;
        }
        
        if (!password.equals(confirm)) {
            lblStatus.setText("Passwords do not match.");
            return;
        }
        
        if (!PasswordUtil.isValidPassword(password)) {
            lblStatus.setText(PasswordUtil.getPasswordValidationMessage(password));
            return;
        }
        
        // STUDENT validation
        if (role.equals("STUDENT")) {
            String studentId = txtStudentId.getText().trim();
            String institution = txtInstitution.getText().trim();
            String qualification = txtQualification.getText().trim();
            String avgText = txtAverage.getText().trim();
            
            if (studentId.isEmpty()) {
                lblStatus.setText("Student ID is required.");
                return;
            }
            if (institution.isEmpty()) {
                lblStatus.setText("Institution is required.");
                return;
            }
            if (qualification.isEmpty()) {
                lblStatus.setText("Qualification is required.");
                return;
            }
            if (avgText.isEmpty()) {
                lblStatus.setText("Academic average is required.");
                return;
            }
            
            try {
                double avg = Double.parseDouble(avgText);
                if (avg < 0 || avg > 100) {
                    lblStatus.setText("Average must be between 0 and 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                lblStatus.setText("Average must be a number.");
                return;
            }
        }
        
        // COMPANY validation
        if (role.equals("COMPANY")) {
            String companyId = txtCompanyId.getText().trim();
            if (companyId.isEmpty()) {
                lblStatus.setText("Company registration number is required.");
                return;
            }
        }
        
        // Register user
        boolean success = AuthService.register(username, password, role);
        
        if (success) {
            User user = AuthService.getUser(username);
            user.setName(name);
            user.setEmail(email);
            user.setSecurityQuestion(securityQuestion);
            
            // Hash the security answer
            String hashedAnswer = PasswordUtil.hashSecurityAnswer(securityAnswer);
            user.setSecurityAnswer(hashedAnswer);
            
            if (user instanceof Student && role.equals("STUDENT")) {
                Student student = (Student) user;
                student.setStudentId(txtStudentId.getText().trim());
                student.setInstitution(txtInstitution.getText().trim());
                student.setQualification(txtQualification.getText().trim());
                student.setSkills(txtSkills.getText().trim());
                student.setAverage(Double.parseDouble(txtAverage.getText().trim()));
                
                ArrayList<Student> students = FileManager.loadStudents();
                students.add(student);
                FileManager.saveStudents(students);
            }
            
            if (user instanceof Company && role.equals("COMPANY")) {
                Company company = (Company) user;
                company.setCompanyId(txtCompanyId.getText().trim());
                
                ArrayList<Company> companies = FileManager.loadCompanies();
                companies.add(company);
                FileManager.saveCompanies(companies);
            }
            
            lblStatus.setText("Registration successful! Please login.");
            lblStatus.setStyle("-fx-text-fill: GREEN;");
            
            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2))
                .setOnFinished(e -> {
                    LoginScreen login = new LoginScreen();
                    login.start(new Stage());
                    ((Stage) txtUsername.getScene().getWindow()).close();
                });
        } else {
            lblStatus.setText("Username already exists.");
        }
    }
}