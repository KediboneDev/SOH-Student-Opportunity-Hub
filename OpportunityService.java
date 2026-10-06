import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class OpportunityService
{
    private ArrayList<Opportunity> opportunities;
    
    public OpportunityService(ArrayList<Opportunity> opportunities)
    {
        this.opportunities = (opportunities == null) ? new ArrayList<>() : opportunities;
    }
    
    // Search by keyword in title, company, location, or qualification
    public ArrayList<Opportunity> search(String keyword)
    {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(opportunities);
        }
        
        String kw = keyword.toLowerCase().trim();
        ArrayList<Opportunity> results = new ArrayList<>();
        
        for (Opportunity opp : opportunities) {
            if (opp.getTitle().toLowerCase().contains(kw) ||
                opp.getCompany().toLowerCase().contains(kw) ||
                opp.getLocation().toLowerCase().contains(kw) ||
                opp.getQualification().toLowerCase().contains(kw)) {
                results.add(opp);
            }
        }
        return results;
    }
    
    // Filter by type (Bursary or Internship)
    public ArrayList<Opportunity> filterByType(ArrayList<Opportunity> list, String type)
    {
        ArrayList<Opportunity> results = new ArrayList<>();
        for (Opportunity opp : list) {
            if (opp.getType().equalsIgnoreCase(type)) {
                results.add(opp);
            }
        }
        return results;
    }
    
    
    // Add a new opportunity
    public boolean addOpportunity(Opportunity opp)
    {
        if (opp == null) return false;
        return opportunities.add(opp);
    }
    
    // Get all opportunities posted by a specific company
    public ArrayList<Opportunity> getOpportunitiesByCompany(String companyUsername)
    {
        Company company = (Company) AuthService.getUser(companyUsername);
        String companyName = (company != null && company.getName() != null) ? company.getName() : companyUsername;
        
        ArrayList<Opportunity> result = new ArrayList<>();
        for (Opportunity opp : opportunities) {
            if (opp.getCompany().equals(companyName)) {
                result.add(opp);
            }
        }
        return result;
    }
    
    // Get all opportunities (for admin/stats)
    public ArrayList<Opportunity> getAllOpportunities()
    {
        return opportunities;
    }
    
    // Delete an opportunity
    public boolean deleteOpportunity(Opportunity opp)
    {
        if (opp == null) return false;
        return opportunities.remove(opp);
    }
    
    // Delete opportunity by title
    public boolean deleteOpportunityByTitle(String title)
    {
        for (int i = 0; i < opportunities.size(); i++) {
            if (opportunities.get(i).getTitle().equals(title)) {
                opportunities.remove(i);
                return true;
            }
        }
        return false;
    }
    
    // Get count of opportunities by type
    public int getCountByType(String type)
    {
        int count = 0;
        for (Opportunity opp : opportunities) {
            if (opp.getType().equalsIgnoreCase(type)) {
                count++;
            }
        }
        return count;
    }
}