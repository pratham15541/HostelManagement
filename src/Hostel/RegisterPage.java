package Hostel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterPage {
    private JFrame frame;
    private JTextField nameField, emailField, addressField, mobileField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterPage() {
        // Initialize the frame
        frame = new JFrame("User Registration");
        frame.setBounds(400, 150, 500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set background color
        frame.getContentPane().setBackground(new Color(200, 220, 240));

        // Title label
        JLabel titleLabel = new JLabel("User Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(160, 30, 250, 40);
        frame.add(titleLabel);

        // Name label and field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nameLabel.setBounds(100, 80, 100, 30);
        frame.add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBounds(200, 80, 200, 30);
        frame.add(nameField);

        // Email label and field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setBounds(100, 120, 100, 30);
        frame.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(200, 120, 200, 30);
        frame.add(emailField);

        // Address label and field
        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        addressLabel.setBounds(100, 160, 100, 30);
        frame.add(addressLabel);

        addressField = new JTextField();
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        addressField.setBounds(200, 160, 200, 30);
        frame.add(addressField);

        // Mobile label and field
        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        mobileLabel.setBounds(100, 200, 100, 30);
        frame.add(mobileLabel);

        mobileField = new JTextField();
        mobileField.setFont(new Font("Arial", Font.PLAIN, 14));
        mobileField.setBounds(200, 200, 200, 30);
        frame.add(mobileField);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setBounds(100, 240, 100, 30);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(200, 240, 200, 30);
        frame.add(passwordField);

        // Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBounds(200, 300, 100, 40);
        registerButton.setBackground(new Color(70, 130, 180));  // Steel blue
        registerButton.setForeground(Color.WHITE);
        frame.add(registerButton);

        // Add action listener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get user details
                String name = nameField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                String mobile = mobileField.getText();
                String password = new String(passwordField.getPassword());

                // Check if email already exists
                if (isEmailExists(email)) {
                    JOptionPane.showMessageDialog(frame, "Email is already registered. Please use a different email.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Register the user
                    registerUser(name, email, address, mobile, password);
                }
            }
        });

        // Show the register frame
        frame.setVisible(true);
    }

    // Check if email already exists in the database
    private boolean isEmailExists(String email) {
        try (Connection conn = Conn.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(frame, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Prepare SQL query to check if email exists
            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);

            // Execute query
            ResultSet rs = pst.executeQuery();

            // If a record is found, email already exists
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register the user in the database
    private void registerUser(String name, String email, String address, String mobile, String password) {
        try (Connection conn = Conn.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(frame, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prepare SQL query to insert user details
            String query = "INSERT INTO users (name, email, address, mobile_number, password,role) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, address);
            pst.setString(4, mobile);
            pst.setString(5, password);
            pst.setString(6, "user");

            // Execute update
            int rowsAffected = pst.executeUpdate();

            // Show success or failure message
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(frame, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.setVisible(false);
                new LoginPage(); // Redirect to login page
            } else {
                JOptionPane.showMessageDialog(frame, "Registration Failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
