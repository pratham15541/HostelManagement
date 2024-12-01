package Hostel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

public class FeedBack extends JFrame {

    private JTextArea complainTextArea, feedbackTextArea;
    private JTextArea prevComplainTextArea, prevFeedbackTextArea;
    private String userId;

    public FeedBack(String title, String userId) {
        super(title);
        this.userId = userId;

        setLayout(null);
        setSize(1500, 900);
        setLocation(200, 100);

        // Background Color
        getContentPane().setBackground(new Color(122, 122, 122));

        // Title Label
        JLabel titleLabel = new JLabel("Complain and Feedback Section");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBounds(400, 50, 700, 60);
        titleLabel.setForeground(new Color(0, 122, 122));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Divider between complain and feedback sections
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setBounds(750, 150, 10, 600);
        add(separator);

        // Complain Section (left side)
        JPanel complainPanel = new JPanel();
        complainPanel.setLayout(null);
        complainPanel.setBackground(new Color(255, 102, 102));
        complainPanel.setBounds(50, 150, 700, 600);
        add(complainPanel);

        JLabel complainLabel = new JLabel("Complain:");
        complainLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        complainLabel.setBounds(50, 30, 200, 30);
        complainPanel.add(complainLabel);

        complainTextArea = new JTextArea("Please submit your complaint here.");
        complainTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
        complainTextArea.setBackground(new Color(216, 232, 255));
        complainTextArea.setBounds(50, 70, 600, 150);
        complainPanel.add(complainTextArea);

        JButton submitComplainButton = new JButton("Submit Complain");
        submitComplainButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitComplainButton.setBackground(new Color(255, 0, 0));
        submitComplainButton.setForeground(Color.WHITE);
        submitComplainButton.setBounds(200, 250, 300, 40);
        submitComplainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitComplain();
            }
        });
        complainPanel.add(submitComplainButton);

        prevComplainTextArea = new JTextArea();
        prevComplainTextArea.setEditable(false);
        prevComplainTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        prevComplainTextArea.setBackground(new Color(255, 255, 255));
        prevComplainTextArea.setBounds(50, 300, 600, 250);
        complainPanel.add(prevComplainTextArea);

        // Feedback Section (right side)
        JPanel feedbackPanel = new JPanel();
        feedbackPanel.setLayout(null);
        feedbackPanel.setBackground(new Color(102, 255, 102));
        feedbackPanel.setBounds(800, 150, 700, 600);
        add(feedbackPanel);

        JLabel feedbackLabel = new JLabel("Feedback:");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        feedbackLabel.setBounds(50, 30, 200, 30);
        feedbackPanel.add(feedbackLabel);

        feedbackTextArea = new JTextArea("Please submit your feedback here.");
        feedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 20));
        feedbackTextArea.setBackground(new Color(216, 232, 255));
        feedbackTextArea.setBounds(50, 70, 600, 150);
        feedbackPanel.add(feedbackTextArea);

        JButton submitFeedbackButton = new JButton("Submit Feedback");
        submitFeedbackButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitFeedbackButton.setBackground(new Color(0, 204, 0));
        submitFeedbackButton.setForeground(Color.WHITE);
        submitFeedbackButton.setBounds(200, 250, 300, 40);
        submitFeedbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitFeedback();
            }
        });
        feedbackPanel.add(submitFeedbackButton);

        prevFeedbackTextArea = new JTextArea();
        prevFeedbackTextArea.setEditable(false);
        prevFeedbackTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        prevFeedbackTextArea.setBackground(new Color(255, 255, 255));
        prevFeedbackTextArea.setBounds(50, 300, 600, 250);
        feedbackPanel.add(prevFeedbackTextArea);

        // Fetch and display previous complains and feedback from database
        displayPreviousComplaintsAndFeedback();
    }

    private void submitComplain() {
        String complainText = complainTextArea.getText().trim();
    
        if (complainText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a complaint", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Insert complain into the complain table
        try (Connection conn = Conn.getConnection()) {
            String query = "INSERT INTO complain (userId, complain, createdAt) VALUES (?, ?, NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userId);
                stmt.setString(2, complainText);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Complaint submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                complainTextArea.setText("");
                displayPreviousComplaintsAndFeedback();  // Refresh the displayed complaints and feedback
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error submitting complaint: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void submitFeedback() {
        String feedbackText = feedbackTextArea.getText().trim();
    
        if (feedbackText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter feedback", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Check if a complaint exists for the user
        try (Connection conn = Conn.getConnection()) {
            // Check if a complaint exists for the user
            String checkQuery = "SELECT Sr FROM complain WHERE userId = ? AND complain IS NOT NULL AND isDeleted = 0";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, userId);
                ResultSet rs = checkStmt.executeQuery();
    
                if (rs.next()) {
                    // If there is a complaint, insert the feedback into the feedback table
                    String query = "INSERT INTO feedback (userId, feedback, createdAt) VALUES (?, ?, NOW())";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, userId);
                        stmt.setString(2, feedbackText);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(this, "Feedback submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        feedbackTextArea.setText("");
                        displayPreviousComplaintsAndFeedback();  // Refresh the displayed complaints and feedback
                    }
                } else {
                    // If no complaint exists, inform the user
                    JOptionPane.showMessageDialog(this, "No complaint found for this user to submit feedback.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error submitting feedback: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void displayPreviousComplaintsAndFeedback() {
        try (Connection conn = Conn.getConnection()) {
            String complainQuery = "SELECT complain FROM complain WHERE userId = ? AND isDeleted = 0 ORDER BY createdAt DESC";
            String feedbackQuery = "SELECT feedback FROM feedback WHERE userId = ? AND isDeleted = 0 ORDER BY createdAt DESC";
    
            // Fetch and display complaints
            try (PreparedStatement complainStmt = conn.prepareStatement(complainQuery)) {
                complainStmt.setString(1, userId);
                ResultSet complainRs = complainStmt.executeQuery();
                StringBuilder complainHistory = new StringBuilder();
    
                while (complainRs.next()) {
                    String complain = complainRs.getString("complain");
                    if (complain != null && !complain.isEmpty()) {
                        complainHistory.append(complain).append("\n\n");
                    }
                }
                prevComplainTextArea.setText(complainHistory.toString());
            }
    
            // Fetch and display feedback
            try (PreparedStatement feedbackStmt = conn.prepareStatement(feedbackQuery)) {
                feedbackStmt.setString(1, userId);
                ResultSet feedbackRs = feedbackStmt.executeQuery();
                StringBuilder feedbackHistory = new StringBuilder();
    
                while (feedbackRs.next()) {
                    String feedback = feedbackRs.getString("feedback");
                    if (feedback != null && !feedback.isEmpty()) {
                        feedbackHistory.append(feedback).append("\n\n");
                    }
                }
                prevFeedbackTextArea.setText(feedbackHistory.toString());
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching previous complaints and feedback: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}    