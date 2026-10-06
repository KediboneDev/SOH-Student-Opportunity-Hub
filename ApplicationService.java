import java.util.ArrayList;

public class ApplicationService
{
    private ArrayList<Applications> applications;
    
    public ApplicationService(ArrayList<Applications> applications)
    {
        this.applications = (applications == null) ? new ArrayList<>() : applications;
    }
    
    public boolean submitApplication(String studentName, String opportunityTitle, double gpa)
    {
        if (hasApplied(studentName, opportunityTitle)) {
            return false;
        }
        Applications app = new Applications(studentName, gpa, opportunityTitle);
        applications.add(app);
        return true;
    }
    
    public boolean hasApplied(String studentName, String opportunityTitle)
    {
        for (Applications app : applications) {
            if (app.getStudentName().equals(studentName) && app.getPost().equals(opportunityTitle)) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Applications> getApplicationsForStudent(String studentName)
    {
        ArrayList<Applications> result = new ArrayList<>();
        for (Applications app : applications) {
            if (app.getStudentName().equals(studentName)) {
                result.add(app);
            }
        }
        return result;
    }
    
    public ArrayList<Applications> getApplicationsForOpportunity(String opportunityTitle)
    {
        ArrayList<Applications> result = new ArrayList<>();
        for (Applications app : applications) {
            if (app.getPost().equals(opportunityTitle)) {
                result.add(app);
            }
        }
        return result;
    }
    
    public void updateApplication(Applications updatedApp)
    {
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i).getStudentName().equals(updatedApp.getStudentName()) &&
                applications.get(i).getPost().equals(updatedApp.getPost())) {
                applications.set(i, updatedApp);
                break;
            }
        }
    }
    
    public ArrayList<Applications> getAllApplications()
    {
        return applications;
    }
}