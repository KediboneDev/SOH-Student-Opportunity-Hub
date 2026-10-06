import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CompanyDashboard extends Application
{
    private String username;
    private BorderPane root;
    private StackPane contentArea;
    private OpportunityService oppService;
    private ApplicationService appService;
    
    public CompanyDashboard() {}
    
    public CompanyDashboard(String username)
    {
        this.username = username;
    }
    
    @Override
    public void start(Stage primaryStage)
    {
        // Load data from files
        ArrayList<Opportunity> opps = FileManager.loadOpportunities();
        oppService = new OpportunityService(opps);
        
        ArrayList<Applications> apps = FileManager.loadApplications();
        appService = new ApplicationService(apps);
        
        // Main layout
        root = new BorderPane();
        root.setStyle("-fx-background-color: GAINSBORO;");
        
        root.setTop(createTopBar());
        root.setLeft(createSidebar());
        
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: WHITE; -fx-padding: 20;");
        root.setCenter(contentArea);
        
        showWelcome();
        
        Scene scene = new Scene(root, 1000, 650);
        primaryStage.setTitle("Company Dashboard - " + username);
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
        
        Label title = new Label("COMPANY OPPORTUNITY HUB");
        title.setStyle("-fx-text-fill: WHITE; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        Company company = (Company) AuthService.getUser(username);
        String displayName = (company != null && company.getName() != null) ? company.getName() : username;
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
        
        Button btnPost = createSidebarButton("Post Opportunity", "DODGERBLUE");
        Button btnManage = createSidebarButton("Manage Applicants", "ORANGE");
        Button btnMyOpps = createSidebarButton("My Opportunities", "PURPLE");
        Button btnChangePass = createSidebarButton("Change Password", "SLATEGRAY");
        
        btnPost.setOnAction(e -> showPostOpportunity());
        btnManage.setOnAction(e -> showManageApplicants());
        btnMyOpps.setOnAction(e -> showMyOpportunities());
        btnChangePass.setOnAction(e -> showChangePassword());
        
        sidebar.getChildren().addAll(btnPost, btnManage, btnMyOpps, btnChangePass);
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
        
        Label lbl1 = new Label("Welcome to Company Dashboard!");
        lbl1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: DARKSLATEBLUE;");
        Label lbl2 = new Label("Post opportunities, manage applicants, and find the best candidates.");
        lbl2.setStyle("-fx-font-size: 14px; -fx-text-fill: DARKGRAY;");
        
        // Statistics cards
        ArrayList<Opportunity> myOpps = oppService.getOpportunitiesByCompany(username);
        int totalOpps = myOpps.size();
        int totalApps = 0;
        int totalSelected = 0;
        
        for (Opportunity opp : myOpps) {
            ArrayList<Applications> apps = appService.getApplicationsForOpportunity(opp.getTitle());
            totalApps += apps.size();
            for (Applications app : apps) {
                if (app.getStatus() == ApplicationStatus.SELECTED) {
                    totalSelected++;
                }
            }
        }
        
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(javafx.geometry.Pos.CENTER);
        statsBox.setPadding(new Insets(20));
        
        VBox card1 = createStatCard("Opportunities Posted", String.valueOf(totalOpps), "DODGERBLUE");
        VBox card2 = createStatCard("Total Applicants", String.valueOf(totalApps), "ORANGE");
        VBox card3 = createStatCard("Candidates Selected", String.valueOf(totalSelected), "LIMEGREEN");
        
        statsBox.getChildren().addAll(card1, card2, card3);
        
        welcome.getChildren().addAll(lbl1, lbl2, statsBox);
        contentArea.getChildren().setAll(welcome);
    }
    
    private VBox createStatCard(String label, String value, String color)
    {
        VBox card = new VBox(5);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setStyle("-fx-background-color: " + color + "; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10;");
        card.setPrefWidth(150);
        
        Label lblValue = new Label(value);
        lblValue.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: WHITE;");
        
        Label lblLabel = new Label(label);
        lblLabel.setStyle("-fx-text-fill: WHITE; -fx-font-size: 12px;");
        
        card.getChildren().addAll(lblValue, lblLabel);
        return card;
    }
    
    private void showPostOpportunity()
    {
        PostOpportunityScreen screen = new PostOpportunityScreen(username, oppService);
        contentArea.getChildren().setAll(screen.getView());
    }
    
    private void showManageApplicants()
    {
        ManageApplicantsScreen screen = new ManageApplicantsScreen(username, oppService, appService);
        contentArea.getChildren().setAll(screen.getView());
    }
    
    private void showChangePassword()
    {
        ChangePasswordScreen screen = new ChangePasswordScreen(username);
        contentArea.getChildren().setAll(screen.getView());
    }
    
    private void showMyOpportunities()
    {
        MyOpportunitiesScreen screen = new MyOpportunitiesScreen(username, oppService);
        contentArea.getChildren().setAll(screen.getView());
    }
}