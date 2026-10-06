public class Bursary extends Opportunity
{
    private String fundingType;
    
    public Bursary(){}
    
    public Bursary(double minAverage, String qualification, String company, 
                   String location, String closingDate, String title, String fundingType)
    {
        super(minAverage, qualification, company, location, closingDate, title);
        this.fundingType = fundingType;
    }
    
    public String getFundingType() { return fundingType; }
    
    @Override
    public String getType()
    {
        return "Bursary";
    }
}