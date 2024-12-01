package Hostel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class AdminPage extends JFrame {
    private JTable userTable, staffTable;
    private JButton addUserButton, addStaffButton;
    private JPanel userPanel, staffPanel;
    private int currentPageUser = 1, currentPageStaff = 1;
    private final int PAGE_SIZE = 10;

    public AdminPage(String title) {
        super(title);
        setLayout(null);
        setSize(1500, 900);
        setLocation(200, 100);

        // Background Color
        getContentPane().setBackground(new Color(122, 122, 122));

        // Title Label
        JLabel titleLabel = new JLabel("Admin Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBounds(550, 30, 400, 60);
        titleLabel.setForeground(new Color(0, 122, 122));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel);

        // Divider line
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setBounds(750, 100, 10, 700);
        add(separator);

        // User Panel
        userPanel = new JPanel(new BorderLayout());
        userPanel.setBounds(50, 100, 650, 700);
        userPanel.setBorder(BorderFactory.createTitledBorder("Users"));
        add(userPanel);

        // User Buttons
        JPanel userButtonPanel = new JPanel(new FlowLayout());
        addUserButton = new JButton("Add User");
        addUserButton.addActionListener(e -> new NewUserFrame(this));
        userButtonPanel.add(addUserButton);
        userPanel.add(userButtonPanel, BorderLayout.NORTH);

        // User Table
        userTable = new JTable();
        JScrollPane userScrollPane = new JScrollPane(userTable);
        userPanel.add(userScrollPane, BorderLayout.CENTER);

        // Staff Panel
        staffPanel = new JPanel(new BorderLayout());
        staffPanel.setBounds(800, 100, 650, 700);
        staffPanel.setBorder(BorderFactory.createTitledBorder("Staff"));
        add(staffPanel);

        // Staff Buttons
        JPanel staffButtonPanel = new JPanel(new FlowLayout());
        addStaffButton = new JButton("Add Staff");
        addStaffButton.addActionListener(e -> new NewStaffFrame(this));
        staffButtonPanel.add(addStaffButton);
        staffPanel.add(staffButtonPanel, BorderLayout.NORTH);

        // Staff Table
        staffTable = new JTable();
        JScrollPane staffScrollPane = new JScrollPane(staffTable);
        staffPanel.add(staffScrollPane, BorderLayout.CENTER);

        // Initial Load
        loadUserData();
        loadStaffData();
    }

     void loadUserData() {
        try (Connection conn = Conn.getConnection()) {
            String query = "SELECT Sr, name, email FROM users WHERE isDeleted = 0 LIMIT ?, ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, (currentPageUser - 1) * PAGE_SIZE);
                stmt.setInt(2, PAGE_SIZE);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel model = buildTableModel(rs);
                model.addColumn("Actions");
                userTable.setModel(model);

                addActionButtons(userTable, "user");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     void loadStaffData() {
        try (Connection conn = Conn.getConnection()) {
            String query = "SELECT Sr, name, designation FROM staffs WHERE isDeleted = 0 LIMIT ?, ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, (currentPageStaff - 1) * PAGE_SIZE);
                stmt.setInt(2, PAGE_SIZE);
                ResultSet rs = stmt.executeQuery();

                DefaultTableModel model = buildTableModel(rs);
                model.addColumn("Actions");
                staffTable.setModel(model);

                addActionButtons(staffTable, "staff");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading staff: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(rs.getObject(columnIndex));
            }
            data.add(row);
        }
        return new DefaultTableModel(data, columnNames);
    }

    private void addActionButtons(JTable table, String type) {
        TableColumn actionsColumn = table.getColumnModel().getColumn(table.getColumnCount() - 1);
        actionsColumn.setCellRenderer(new ButtonRenderer());
        actionsColumn.setCellEditor(new ButtonEditor(new JCheckBox(), table, type));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPage("Admin Panel").setVisible(true));
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value == null ? "Edit/Delete" : value.toString());
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private JTable table;
    private String type;

    public ButtonEditor(JCheckBox checkBox, JTable table, String type) {
        super(checkBox);
        this.table = table;
        this.type = type;
        button = new JButton();
        button.addActionListener(e -> handleAction());
    }

    private void handleAction() {
        int row = table.getSelectedRow();
        String id = table.getValueAt(row, 0) == null ? "Unknown" : table.getValueAt(row, 0).toString();

        if ("user".equals(type)) {
            new EditUserFrame((AdminPage) table.getTopLevelAncestor(), id);
        } else if ("staff".equals(type)) {
            new EditStaffFrame((AdminPage) table.getTopLevelAncestor(), id);
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        button.setText(value == null ? "Edit/Delete" : value.toString());
        return button;
    }
}
class EditUserFrame extends JFrame {
    private String userId;

    public EditUserFrame(AdminPage parent, String userId) {
        this.userId = userId;
        setTitle("Edit User");
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(7, 2, 10, 10));

        // Fetch user data from the database
        String[] userData = getUserData(userId);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(userData[0]);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(userData[1]);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(userData[2]);

        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField(userData[3]);

        JLabel mobileNumberLabel = new JLabel("Mobile Number:");
        JTextField mobileNumberField = new JTextField(userData[4]);

        JLabel addressLabel = new JLabel("Address:");
        JTextArea addressField = new JTextArea(userData[5]);
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleField.getText();
            String mobileNumber = mobileNumberField.getText();
            String address = addressField.getText();

            updateUser(userId, name, email, password, role, mobileNumber, address);
            parent.loadUserData(); // Reload user data
            dispose();
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            deleteUser(userId);
            parent.loadUserData(); // Reload user data
            dispose();
        });

        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleField);
        add(mobileNumberLabel);
        add(mobileNumberField);
        add(addressLabel);
        add(addressScrollPane);
        add(saveButton);
        add(deleteButton);

        setVisible(true);
    }

    private String[] getUserData(String userId) {
        // Retrieve user data from the database based on userId
        String[] userData = new String[6];
        try (Connection conn = Conn.getConnection()) {
            String query = "SELECT name, email, password, role, mobile_number, address FROM users WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    userData[0] = rs.getString("name");
                    userData[1] = rs.getString("email");
                    userData[2] = rs.getString("password");
                    userData[3] = rs.getString("role");
                    userData[4] = rs.getString("mobile_number");
                    userData[5] = rs.getString("address");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching user data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return userData;
    }

    private void updateUser(String userId, String name, String email, String password, String role, String mobileNumber, String address) {
        try (Connection conn = Conn.getConnection()) {
            String query = "UPDATE users SET name = ?, email = ?, password = ?, role = ?, mobile_number = ?, address = ? WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, role);
                stmt.setString(5, mobileNumber);
                stmt.setString(6, address);
                stmt.setString(7, userId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser(String userId) {
        try (Connection conn = Conn.getConnection()) {
            String query = "UPDATE users SET isDeleted = 1 WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
class EditStaffFrame extends JFrame {
    private String staffId;

    public EditStaffFrame(AdminPage parent, String staffId) {
        this.staffId = staffId;
        setTitle("Edit Staff");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Fetch staff data from the database
        String[] staffData = getStaffData(staffId);

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(staffData[0]);

        JLabel designationLabel = new JLabel("Designation:");
        JTextField designationField = new JTextField(staffData[1]);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String designation = designationField.getText();

            updateStaff(staffId, name, designation);
            parent.loadStaffData(); // Reload staff data
            dispose();
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            deleteStaff(staffId);
            parent.loadStaffData(); // Reload staff data
            dispose();
        });

        add(nameLabel);
        add(nameField);
        add(designationLabel);
        add(designationField);
        add(saveButton);
        add(deleteButton);

        setVisible(true);
    }

    private String[] getStaffData(String staffId) {
        // Retrieve staff data from the database based on staffId
        String[] staffData = new String[2];
        try (Connection conn = Conn.getConnection()) {
            String query = "SELECT name, designation FROM staffs WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, staffId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    staffData[0] = rs.getString("name");
                    staffData[1] = rs.getString("designation");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching staff data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return staffData;
    }

    private void updateStaff(String staffId, String name, String designation) {
        try (Connection conn = Conn.getConnection()) {
            String query = "UPDATE staffs SET name = ?, designation = ? WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, designation);
                stmt.setString(3, staffId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating staff: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteStaff(String staffId) {
        try (Connection conn = Conn.getConnection()) {
            String query = "UPDATE staffs SET isDeleted = 1 WHERE Sr = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, staffId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting staff: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


class NewUserFrame extends JFrame {
    public NewUserFrame(JFrame parent) {
        setTitle("Add New User");
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(7, 2, 10, 10));  // Adjusted to 7 rows

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        JTextField roleField = new JTextField();

        JLabel mobileNumberLabel = new JLabel("Mobile Number:");
        JTextField mobileNumberField = new JTextField();

        JLabel addressLabel = new JLabel("Address:");
        JTextArea addressField = new JTextArea();
        addressField.setLineWrap(true);
        addressField.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleField.getText();
            String mobileNumber = mobileNumberField.getText();
            String address = addressField.getText();
        
            try (Connection conn = Conn.getConnection()) {
                String query = "INSERT INTO users (name, email, password, role, mobile_number, address, isDeleted) VALUES (?, ?, ?, ?, ?, ?, 0)";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, password);  // Store password securely (hashing is recommended)
                    stmt.setString(4, role);
                    stmt.setString(5, mobileNumber);
                    stmt.setString(6, address);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User added successfully!");
        
                    // Call loadUserData to refresh the user table in the parent frame
                    ((AdminPage) parent).loadUserData();
                    dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        // Adding components to the frame
        add(nameLabel);
        add(nameField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleField);
        add(mobileNumberLabel);
        add(mobileNumberField);
        add(addressLabel);
        add(addressScrollPane);
        add(saveButton);
        add(cancelButton);

        setVisible(true);
    }
}


class NewStaffFrame extends JFrame {
    public NewStaffFrame(JFrame parent) {
        setTitle("Add New Staff");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Create the form fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel designationLabel = new JLabel("Designation:");
        JTextField designationField = new JTextField();

        // Create the Save button and its action
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String designation = designationField.getText();
        
            if (name.isEmpty() || designation.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try (Connection conn = Conn.getConnection()) {
                    String query = "INSERT INTO staffs (name, designation, isDeleted) VALUES (?, ?, 0)";
                    try (PreparedStatement stmt = conn.prepareStatement(query)) {
                        stmt.setString(1, name);
                        stmt.setString(2, designation);
                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Staff added successfully!");
        
                            // Call loadStaffData to refresh the staff table in the parent frame
                            ((AdminPage) parent).loadStaffData();
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to add staff.", "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error adding staff: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
                dispose();  // Close the frame after saving the data
            }
        });
        

        // Create the Cancel button and its action
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        // Add the components to the frame
        add(nameLabel);
        add(nameField);
        add(designationLabel);
        add(designationField);
        add(saveButton);
        add(cancelButton);

        setVisible(true);
    }
}
