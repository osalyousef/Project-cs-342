package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import services.AdminService;

public class AdminHomePage extends JFrame {
    private JButton HomeButton, EventsButton, UsersButton, LogoutButton;
    private JPanel contentPanel, menuPanel;

    public AdminHomePage() {
        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Navigation Bar
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(StyleUtils.PRIMARY_COLOR);
        menuPanel.setPreferredSize(new Dimension(0, 70));
        menuPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Left Section - Brand
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(StyleUtils.PRIMARY_COLOR);

        JLabel brand = new JLabel("ADMIN PANEL");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(0, 10, 0, 30));
        leftPanel.add(brand);

        // Center Section - Navigation Buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        centerPanel.setBackground(StyleUtils.PRIMARY_COLOR);

        HomeButton = createNavButton("Dashboard");
        EventsButton = createNavButton("Manage Events");
        UsersButton = createNavButton("Manage Users");

        centerPanel.add(HomeButton);
        centerPanel.add(EventsButton);
        centerPanel.add(UsersButton);

        // Right Panel - Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(StyleUtils.PRIMARY_COLOR);

        LogoutButton = createLogoutButton();
        rightPanel.add(LogoutButton);

        menuPanel.add(leftPanel, BorderLayout.WEST);
        menuPanel.add(centerPanel, BorderLayout.CENTER);
        menuPanel.add(rightPanel, BorderLayout.EAST);

        add(menuPanel, BorderLayout.NORTH);

        // Content
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleUtils.BG_COLOR);
        setHomeContent();
        add(contentPanel, BorderLayout.CENTER);

        // Listener logic
        MenuListener ml = new MenuListener();
        HomeButton.addActionListener(ml);
        EventsButton.addActionListener(ml);
        UsersButton.addActionListener(ml);
        LogoutButton.addActionListener(ml);

        setVisible(true);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(45, 55, 72)); // Slightly lighter than primary
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
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

        return btn;
    }

    private JButton createLogoutButton() {
        JButton btn = new JButton("Logout");
        btn.setBackground(new Color(220, 53, 69)); // Red color for logout
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
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

        return btn;
    }

    private void setHomeContent() {
        contentPanel.removeAll();
        JPanel dash = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        dash.setBackground(StyleUtils.BG_COLOR);
        dash.setBorder(new EmptyBorder(30, 30, 30, 30));

        dash.add(createStatCard("Total Users", AdminService.getUsersCount(), new Color(59, 130, 246)));
        dash.add(createStatCard("Total Events", AdminService.getEventsCount(), new Color(16, 185, 129)));
        dash.add(createStatCard("Registrations", AdminService.getRegistrationsCount(), new Color(245, 158, 11)));
        
        contentPanel.add(dash, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, int count, Color stripColor) {
        JPanel card = StyleUtils.createCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 140));
        
        JPanel strip = new JPanel();
        strip.setBackground(stripColor);
        strip.setPreferredSize(new Dimension(250, 6));
        
        JLabel countLbl = new JLabel(String.valueOf(count));
        countLbl.setFont(new Font("Segoe UI", Font.BOLD, 48));
        countLbl.setForeground(StyleUtils.TEXT_DARK);
        
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(StyleUtils.FONT_REGULAR);
        titleLbl.setForeground(StyleUtils.TEXT_LIGHT);

        card.add(strip, BorderLayout.NORTH);
        JPanel inner = new JPanel(new GridLayout(2, 1));
        inner.setBackground(Color.WHITE);
        inner.add(titleLbl);
        inner.add(countLbl);
        inner.setBorder(new EmptyBorder(10, 20, 10, 20));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    // Keep your inner MenuListener class logic
    private class MenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == HomeButton) setHomeContent();
            else if (e.getSource() == EventsButton) {
                contentPanel.removeAll();
                contentPanel.add(new EventsPanel(), BorderLayout.CENTER);
                contentPanel.revalidate(); contentPanel.repaint();
            } else if (e.getSource() == UsersButton) {
                contentPanel.removeAll();
                contentPanel.add(new UsersPanel(), BorderLayout.CENTER);
                contentPanel.revalidate(); contentPanel.repaint();
            } else if (e.getSource() == LogoutButton) {
                int confirm = JOptionPane.showConfirmDialog(AdminHomePage.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Session.clearSession();
                    dispose();
                    new AdminLogin();
                }
            }
        }
    }
}