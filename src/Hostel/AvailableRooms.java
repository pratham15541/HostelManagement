package Hostel;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AvailableRooms extends JFrame {
    private final String userId;
    private final Map<JButton, String> roomButtons = new HashMap<>();

    public AvailableRooms(String title, String userId) {
        super(title);
        this.userId = userId;

        setLayout(null);
        setSize(1500, 900);
        setLocation(200, 100);

        // Background Color
        getContentPane().setBackground(new Color(122, 122, 122));

        // Title Label
        JLabel titleLabel = new JLabel("Available Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBounds(550, 50, 400, 60);
        titleLabel.setForeground(new Color(0, 122, 122));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Room Panels
        JPanel groundFloorPanel = createRoomPanel("Ground Floor", 3, 3, 100, 150, "0");
        add(groundFloorPanel);

        JPanel firstFloorPanel = createRoomPanel("First Floor", 2, 3, 100, 450, "1");
        add(firstFloorPanel);

        // Load Room Data
        loadRoomData();
    }

    private JPanel createRoomPanel(String floorName, int rows, int cols, int x, int y, String floorPrefix) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(rows, cols, 10, 10));
        panel.setBounds(x, y, 1200, rows * 100);
        panel.setBorder(BorderFactory.createTitledBorder(floorName));
        panel.setBackground(new Color(200, 200, 200));

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                String roomId = floorPrefix + String.format("%02d", row * cols + col + 1);
                JButton roomButton = new JButton(roomId);
                roomButton.setFont(new Font("Arial", Font.BOLD, 16));
                roomButton.addActionListener(e -> handleRoomBooking(roomId, roomButton));
                panel.add(roomButton);
                roomButtons.put(roomButton, roomId);
            }
        }

        return panel;
    }

    private void loadRoomData() {
        try (Connection conn = Conn.getConnection()) {
            String query = "SELECT roomId, userId, bookCondition FROM rooms WHERE isDeleted = 0";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String roomId = rs.getString("roomId");
                    String bookedBy = rs.getString("userId");
                    boolean isBooked = rs.getInt("bookCondition") == 1;

                    for (Map.Entry<JButton, String> entry : roomButtons.entrySet()) {
                        if (entry.getValue().equals(roomId)) {
                            JButton button = entry.getKey();
                            if (isBooked) {
                                if (bookedBy.equals(userId)) {
                                    button.setBackground(Color.GREEN); // Booked by the current user
                                } else {
                                    button.setBackground(Color.RED); // Booked by another user
                                }
                            } else {
                                button.setBackground(Color.WHITE); // Available
                            }
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading room data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRoomBooking(String roomId, JButton button) {
        try (Connection conn = Conn.getConnection()) {
            String checkQuery = "SELECT userId, bookCondition FROM rooms WHERE roomId = ? AND isDeleted = 0";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, roomId);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    String bookedBy = rs.getString("userId");
                    boolean isBooked = rs.getInt("bookCondition") == 1;

                    if (isBooked) {
                        if (bookedBy.equals(userId)) {
                            JOptionPane.showMessageDialog(this, "You already booked this room.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this, "Room is already booked by another user.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Book the room
                        String updateQuery = "UPDATE rooms SET userId = ?, bookCondition = 1, updatedAt = NOW() WHERE roomId = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setString(1, userId);
                            updateStmt.setString(2, roomId);
                            updateStmt.executeUpdate();
                            button.setBackground(Color.GREEN); // Mark as booked by the current user
                            JOptionPane.showMessageDialog(this, "Room booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Room not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error booking room: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AvailableRooms frame = new AvailableRooms("Available Rooms", "user123");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
