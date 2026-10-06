import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ChangePasswordScreen
{
    private VBox view;
    private String username;

    private PasswordField txtCurrent;
    private PasswordField txtNew;
    private PasswordField txtConfirm;
    private Label lblStatus;

    public ChangePasswordScreen(String username)
    {
        this.username = username;

        view = new VBox(15);
        view.setPadding(new Insets(20));

        Label title = new Label("Change Password");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");

        Label lblInfo = new Label("Enter your current password, then choose a new one.");
        lblInfo.setStyle("-fx-text-fill: GRAY; -fx-font-size: 12px;");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(10));

        int row = 0;

        form.add(new Label("Current Password:"), 0, row);
        txtCurrent = new PasswordField();
        txtCurrent.setPrefWidth(280);
        form.add(txtCurrent, 1, row++);

        form.add(new Label("New Password:"), 0, row);
        txtNew = new PasswordField();
        txtNew.setPrefWidth(280);
        form.add(txtNew, 1, row++);

        form.add(new Label("Confirm New Password:"), 0, row);
        txtConfirm = new PasswordField();
        txtConfirm.setPrefWidth(280);
        form.add(txtConfirm, 1, row++);

        lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: RED;");

        Button btnSave = new Button("Update Password");
        btnSave.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE; -fx-font-size: 13px; -fx-padding: 8 16;");
        btnSave.setOnAction(e -> handleChangePassword());

        Button btnClear = new Button("Clear");
        btnClear.setStyle("-fx-background-color: LIGHTGRAY; -fx-font-size: 13px; -fx-padding: 8 16;");
        btnClear.setOnAction(e -> {
            txtCurrent.clear();
            txtNew.clear();
            txtConfirm.clear();
            lblStatus.setText("");
        });

        HBox buttons = new HBox(10);
        buttons.getChildren().addAll(btnSave, btnClear);

        view.getChildren().addAll(title, lblInfo, form, buttons, lblStatus);
    }

    private void handleChangePassword()
    {
        String current = txtCurrent.getText();
        String newPass = txtNew.getText();
        String confirm = txtConfirm.getText();

        if (current.isEmpty() || newPass.isEmpty() || confirm.isEmpty())
        {
            showError("Please fill in all fields.");
            return;
        }

        // Verify current password
        if (!PasswordUtil.verifyPassword(current, AuthService.getUser(username).getPasswordHash()))
        {
            showError("Current password is incorrect.");
            return;
        }

        // Check new and confirm match
        if (!newPass.equals(confirm))
        {
            showError("New passwords do not match.");
            return;
        }

        // Check new password meets requirements
        if (!PasswordUtil.isValidPassword(newPass))
        {
            showError("New password does not meet requirements. Must be at least 8 characters with uppercase, lowercase, number, and special character.");
            return;
        }

        // Check new password is not the same as current
        if (PasswordUtil.verifyPassword(newPass, AuthService.getUser(username).getPasswordHash()))
        {
            showError("New password must be different from your current password.");
            return;
        }

        // Apply the change
        String newHash = PasswordUtil.hashPassword(newPass);
        AuthService.getUser(username).setPasswordHash(newHash);
        AuthService.updateUser(AuthService.getUser(username));

        txtCurrent.clear();
        txtNew.clear();
        txtConfirm.clear();

        lblStatus.setText("Password updated successfully!");
        lblStatus.setStyle("-fx-text-fill: GREEN;");

        new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3))
            .setOnFinished(e -> {
                lblStatus.setText("");
                lblStatus.setStyle("-fx-text-fill: RED;");
            });
    }

    private void showError(String message)
    {
        lblStatus.setText(message);
        lblStatus.setStyle("-fx-text-fill: RED;");
    }

    public VBox getView() { return view; }
}
