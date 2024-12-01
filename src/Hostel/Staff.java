package Hostel;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Staff extends JFrame {

    private int currentPage = 1;
    private int totalRecords = 0;
    private int totalPages = 0;
    private DefaultTableModel model;

    public Staff(String title) {
        super(title);
        setLayout(null);
        setSize(1500, 900);
        setLocation(200, 100);

        // Background Color
        getContentPane().setBackground(new Color(255, 255, 255));

        // Title Label
        JLabel titleLabel = new JLabel("Staff Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBounds(450, 50, 700, 60);
        titleLabel.setForeground(new Color(0, 122, 122));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Table to display staff information
        String[] columns = {"Name", "Designation"};
        model = new DefaultTableModel(columns, 0);

        // Fetch the total record count and populate table
        fetchStaffData();

        JTable staffTable = new JTable(model);
        staffTable.setFont(new Font("Arial", Font.PLAIN, 18));
        staffTable.setRowHeight(30);
        staffTable.setBackground(new Color(216, 232, 255));
        JScrollPane scrollPane = new JScrollPane(staffTable);
        scrollPane.setBounds(150, 150, 1200, 200);
        add(scrollPane);

        // Previous button
        JButton prevButton = new JButton("Previous");
        prevButton.setBounds(600, 600, 150, 40);
        prevButton.setFont(new Font("Arial", Font.BOLD, 18));
        prevButton.setBackground(new Color(70, 130, 180));  // Steel blue
        prevButton.setForeground(Color.WHITE);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    fetchStaffData();
                }
            }
        });
        add(prevButton);

        // Next button
        JButton nextButton = new JButton("Next");
        nextButton.setBounds(800, 600, 150, 40);
        nextButton.setFont(new Font("Arial", Font.BOLD, 18));
        nextButton.setBackground(new Color(70, 130, 180));  // Steel blue
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage < totalPages) {
                    currentPage++;
                    fetchStaffData();
                }
            }
        });
        add(nextButton);
    }

    // Method to fetch staff data from database with pagination
    private void fetchStaffData() {
        try (Connection conn = Conn.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get total number of records
            String countQuery = "SELECT COUNT(*) FROM staffs";
            PreparedStatement countStmt = conn.prepareStatement(countQuery);
            ResultSet countRs = countStmt.executeQuery();
            if (countRs.next()) {
                totalRecords = countRs.getInt(1);
                totalPages = (int) Math.ceil((double) totalRecords / 5);
            }

            // Clear previous data from the table model
            model.setRowCount(0);

            // Prepare the query for pagination (fetch 5 records based on currentPage)
            String query = "SELECT name, designation FROM staffs LIMIT ?, 5";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, (currentPage - 1) * 5);
            ResultSet rs = pst.executeQuery();

            // Loop through the result set and add staff info to the table model
            while (rs.next()) {
                String name = rs.getString("name");
                String designation = rs.getString("designation");
                model.addRow(new Object[]{name, designation});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching staff data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
