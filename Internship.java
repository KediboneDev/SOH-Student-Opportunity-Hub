public class Internship extends Opportunity
{
    private int durationMonths;
    
    public Internship(){}
    
    public Internship(double minAverage, String qualification, String company, 
                      String location, String closingDate, String title, int durationMonths)
    {
        super(minAverage, qualification, company, location, closingDate, title);
        // Validate: duration must be at least 6 months
        if (durationMonths < 6) {
            this.durationMonths = 6;
        } else {
            this.durationMonths = durationMonths;
        }
    }
    
    public int getDurationMonths() { return durationMonths; }
    
    @Override
    public String getType()
    {
        return "Internship";
    }
}