package models;

public class Admin {
    private Long id;
    private String username;
    private String password;

    // Constructor
    public Admin(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Example method for admin-specific functionality
    public void performAdminTask() {
        // Admin-specific logic here
        System.out.println("Admin task performed by: " + username);
    }
}