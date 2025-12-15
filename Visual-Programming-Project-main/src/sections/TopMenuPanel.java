package sections;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import auth.StyleUtils;

public class TopMenuPanel extends JPanel {
    public JButton homeButton, ticketsButton, eventMgmtButton, profileButton, notificationsButton, logoutButton;

    public TopMenuPanel(ActionListener listener) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 70)); // Height of top navbar
        setBackground(StyleUtils.PRIMARY_COLOR);
        setBorder(new EmptyBorder(10, 20, 10, 20));

        // Left Section - Brand/Logo
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(StyleUtils.PRIMARY_COLOR);

        JLabel brand = new JLabel("EVENT APP");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(0, 10, 0, 30));
        leftPanel.add(brand);

        // Center Section - Navigation Buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        centerPanel.setBackground(StyleUtils.PRIMARY_COLOR);

        homeButton = createNavButton("Home", listener);
        ticketsButton = createNavButton("Tickets", listener);
        eventMgmtButton = createNavButton("Manage Events", listener);
        profileButton = createNavButton("Profile", listener);
        notificationsButton = createNavButton("Notifications", listener);

        centerPanel.add(homeButton);
        centerPanel.add(ticketsButton);
        centerPanel.add(eventMgmtButton);
        centerPanel.add(profileButton);
        centerPanel.add(notificationsButton);

        // Right Section - Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(StyleUtils.PRIMARY_COLOR);

        logoutButton = createLogoutButton(listener);
        rightPanel.add(logoutButton);

        // Add panels to navbar
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JButton createNavButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(45, 55, 72)); // Slightly lighter than primary
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(StyleUtils.ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(45, 55, 72));
            }
        });

        btn.addActionListener(listener);
        return btn;
    }

    private JButton createLogoutButton(ActionListener listener) {
        JButton btn = new JButton("Logout");
        btn.setBackground(new Color(220, 53, 69)); // Red color for logout
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(200, 35, 51)); // Darker red on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 53, 69));
            }
        });

        btn.addActionListener(listener);
        return btn;
    }
}
