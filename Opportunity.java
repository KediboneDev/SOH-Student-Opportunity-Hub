import java.io.Serializable;

public abstract class Opportunity implements Comparable<Opportunity>, Serializable
{
    private double minAverage;
    private String qualification;
    private String company;
    private String location;
    private String closingDate;
    private String title;
    
    public Opportunity() {}
    
    public Opportunity(double minAverage, String qualification, String company, 
                       String location, String closingDate, String title)
    {
        this.minAverage = minAverage;
        this.qualification = qualification;
        this.company = company;
        this.location = location;
        this.closingDate = closingDate;
        this.title = title;
    }
    
    public double getMinAverage() { return minAverage; }
    public String getQualification() { return qualification; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public String getClosingDate() { return closingDate; }
    public String getTitle() { return title; }
    
    public void setTitle(String title) { this.title = title; }
    public void setMinAverage(double minAverage) { this.minAverage = minAverage; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setLocation(String location) { this.location = location; }
    public void setClosingDate(String closingDate) { this.closingDate = closingDate; }
    
    public boolean isEligible(double studentAverage)
    {
        return studentAverage >= minAverage;
    }
    
    @Override
    public int compareTo(Opportunity other) {
        return this.closingDate.compareTo(other.closingDate);
    }
    
    public abstract String getType();
}