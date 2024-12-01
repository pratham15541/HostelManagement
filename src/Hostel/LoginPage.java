package Hostel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage {
    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginPage() {
        // Initialize the frame
        frame = new JFrame("User Login");
        frame.setBounds(400, 150, 500, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set background color
        frame.getContentPane().setBackground(new Color(200, 220, 240));

        // Title label
        JLabel titleLabel = new JLabel("User Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(180, 30, 200, 40);
        frame.add(titleLabel);

        // Email label and field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setBounds(100, 80, 100, 30);
        frame.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(200, 80, 200, 30);
        frame.add(emailField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setBounds(100, 120, 100, 30);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(200, 120, 200, 30);
        frame.add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBounds(200, 170, 100, 40);
        loginButton.setBackground(new Color(70, 130, 180));  // Steel blue
        loginButton.setForeground(Color.WHITE);
        frame.add(loginButton);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBounds(200, 220, 100, 40);
        registerButton.setBackground(new Color(50, 205, 50));  // Lime green
        registerButton.setForeground(Color.WHITE);
        frame.add(registerButton);

        // Add action listener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to check email and password from the database
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
        
                // Authenticate the user using the database
                User user = authenticateUser(email, password);
                if (user != null) {
                    // Successful login, redirect to HomePage with user info
                    frame.dispose();  // Dispose of the login frame
                   // After successful login and before creating Homepage instance
String role = (user.getRole() != null) ? user.getRole() : "user";

// Debugging: Print the role before passing it to Homepage
// System.out.println("Role passed to Homepage: " + role);

Homepage homepage = new Homepage("Homiee Hostel", String.valueOf(user.getUserId()), role);
homepage.setLocation(200, 100);
homepage.setSize(1400, 900);
homepage.setVisible(true);  // Make Homepage visible

                } else {
                    // Show error message
                    JOptionPane.showMessageDialog(frame, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add action listener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to the RegisterPage
                frame.setVisible(false);
                new RegisterPage();
            }
        });

        // Show the login frame
        frame.setVisible(true);
    }

    // Method to authenticate user from the database using Conn.java
    private User authenticateUser(String email, String password) {
        try (Connection conn = Conn.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(frame, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
    
            // Prepare SQL query to select user from the database
            String query = "SELECT Sr, name, email, role FROM users WHERE email = ? AND password = ?";
    
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);
    
            // Execute query
            ResultSet rs = pst.executeQuery();
    
            // If a record is found, create a User object and return it
            if (rs.next()) {
                int userId = rs.getInt("Sr");
                String userName = rs.getString("name");
                String userEmail = rs.getString("email");
                String userRole = rs.getString("role");
                
                // Debugging: Print the role fetched from the database
                // System.out.println("Fetched role from database: " + userRole);
    
                if (userRole == null) {
                    userRole = "user";  // Set a default value if role is null
                }
    
                // Debugging: Print the final role before returning the user
                // System.out.println("Role assigned to user: " + userRole);
    
                return new User(userId, userName, userEmail, userRole);  // Return user info
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if authentication fails
    }
    
}
