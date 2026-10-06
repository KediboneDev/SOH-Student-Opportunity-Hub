import java.io.Serializable;

public abstract class User implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    protected String username;
    protected String passwordHash;
    protected String role;
    protected String name;
    protected String email;
    protected String securityQuestion;
    protected String securityAnswer;
    
    public User() {}
    
    public User(String username, String passwordHash, String role)
    {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getSecurityQuestion() { return securityQuestion; }
    public String getSecurityAnswer() { return securityAnswer; }
    
    // Setters
    public void setPasswordHash(String hash) { this.passwordHash = hash; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setSecurityQuestion(String question) { this.securityQuestion = question; }
    public void setSecurityAnswer(String answer) { this.securityAnswer = answer; }
}