package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import services.AdminService;

public class AdminHomePage extends JFrame {
    private JButton HomeButton, EventsButton, UsersButton, ReportsButton; 
    private JPanel contentPanel, menuPanel;

    public AdminHomePage() {
        setTitle("Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // sidebar panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(StyleUtils.PRIMARY_COLOR);
        menuPanel.setPreferredSize(new Dimension(250, 0));
        
        JLabel brand = new JLabel("ADMIN PANEL");
        brand.setFont(StyleUtils.FONT_HEADER);
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(30, 30, 30, 30));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        HomeButton = createSidebarBtn("Dashboard");
        EventsButton = createSidebarBtn("Manage Events");
        UsersButton = createSidebarBtn("Manage Users");
        ReportsButton = createSidebarBtn("Analytics & Reports"); 

        menuPanel.add(brand);
        menuPanel.add(HomeButton);
        menuPanel.add(EventsButton);
        menuPanel.add(UsersButton);
        menuPanel.add(ReportsButton);
        add(menuPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(StyleUtils.BG_COLOR);
        setHomeContent();
        add(contentPanel, BorderLayout.CENTER);

        MenuListener ml = new MenuListener();
        HomeButton.addActionListener(ml);
        EventsButton.addActionListener(ml);
        UsersButton.addActionListener(ml);
        ReportsButton.addActionListener(ml); 

        setVisible(true);
    }

    private JButton createSidebarBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(StyleUtils.PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFont(StyleUtils.FONT_REGULAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setBorder(new EmptyBorder(15, 30, 15, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        contentPanel.revalidate(); contentPanel.repaint();
    }

    private JPanel createStatCard(String title, int count, Color stripColor) {
        JPanel card = StyleUtils.createCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 140));
        JPanel strip = new JPanel(); strip.setBackground(stripColor); strip.setPreferredSize(new Dimension(250, 6));
        JLabel countLbl = new JLabel(String.valueOf(count)); countLbl.setFont(new Font("Segoe UI", Font.BOLD, 48)); countLbl.setForeground(StyleUtils.TEXT_DARK);
        JLabel titleLbl = new JLabel(title); titleLbl.setFont(StyleUtils.FONT_REGULAR); titleLbl.setForeground(StyleUtils.TEXT_LIGHT);
        card.add(strip, BorderLayout.NORTH);
        JPanel inner = new JPanel(new GridLayout(2, 1)); inner.setBackground(Color.WHITE); inner.add(titleLbl); inner.add(countLbl); inner.setBorder(new EmptyBorder(10, 20, 10, 20));
        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    private class MenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            contentPanel.removeAll();
            if (e.getSource() == HomeButton) setHomeContent();
            else if (e.getSource() == EventsButton) contentPanel.add(new EventsPanel(), BorderLayout.CENTER);
            else if (e.getSource() == UsersButton) contentPanel.add(new UsersPanel(), BorderLayout.CENTER);
            else if (e.getSource() == ReportsButton) contentPanel.add(new ReportsPanel(), BorderLayout.CENTER); 
            contentPanel.revalidate(); contentPanel.repaint();
        }
    }
}