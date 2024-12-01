package Hostel;

public class User {
    private int userId;
    private String userName;
    private String email;
    private String userRole;  // Add userRole

    public User(int userId, String userName, String email, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.userRole = userRole;  // Initialize userRole
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return userRole;
    }
}
