import java.io.Serializable;
import java.time.LocalDate;

public class Applications implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String studentName;
    private double gpa;
    private String opportunityTitle;
    private ApplicationStatus status;
    private String dateApplied;
    
    public Applications(String studentName, double gpa, String opportunityTitle)
    {
        this.studentName = studentName;
        this.gpa = gpa;
        this.opportunityTitle = opportunityTitle;
        this.status = ApplicationStatus.PENDING;
        this.dateApplied = LocalDate.now().toString();
    }
    
    public String getStudentName() { return studentName; }
    public double getGpa() { return gpa; }
    public String getPost() { return opportunityTitle; }
    public ApplicationStatus getStatus() { return status; }
    public String getDateApplied() { return dateApplied; }
    
    public void setStatus(ApplicationStatus status) { this.status = status; }
    
    @Override
    public String toString()
    {
        return opportunityTitle + " - " + status + " (Applied: " + dateApplied + ")";
    }
}