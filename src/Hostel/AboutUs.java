package Hostel;

import javax.swing.*;
import java.awt.*;

public class AboutUs extends JFrame {
    Color c;
    JLabel l1;
    JTextArea t1, t2, t3, t4, t5;
    JLabel i1, i2, i3, i4, i5;
    Font f;
    Image new1, new2, new3, new4, new5;
    JScrollPane scrollpane;
    JPanel Main;

    AboutUs(String s) {
        super(s);
        c = new Color(227, 223, 208);
        this.getContentPane().setBackground(c);

        Main = new JPanel();
        Main.setLayout(null); 
        Main.setBackground(c);
        Main.setPreferredSize(new Dimension(1500, 2100)); 

        setupComponents();

        // Create scrollable area
        scrollpane = new JScrollPane(Main);
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollpane.getVerticalScrollBar().setPreferredSize(new Dimension(35, Integer.MAX_VALUE));

        // Set initial scroll position to top-left corner
        SwingUtilities.invokeLater(() -> {
            scrollpane.getViewport().setViewPosition(new Point(0, 0));
        });

        add(scrollpane, BorderLayout.CENTER);
    }

    private void setupComponents() {
        loadImages();

        addHeading();
        addTextAreas();
    }

    private void addTextAreas() {
        f = new Font("Arial", Font.BOLD, 25);

        t1 = createTextArea("In Our Hostel rooms, we are providing Two Persons sharing each room and they have individual beds, table, cupboards etc.", 600, 300);
        t2 = createTextArea("In Our Dining Area, we are providing Tables, Fresh Food, and there is also a central A.C.", 100, 600);
        t3 = createTextArea("Playground: We have a large playground for playing games and we also conduct events.", 600, 1000);
        t4 = createTextArea("Indoor Game: We have many indoor games in our indoor game area.", 100, 1400);
        t5 = createTextArea("GYM: We have a gym area where you can exercise.", 600, 1800);
    }

    private JTextArea createTextArea(String text, int x, int y) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setBounds(x, y, 800, 100);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(f);
        textArea.setBackground(c);
        textArea.setForeground(new Color(139, 194, 139));
        Main.add(textArea);
        return textArea;
    }

    private void addHeading() {
        ImageIcon heading = new ImageIcon("src/assets/Hostel/facilities.png");
        Image h = heading.getImage();
        Image head = h.getScaledInstance(700, 400, Image.SCALE_SMOOTH);

        f = new Font("Arial", Font.BOLD, 40);
        l1 = new JLabel(new ImageIcon(head));
        l1.setBounds(375, 30, 700, 100);
        Main.add(l1);
    }

    private void loadImages() {
        ImageIcon I1 = new ImageIcon("src/assets/room.jpeg");
        ImageIcon I2 = new ImageIcon("src/assets/dinningarea.png");
        ImageIcon I3 = new ImageIcon("src/assets/playground.jpg");
        ImageIcon I4 = new ImageIcon("src/assets/indoorgamearea.jpg");
        ImageIcon I5 = new ImageIcon("src/assets/gymarea.jpg");

        new1 = I1.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        new2 = I2.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        new3 = I3.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        new4 = I4.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        new5 = I5.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);

        i1 = new JLabel(new ImageIcon(new1));
        i2 = new JLabel(new ImageIcon(new2));
        i3 = new JLabel(new ImageIcon(new3));
        i4 = new JLabel(new ImageIcon(new4));
        i5 = new JLabel(new ImageIcon(new5));

        i1.setBounds(50, 150, 400, 300);
        i2.setBounds(1000, 500, 400, 300);
        i3.setBounds(50, 900, 400, 300);
        i4.setBounds(1000, 1300, 400, 300);
        i5.setBounds(50, 1700, 400, 300);

        Main.add(i1);
        Main.add(i2);
        Main.add(i3);
        Main.add(i4);
        Main.add(i5);
    }

}
