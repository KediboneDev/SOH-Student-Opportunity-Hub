import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class StudentDashboard extends Application
{
    private String username;
    private BorderPane root;
    private StackPane contentArea;
    private OpportunityService oppService;
    private ApplicationService appService;
    private NotificationService notifService;
    
    public StudentDashboard() {}
    
    public StudentDashboard(String username)
    {
        this.username = username;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        ArrayList<Opportunity> opps = FileManager.loadOpportunities();
        oppService = new OpportunityService(opps);
        
        ArrayList<Applications> apps = FileManager.loadApplications();
        appService = new ApplicationService(apps);
        
        ArrayList<Notification> notifs = FileManager.loadNotifications();
        notifService = new NotificationService(notifs);
        
        root = new BorderPane();
        root.setStyle("-fx-background-color: GAINSBORO;");
        
        root.setTop(createTopBar());
        root.setLeft(createSidebar());
        
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: WHITE; -fx-padding: 20;");
        root.setCenter(contentArea);
        
        showWelcome();
        
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Student Dashboard - " + username);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            AuthService.logout();
            Platform.exit();
        });
        primaryStage.show();
    }
    
    private HBox createTopBar()
    {
        HBox bar = new HBox(15);
        bar.setStyle("-fx-background-color: DARKSLATEBLUE; -fx-padding: 15;");
        
        Label title = new Label("STUDENT OPPORTUNITY HUB");
        title.setStyle("-fx-text-fill: WHITE; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        Student student = (Student) AuthService.getUser(username);
        String displayName = (student != null && student.getName() != null) ? student.getName() : username;
        Label user = new Label("Welcome, " + displayName);
        user.setStyle("-fx-text-fill: LIGHTGRAY;");
        
        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: RED; -fx-text-fill: WHITE;");
        logout.setOnAction(e -> {
            AuthService.logout();
            Stage stage = (Stage) bar.getScene().getWindow();
            stage.close();
            new LoginScreen().start(new Stage());
        });
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        bar.getChildren().addAll(title, spacer, user, logout);
        return bar;
    }
    
    private VBox createSidebar()
    {
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: DARKSLATEGRAY; -fx-padding: 15;");
        sidebar.setPrefWidth(200);
        
        Button btnOpp = createSidebarButton("Opportunities", "DODGERBLUE");
        Button btnApps = createSidebarButton("My Applications", "MEDIUMSEAGREEN");
        Button btnNotif = createSidebarButton("Notifications", "ORANGE");
        Button btnProfile = createSidebarButton("My Profile", "PURPLE");
        
        btnOpp.setOnAction(e -> showOpportunities());
        btnApps.setOnAction(e -> showApplications());
        btnNotif.setOnAction(e -> showNotifications());
        btnProfile.setOnAction(e -> showProfile());
        
        sidebar.getChildren().addAll(btnOpp, btnApps, btnNotif, btnProfile);
        return sidebar;
    }
    
    private Button createSidebarButton(String text, String color)
    {
        Button btn = new Button(text);
        btn.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: WHITE; -fx-font-size: 14px; -fx-padding: 10;",
            color));
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }
    
    private void showWelcome()
    {
        VBox welcome = new VBox(15);
        welcome.setStyle("-fx-alignment: center; -fx-padding: 50;");
        
        Student student = (Student) AuthService.getUser(username);
        double avg = (student != null) ? student.getAverage() : 0;
        
        Label lbl1 = new Label("Welcome to Student Opportunity Hub!");
        lbl1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        Label lbl2 = new Label("Your current academic average: " + avg + "%");
        Label lbl3 = new Label("Browse opportunities that match your qualifications.");
        
        welcome.getChildren().addAll(lbl1, lbl2, lbl3);
        contentArea.getChildren().setAll(welcome);
    }
    
    private void showOpportunities()
    {
        OpportunitiesScreen screen = new OpportunitiesScreen(oppService, username, appService);
        contentArea.getChildren().setAll(screen.getView());
    }
    
    private void showApplications()
    {
        MyApplicationsScreen screen = new MyApplicationsScreen(username, appService);
        contentArea.getChildren().setAll(screen.getView());
    }
    
    private void showNotifications()
    {
        NotificationsScreen screen = new NotificationsScreen(username, notifService);
        contentArea.getChildren().setAll(screen.getView());
    }
    
    private void showProfile()
    {
        StudentProfileScreen screen = new StudentProfileScreen(username);
        contentArea.getChildren().setAll(screen.getView());
    }
}