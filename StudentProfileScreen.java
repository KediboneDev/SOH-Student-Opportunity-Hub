import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class StudentProfileScreen
{
    private VBox view;
    private String studentUsername;
    
    private TextField txtName;
    private TextField txtEmail;
    private TextField txtInstitution;
    private TextField txtQualification;
    private TextField txtAverage;
    private TextArea txtSkills;
    private Label lblStatus;
    
    public StudentProfileScreen(String studentUsername)
    {
        this.studentUsername = studentUsername;
        
        view = new VBox(15);
        view.setPadding(new Insets(20));
        
        Label title = new Label("My Profile");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        
        Label lblNote = new Label("Note: Institution, Qualification, and GPA cannot be changed.");
        lblNote.setStyle("-fx-text-fill: RED; -fx-font-size: 11px;");
        
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(12);
        form.setPadding(new Insets(10));
        
        int row = 0;
        
        form.add(new Label("Full Name:"), 0, row);
        txtName = new TextField();
        txtName.setPrefWidth(300);
        txtName.setEditable(false);  // READ ONLY
        txtName.setStyle("-fx-background-color: LIGHTGRAY;");
        form.add(txtName, 1, row++);
        
        form.add(new Label("Email:"), 0, row);
        txtEmail = new TextField();
        txtEmail.setEditable(false);  // READ ONLY
        txtEmail.setStyle("-fx-background-color: LIGHTGRAY;");
        form.add(txtEmail, 1, row++);
        
        form.add(new Label("Institution:"), 0, row);
        txtInstitution = new TextField();
        txtInstitution.setEditable(false);  // READ ONLY - CANNOT CHANGE
        txtInstitution.setStyle("-fx-background-color: LIGHTGRAY;");
        form.add(txtInstitution, 1, row++);
        
        form.add(new Label("Qualification:"), 0, row);
        txtQualification = new TextField();
        txtQualification.setEditable(false);  // READ ONLY - CANNOT CHANGE
        txtQualification.setStyle("-fx-background-color: LIGHTGRAY;");
        form.add(txtQualification, 1, row++);
        
        form.add(new Label("Academic Average (%):"), 0, row);
        txtAverage = new TextField();
        txtAverage.setEditable(false);  // READ ONLY - CANNOT CHANGE
        txtAverage.setStyle("-fx-background-color: LIGHTGRAY;");
        form.add(txtAverage, 1, row++);
        
        form.add(new Label("Skills:"), 0, row);
        txtSkills = new TextArea();
        txtSkills.setPrefHeight(80);
        txtSkills.setPrefWidth(300);
        txtSkills.setPromptText("e.g., Java, Python, SQL, Communication, Leadership");
        txtSkills.setEditable(true);  // ONLY THIS CAN BE EDITED
        form.add(txtSkills, 1, row++);
        
        lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: RED;");
        
        HBox buttons = new HBox(10);
        Button btnSave = new Button("Save Skills");
        Button btnRefresh = new Button("Refresh");
        Button btnChangePassword = new Button("Change Password");
        
        btnSave.setStyle("-fx-background-color: LIMEGREEN; -fx-text-fill: WHITE;");
        btnSave.setOnAction(e -> saveSkillsOnly());
        btnRefresh.setOnAction(e -> loadProfile());
        btnChangePassword.setStyle("-fx-background-color: DODGERBLUE; -fx-text-fill: WHITE;");
        btnChangePassword.setOnAction(e -> {
            ChangePasswordScreen screen = new ChangePasswordScreen(studentUsername);
            view.getChildren().setAll(screen.getView());
        });
        
        buttons.getChildren().addAll(btnSave, btnRefresh, btnChangePassword);
        
        view.getChildren().addAll(title, lblNote, form, buttons, lblStatus);
        
        loadProfile();
    }
    
    private void loadProfile()
    {
        Student student = (Student) AuthService.getUser(studentUsername);
        if (student != null) {
            if (student.getName() != null) txtName.setText(student.getName());
            if (student.getEmail() != null) txtEmail.setText(student.getEmail());
            if (student.getInstitution() != null) txtInstitution.setText(student.getInstitution());
            if (student.getQualification() != null) txtQualification.setText(student.getQualification());
            if (student.getAverage() > 0) txtAverage.setText(String.valueOf((int)student.getAverage()));
            if (student.getSkills() != null) txtSkills.setText(student.getSkills());
        }
    }
    
    private void saveSkillsOnly()
    {
        String skills = txtSkills.getText().trim();
        
        Student student = (Student) AuthService.getUser(studentUsername);
        if (student != null) {
            student.setSkills(skills);
            
            // Update in students.dat
            ArrayList<Student> allStudents = FileManager.loadStudents();
            for (int i = 0; i < allStudents.size(); i++) {
                if (allStudents.get(i).getUsername().equals(studentUsername)) {
                    allStudents.set(i, student);
                    break;
                }
            }
            FileManager.saveStudents(allStudents);
            
            lblStatus.setText("Skills updated successfully!");
            lblStatus.setStyle("-fx-text-fill: GREEN;");
            
            new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3))
                .setOnFinished(e -> {
                    lblStatus.setText("");
                    lblStatus.setStyle("-fx-text-fill: RED;");
                });
        }
    }
    
    public VBox getView() { return view; }
}