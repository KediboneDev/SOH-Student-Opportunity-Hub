import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ForgotPasswordScreen extends Application
{
    private TextField txtUsername;
    private TextField txtIdNumber;
    private TextField txtSecurityAnswer;
    private ComboBox<String> cmbRole;
    private Label lblStatus;
    private Label lblSecurityQuestion;
    private Button btnVerify;
    private Button btnBack;
    private int step = 1;
    private User currentUser;
    private Stage currentStage;
    
    @Override
    public void start(Stage primaryStage)
    {
        this.currentStage = primaryStage;
        
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setStyle("-fx-padding: 20; -fx-background-color: GAINSBORO;");
        
        Label lblTitle = new Label("Reset Password");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        Label lblInstruction = new Label("Step 1: Enter your username and ID to verify your identity.");
        lblInstruction.setStyle("-fx-font-size: 12px; -fx-text-fill: GRAY;");
        
        txtUsername = new TextField();
        txtIdNumber = new TextField();
        txtSecurityAnswer = new TextField();
        
        lblSecurityQuestion = new Label();
        lblSecurityQuestion.setStyle("-fx-font-style: italic; -fx-text-fill: DARKSLATEBLUE;");
        lblSecurityQuestion.setVisible(false);
        
        cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("STUDENT", "COMPANY");
        cmbRole.setValue("STUDENT");
        
        btnVerify = new Button("Verify Identity");
        btnVerify.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        btnBack = new Button("Back to Login");
        btnBack.setStyle("-fx-background-color: LIGHTGRAY;");
        
        lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: RED;");
        
        txtSecurityAnswer.setVisible(false);
        
        btnVerify.setOnAction(e -> handleVerification());
        btnBack.setOnAction(e -> {
            LoginScreen login = new LoginScreen();
            try {
                login.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            currentStage.close();
        });
        
        int row = 0;
        pane.add(lblTitle, 0, row++, 2, 1);
        pane.add(lblInstruction, 0, row++, 2, 1);
        pane.add(new Label("Role:"), 0, row);
        pane.add(cmbRole, 1, row++);
        pane.add(new Label("Username:"), 0, row);
        pane.add(txtUsername, 1, row++);
        pane.add(new Label("ID Number:"), 0, row);
        pane.add(txtIdNumber, 1, row++);
        pane.add(lblSecurityQuestion, 0, row, 2, 1);
        row++;
        pane.add(new Label("Answer:"), 0, row);
        pane.add(txtSecurityAnswer, 1, row++);
        pane.add(btnVerify, 0, row);
        pane.add(btnBack, 1, row++);
        pane.add(lblStatus, 0, row++, 2, 1);
        
        Scene scene = new Scene(pane, 500, 450);
        primaryStage.setTitle("SOH - Forgot Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void handleVerification()
    {
        if (step == 1) {
            String username = txtUsername.getText().trim();
            String idNumber = txtIdNumber.getText().trim();
            String role = cmbRole.getValue();
            
            if (username.isEmpty() || idNumber.isEmpty()) {
                lblStatus.setText("Please enter both username and ID number.");
                return;
            }
            
            currentUser = AuthService.getUser(username);
            
            if (currentUser == null) {
                lblStatus.setText("Username not found.");
                return;
            }
            
            if (!currentUser.getRole().equalsIgnoreCase(role)) {
                lblStatus.setText("Role does not match.");
                return;
            }
            
            boolean idValid = false;
            if (role.equals("STUDENT") && currentUser instanceof Student) {
                Student student = (Student) currentUser;
                idValid = idNumber.equals(student.getStudentId());
            } else if (role.equals("COMPANY") && currentUser instanceof Company) {
                Company company = (Company) currentUser;
                idValid = idNumber.equals(company.getCompanyId());
            }
            
            if (!idValid) {
                lblStatus.setText("ID number does not match.");
                return;
            }
            
            step = 2;
            String securityQuestion = currentUser.getSecurityQuestion();
            
            lblSecurityQuestion.setText("Security Question: " + securityQuestion);
            lblSecurityQuestion.setVisible(true);
            txtSecurityAnswer.setVisible(true);
            btnVerify.setText("Reset Password");
            
            lblStatus.setText("Please answer your security question.");
            
        } else if (step == 2) {
            String answer = txtSecurityAnswer.getText().trim().toLowerCase();
            
            if (answer.isEmpty()) {
                lblStatus.setText("Please answer the security question.");
                return;
            }
            
            String storedHash = currentUser.getSecurityAnswer();
            boolean answerValid = PasswordUtil.verifySecurityAnswer(answer, storedHash);
            
            if (!answerValid) {
                lblStatus.setText("Security answer is incorrect.");
                return;
            }
            
            String tempPassword = generateTempPassword();
            String hashedPassword = PasswordUtil.hashPassword(tempPassword);
            
            currentUser.setPasswordHash(hashedPassword);
            AuthService.updateUser(currentUser);
            
            lblStatus.setText("TEMPORARY PASSWORD: " + tempPassword + 
                             "\n\nLogin with this password.\nPlease close this window.");
            lblStatus.setStyle("-fx-text-fill: GREEN;");
            
            btnVerify.setDisable(true);
            
            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(10))
                .setOnFinished(e -> {
                    LoginScreen login = new LoginScreen();
                    try {
                        login.start(new Stage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    currentStage.close();
                });
        }
    }
    
    private String generateTempPassword()
    {
        String chars = "ABCDEFGHJKMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            int index = (int)(Math.random() * chars.length());
            temp.append(chars.charAt(index));
        }
        return temp.toString();
    }
}