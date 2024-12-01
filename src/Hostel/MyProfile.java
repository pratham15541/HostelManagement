package Hostel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;


public class MyProfile extends JFrame implements ActionListener {
    JLabel nameLabel, emailLabel, passwordLabel, mobile_numberLabel, addressLabel;
    JTextField nameField, emailField, mobile_numberField, addressField, passwordField;
    JButton saveButton;
    String userId;

    // Constructor to accept userId
    public MyProfile(String title, String userId) {
        super(title);
        this.userId = userId;

        // Set window properties
        setLayout(null);
        setSize(1500, 900);
        setLocation(200, 100);
        setTitle("My Profile");

        // Create and set labels and text fields
        createFields();

        // Create the save button
        saveButton = new JButton("Save Changes");
        saveButton.setBounds(600, 300, 200, 40);
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setBackground(new Color(0, 122, 122));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(this);
        add(saveButton);

        // Set background color
        getContentPane().setBackground(new Color(245, 245, 245));

        // Fetch and populate user data from the database
        fetchUserDataFromDatabase();
    }

    private void createFields() {
        Font labelFont = new Font("Arial", Font.PLAIN, 18);
        Font fieldFont = new Font("Arial", Font.PLAIN, 18);

        // Name field
        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(200, 50, 100, 30);
        nameLabel.setFont(labelFont);
        add(nameLabel);
        nameField = new JTextField();
        nameField.setBounds(300, 50, 500, 30);
        nameField.setFont(fieldFont);
        add(nameField);

        // Email field
        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(200, 100, 100, 30);
        emailLabel.setFont(labelFont);
        add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(300, 100, 500, 30);
        emailField.setFont(fieldFont);
        add(emailField);

        // Password field
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(200, 150, 100, 30);
        passwordLabel.setFont(labelFont);
        add(passwordLabel);
        passwordField = new JTextField();
        passwordField.setBounds(300, 150, 500, 30);
        passwordField.setFont(fieldFont);
        add(passwordField);

        // Mobile Number field
        mobile_numberLabel = new JLabel("Mobile Number:");
        mobile_numberLabel.setBounds(200, 200, 150, 30);
        mobile_numberLabel.setFont(labelFont);
        add(mobile_numberLabel);
        mobile_numberField = new JTextField();
        mobile_numberField.setBounds(350, 200, 450, 30);
        mobile_numberField.setFont(fieldFont);
        add(mobile_numberField);

        // Address field
        addressLabel = new JLabel("Address:");
        addressLabel.setBounds(200, 250, 100, 30);
        addressLabel.setFont(labelFont);
        add(addressLabel);
        addressField = new JTextField();
        addressField.setBounds(300, 250, 500, 30);
        addressField.setFont(fieldFont);
        add(addressField);
    }

    private void fetchUserDataFromDatabase() {
        // Database connection and query to fetch user data
        try (Connection conn = Conn.getConnection()) {
            String query = "SELECT name, email, password, mobile_number, address FROM users WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userId); // Set the user ID parameter

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Populate fields with fetched data
                    nameField.setText(rs.getString("name"));
                    emailField.setText(rs.getString("email"));
                    passwordField.setText(rs.getString("password"));
                    mobile_numberField.setText(rs.getString("mobile_number"));
                    addressField.setText(rs.getString("address"));
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching user data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String mobile_number = mobile_numberField.getText().trim();
            String address = addressField.getText().trim();

            // Validate inputs
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || mobile_number.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update user data in the database
            updateUserDataInDatabase(name, email, password, mobile_number, address);
        }
    }

    private void updateUserDataInDatabase(String name, String email, String password, String mobile_number, String address) {
        try (Connection conn = Conn.getConnection()) {
            // Get the current time using LocalDateTime
            LocalDateTime now = LocalDateTime.now();
            // Convert it to Timestamp (which is compatible with MySQL DATETIME)
            Timestamp updatedAt = Timestamp.valueOf(now);
    
            // Updated query to include updatedAt column with explicit value
            String updateQuery = "UPDATE users SET name = ?, email = ?, password = ?, mobile_number = ?, address = ?, updatedAt = ? WHERE Sr = ?";
    
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, mobile_number);
                stmt.setString(5, address);
                stmt.setTimestamp(6, updatedAt);  // Pass the Timestamp here
                stmt.setString(7, userId);
    
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Changes saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating data.", "Update Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating user data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  
}
