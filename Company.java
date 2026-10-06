import java.util.ArrayList;

public class Company extends User
{
    private static final long serialVersionUID = 1L;
    
    private String companyId;
    private String industry;
    private String location;
    private ArrayList<String> postedOpportunityIds;
    
    public Company()
    {
        super();
        this.postedOpportunityIds = new ArrayList<>();
    }
    
    public Company(String username, String passwordHash, String companyName)
    {
        super(username, passwordHash, "COMPANY");
        this.name = companyName;
        this.postedOpportunityIds = new ArrayList<>();
    }
    
    public String getCompanyId() { return companyId; }
    public String getIndustry() { return industry; }
    public String getLocation() { return location; }
    public ArrayList<String> getPostedOpportunityIds() { return postedOpportunityIds; }
    
    public void setCompanyId(String id) { this.companyId = id; }
    public void setIndustry(String industry) { this.industry = industry; }
    public void setLocation(String location) { this.location = location; }
    
    public void addOpportunityId(String oppId)
    {
        if (!postedOpportunityIds.contains(oppId)) {
            postedOpportunityIds.add(oppId);
        }
    }
    
    @Override
    public String toString()
    {
        return String.format("%s (Company) - %s", username, name);
    }
}