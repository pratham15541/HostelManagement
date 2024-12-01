package Hostel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Homepage extends JFrame implements ActionListener {
    JTextArea hostelInfoAddressTextArea;

    JPanel top;
    JLabel titleLabel, hostelImageLabel;
    JButton[] navbarButtons = new JButton[5]; // Updated size for "My Profile"
    JTextArea hostelInfoTextArea;
    Font font;
    Color backgroundColor;

    // Animation variables
    int textXPosition = 900;
    int textSpeed = 2;

    private String userId, userRole; // Store user ID

    // Constructor with user ID
    Homepage(String title, String userId, String userRole) {
        super(title);
        this.userId = userId; // Assign user ID
        this.userRole = userRole;
        setLayout(null);
        backgroundColor = new Color(122, 122, 122);
        this.getContentPane().setBackground(backgroundColor);

        top = new JPanel();

        // Create top section (title and navbar)
        createTopSection();

        // Create bottom section (hostel image and information)
        createBottomSection();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500, 900);
        setLocation(200, 100);

        // Start text animation
        startTextAnimation();
    }

    private void createTopSection() {
        backgroundColor = new Color(216, 232, 255);
        top.setBounds(0, 0, 1600, 150);
        top.setLayout(null);
        top.setBackground(backgroundColor);
        add(top);

        // Create title label
        titleLabel = new JLabel("Homiee Hostel");
        Font titleFont = new Font("Arial", Font.BOLD, 50);
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(600, 20, 400, 100);
        titleLabel.setForeground(new Color(0, 122, 122));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        top.add(titleLabel);

        // Create navbar buttons
        String[] buttonLabels = { "My Profile", "Staff", "About Us", "FeedBack", "Available Rooms" };
        font = new Font("Arial", Font.BOLD, 20);

        // Create regular navbar buttons
        for (int i = 0; i < 5; i++) { // Updated loop for 5 buttons
            navbarButtons[i] = new JButton(buttonLabels[i]);
            navbarButtons[i].setBounds(200 * (i + 1), 120, 160, 40);
            navbarButtons[i].setBackground(backgroundColor);
            navbarButtons[i].setFont(font);
            navbarButtons[i].setBorderPainted(false);
            navbarButtons[i].setFocusPainted(false);
            navbarButtons[i].setContentAreaFilled(false);
            navbarButtons[i].addActionListener(this);
            top.add(navbarButtons[i]);
        }

        // If the user is an admin, add an "Admin" button
        // If the userRole is not null and the user is an admin, add an "Admin" button
        if (userRole != null && userRole.equals("admin")) {
            JButton adminButton = new JButton("Admin");
            adminButton.setBounds(0, 120, 160, 40); // Place it on the far left
            adminButton.setBackground(backgroundColor);
            adminButton.setFont(font);
            adminButton.setBorderPainted(false);
            adminButton.setFocusPainted(false);
            adminButton.setContentAreaFilled(false);
            adminButton.addActionListener(this);
            top.add(adminButton);
        }

    }

    private void createBottomSection() {
        // Create hostel image
        hostelImageLabel = new JLabel(new ImageIcon("src/assets/HostelImage.png"));
        hostelImageLabel.setBounds(100, 250, 600, 400);
        hostelImageLabel.setIcon(new ImageIcon(new ImageIcon("src/assets/HostelImage.png").getImage()
                .getScaledInstance(600, 400, java.awt.Image.SCALE_SMOOTH)));
        add(hostelImageLabel);

        // Create hostel information text area
        hostelInfoTextArea = new JTextArea("This is our Front-View of Hostel.");
        hostelInfoTextArea.setEditable(false);
        hostelInfoTextArea.setBounds(900, 300, 600, 100);
        hostelInfoTextArea.setFont(new Font("Arial", Font.BOLD, 30));

        hostelInfoTextArea.setLineWrap(true);
        hostelInfoTextArea.setWrapStyleWord(true);

        add(hostelInfoTextArea);

        // Create hostel address information
        hostelInfoAddressTextArea = new JTextArea(
                "Address: Homiee hostel at Saffron Tower, near SevenSeas Mall, Fatehganj, Vadodara");
        hostelInfoAddressTextArea.setEditable(false);
        hostelInfoAddressTextArea.setBounds(900, 450, 600, 60);
        hostelInfoAddressTextArea.setFont(new Font("Arial", Font.BOLD, 25));

        hostelInfoAddressTextArea.setLineWrap(true);
        hostelInfoAddressTextArea.setWrapStyleWord(true);

        add(hostelInfoAddressTextArea);

        // Create a horizontal line below navbar
        JSeparator separator = new JSeparator();
        separator.setBounds(0, 150, 1600, 10);
        separator.setBackground(Color.BLACK);
        separator.setForeground(Color.BLACK);
        add(separator);
    }

    private void startTextAnimation() {
        String fullText = "This is our Front-View of Hostel.";
        StringBuilder displayedText = new StringBuilder();
        AtomicInteger currentCharIndex = new AtomicInteger(0);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = currentCharIndex.get();

                if (index < fullText.length()) {
                    displayedText.append(fullText.charAt(index));
                    hostelInfoTextArea.setText(displayedText.toString());
                    currentCharIndex.incrementAndGet();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Pass user ID to the next page
        if (e.getActionCommand().equals("Admin")) {
            AdminPage adminPage = new AdminPage("Admin Page");
            adminPage.setLocation(200, 100);
            adminPage.setSize(1300, 900);
            adminPage.setVisible(true);
        }
        
       else if (e.getActionCommand().equals("My Profile")) {
            MyProfile myProfilePage = new MyProfile("My Profile", userId);
            myProfilePage.setLocation(200, 100);
            myProfilePage.setSize(1500, 900);
            myProfilePage.setVisible(true);
        } else if (e.getActionCommand().equals("Staff")) {
            Staff staffPage = new Staff("Staff");
            staffPage.setLocation(200, 100);
            staffPage.setSize(1500, 900);
            staffPage.setVisible(true);
        }
        else if (e.getActionCommand().equals("About Us")) {
        AboutUs aboutUsPage = new AboutUs("About Us");
        aboutUsPage.setLocation(200, 100);
        aboutUsPage.setSize(1500, 900);
        aboutUsPage.setVisible(true);}
        else if (e.getActionCommand().equals("FeedBack")) {
            FeedBack feedBackPage = new FeedBack("FeedBack", userId);
            feedBackPage.setLocation(200, 100);
            feedBackPage.setSize(1500, 900);
            feedBackPage.setVisible(true);
        }
        else if (e.getActionCommand().equals("Available Rooms")) {
        AvailableRooms availableRoomsPage = new AvailableRooms("Available Rooms",userId);
        availableRoomsPage.setLocation(200, 100);
        availableRoomsPage.setSize(1500, 900);
        availableRoomsPage.setVisible(true);
        }
    }

}
