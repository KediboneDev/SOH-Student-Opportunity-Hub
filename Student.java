import java.io.Serializable;

public class Student extends User
{
    private static final long serialVersionUID = 1L;
    
    private String studentId;
    private String institution;
    private String qualification;
    private double average;
    private String skills;
    
    public Student()
    {
        super();
    }
    
    public Student(String username, String passwordHash, double average)
    {
        super(username, passwordHash, "STUDENT");
        this.average = average;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getInstitution() { return institution; }
    public String getQualification() { return qualification; }
    public double getAverage() { return average; }
    public String getSkills() { return skills; }
    
    // Setters
    public void setStudentId(String id) { this.studentId = id; }
    public void setInstitution(String inst) { this.institution = inst; }
    public void setQualification(String qual) { this.qualification = qual; }
    public void setAverage(double avg) { this.average = avg; }
    public void setSkills(String skills) { this.skills = skills; }
    
    @Override
    public String toString()
    {
        return String.format("%s (Student) - Avg: %.1f%%", username, average);
    }
}