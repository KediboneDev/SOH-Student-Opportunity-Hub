import java.io.*;
import java.util.ArrayList;

public class FileManager
{
    private static final String DATA_DIR = "data/";
    
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // Student methods
    public static void saveStudents(ArrayList<Student> students)
    {
        writeObjectsToFile(DATA_DIR + "students.dat", students);
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<Student> loadStudents()
    {
        return (ArrayList<Student>) readObjectsFromFile(DATA_DIR + "students.dat");
    }
    
    // Company methods
    public static void saveCompanies(ArrayList<Company> companies)
    {
        writeObjectsToFile(DATA_DIR + "companies.dat", companies);
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<Company> loadCompanies()
    {
        return (ArrayList<Company>) readObjectsFromFile(DATA_DIR + "companies.dat");
    }
    
    // Opportunity methods
    public static void saveOpportunities(ArrayList<Opportunity> opportunities)
    {
        writeObjectsToFile(DATA_DIR + "opportunities.dat", opportunities);
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<Opportunity> loadOpportunities()
    {
        return (ArrayList<Opportunity>) readObjectsFromFile(DATA_DIR + "opportunities.dat");
    }
    
    // Applications methods (using Applications class)
    public static void saveApplications(ArrayList<Applications> applications)
    {
        writeObjectsToFile(DATA_DIR + "applications.dat", applications);
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<Applications> loadApplications()
    {
        return (ArrayList<Applications>) readObjectsFromFile(DATA_DIR + "applications.dat");
    }
    
    // Notification methods
    public static void saveNotifications(ArrayList<Notification> notifications)
    {
        writeObjectsToFile(DATA_DIR + "notifications.dat", notifications);
    }
    
    @SuppressWarnings("unchecked")
    public static ArrayList<Notification> loadNotifications()
    {
        return (ArrayList<Notification>) readObjectsFromFile(DATA_DIR + "notifications.dat");
    }
    
    private static void writeObjectsToFile(String fileName, ArrayList<?> list)
    {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(list);
            System.out.println(">> Written " + list.size() + " objects to: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to " + fileName + ": " + e.getMessage());
        }
    }
    
    private static ArrayList<?> readObjectsFromFile(String fileName)
    {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (ArrayList<?>) in.readObject();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading from " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}