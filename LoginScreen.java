import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginScreen extends Application
{
    private TextField txtUsername;
    private PasswordField txtPassword;
    private ComboBox<String> cmbRole;
    private Label lblStatus;
    
    @Override
    public void start(Stage primaryStage)
    {
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setStyle("-fx-padding: 20; -fx-background-color: GAINSBORO;");
        
        Label lblTitle = new Label("STUDENT OPPORTUNITY HUB");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        txtUsername = new TextField();
        txtPassword = new PasswordField();
        cmbRole = new ComboBox<>();
        cmbRole.getItems().addAll("STUDENT", "COMPANY");
        cmbRole.setValue("STUDENT");
        
        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        Button btnSignUp = new Button("Sign Up");
        btnSignUp.setStyle("-fx-background-color: MEDIUMSEAGREEN; -fx-text-fill: WHITE;");
        
        Button btnForgot = new Button("Forgot Password?");
        btnForgot.setStyle("-fx-background-color: TRANSPARENT; -fx-text-fill: DODGERBLUE; -fx-underline: true;");
        btnForgot.setOnAction(e -> {
            ForgotPasswordScreen forgot = new ForgotPasswordScreen();
            forgot.start(new Stage());
        });
        
        lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: RED;");
        
        btnLogin.setOnAction(this::loginClick);
        btnSignUp.setOnAction(this::signUpClick);
        
        int row = 0;
        pane.add(lblTitle, 0, row++, 2, 1);
        pane.add(new Label("Username:"), 0, row);
        pane.add(txtUsername, 1, row++);
        pane.add(new Label("Password:"), 0, row);
        pane.add(txtPassword, 1, row++);
        pane.add(new Label("Role:"), 0, row);
        pane.add(cmbRole, 1, row++);
        pane.add(btnLogin, 0, row);
        pane.add(btnSignUp, 1, row++);
        pane.add(btnForgot, 0, row++, 2, 1);
        pane.add(lblStatus, 0, row++, 2, 1);
        
        Scene scene = new Scene(pane, 400, 340);
        primaryStage.setTitle("SOH - Login");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> primaryStage.close());
        primaryStage.show();
    }
    
    private void loginClick(ActionEvent event)
    {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String role = cmbRole.getValue();
        
        if (username.isEmpty() || password.isEmpty()) {
            lblStatus.setText("Please enter username and password.");
            return;
        }
        
        boolean success = AuthService.login(username, password);
        
        if (success && AuthService.hasRole(role)) {
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.close();
            
            if (role.equals("STUDENT")) {
                StudentDashboard dashboard = new StudentDashboard(username);
                dashboard.start(new Stage());
            } else {
                CompanyDashboard dashboard = new CompanyDashboard(username);
                dashboard.start(new Stage());
            }
        } else {
            lblStatus.setText("Invalid username or password.");
        }
    }
    
    private void signUpClick(ActionEvent event)
    {
        SignUpScreen signUp = new SignUpScreen();
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        signUp.start(new Stage());
        stage.close();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }
}