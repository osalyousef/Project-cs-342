package auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Entry extends JFrame implements ActionListener {
    JTextArea Entry;
    JButton Admin_Button, User_Button;

    Register r = new Register();
    AdminLogin ad = new AdminLogin();

    public Entry() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Entry");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Welcome text
        Entry = new JTextArea("Welcome to our event management project\nare you signing in as?");
        Entry.setFont(new Font("Segoe UI", Font.BOLD, 20));
        Entry.setEditable(false);
        Entry.setBackground(Color.WHITE);
        Entry.setFocusable(false);
        Entry.setAlignmentX(Component.CENTER_ALIGNMENT);
        Entry.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 45, 0));
        buttonPanel.setBackground(Color.WHITE);

        User_Button = new JButton("User");
        User_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        User_Button.setPreferredSize(new Dimension(150, 45));
        User_Button.setBackground(Color.GRAY);
        User_Button.setForeground(Color.WHITE);
        User_Button.setFocusPainted(false);
        User_Button.setOpaque(true);
        User_Button.setBorderPainted(false);

        Admin_Button = new JButton("Admin");
        Admin_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Admin_Button.setPreferredSize(new Dimension(150, 45));
        Admin_Button.setBackground(Color.GRAY);
        Admin_Button.setForeground(Color.WHITE);
        Admin_Button.setFocusPainted(false);
        Admin_Button.setOpaque(true);
        Admin_Button.setBorderPainted(false);

        buttonPanel.add(User_Button);
        buttonPanel.add(Admin_Button);

        mainPanel.add(Entry);
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);

        User_Button.addActionListener(this);
        Admin_Button.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == User_Button) {
            this.setVisible(false);
            Login a = new Login();
            a.setVisible(true);
        } else {
            this.setVisible(false);
            ad.setVisible(true);
        }
    }
}