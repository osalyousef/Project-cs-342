package auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import services.EventService;
import services.AdminService;

public class AdminHomePage extends JFrame {
    private JButton HomeButton, EventsButton, UsersButton;
    private JPanel contentPanel, menuPanel;

    public AdminHomePage() {
        setTitle("Admin - Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Side menu panel
        menuPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBackground(Color.GRAY);

        HomeButton = new JButton("Home");
        EventsButton = new JButton("Events");
        UsersButton = new JButton("Users");

        configureButton(HomeButton);
        configureButton(EventsButton);
        configureButton(UsersButton);

        menuPanel.add(HomeButton);
        menuPanel.add(EventsButton);
        menuPanel.add(UsersButton);

        add(menuPanel, BorderLayout.WEST);

        // Main content panel
        contentPanel = new JPanel(new BorderLayout());
        setHomeContent();
        add(contentPanel, BorderLayout.CENTER);

        // Menu listener assignment
        MenuListener menuListener = new MenuListener();
        HomeButton.addActionListener(menuListener);
        EventsButton.addActionListener(menuListener);
        UsersButton.addActionListener(menuListener);

        setVisible(true);
    }

    private void setHomeContent() {
        contentPanel.removeAll();

        JPanel dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new GridLayout(2, 3, 20, 20)); // 2 rows x 3 columns
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        dashboardPanel.setBackground(Color.WHITE);

        int usersCount = AdminService.getUsersCount();
        int eventsCount = AdminService.getEventsCount();
        int registrationsCount = AdminService.getRegistrationsCount();
        int categoriesCount = AdminService.getCategoriesCount();
        int organizersCount = AdminService.getOrganizersCount();
        int attendeesCount = AdminService.getAttendeesCount();

        dashboardPanel.add(createStatCard("Total Users", usersCount));
        dashboardPanel.add(createStatCard("Total Events", eventsCount));
        dashboardPanel.add(createStatCard("Total Registrations", registrationsCount));
        dashboardPanel.add(createStatCard("Total Categories", categoriesCount));
        dashboardPanel.add(createStatCard("Total Organizers", organizersCount));
        dashboardPanel.add(createStatCard("Total Attendees", attendeesCount));

        contentPanel.add(dashboardPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, int count) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel countLabel = new JLabel(String.valueOf(count), SwingConstants.CENTER);
        countLabel.setFont(new Font("Arial", Font.BOLD, 36));
        countLabel.setForeground(Color.DARK_GRAY);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);

        return card;
    }

    // Set users content
    private void setUsersContent() {
        contentPanel.removeAll();
        JLabel usersLabel = new JLabel("Manage Users", SwingConstants.CENTER);
        usersLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPanel.add(usersLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Set events content using EventsPanel
    private void setEventsContent() {
        contentPanel.removeAll();
        EventsPanel eventsPanel = new EventsPanel();
        contentPanel.add(eventsPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Button style configuration
    private void configureButton(JButton button) {
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setFont(new Font("Arial", Font.BOLD, 16));
    }

    // Inner class to handle menu button clicks
    private class MenuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == HomeButton) {
                setHomeContent();
            } else if (e.getSource() == EventsButton) {
                contentPanel.removeAll();
                EventsPanel eventsPanel = new EventsPanel();
                contentPanel.add(eventsPanel, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            } else if (e.getSource() == UsersButton) {
                contentPanel.removeAll();
                // Make sure to import com.mycompany.userspanel.UsersPanel if necessary.
                UsersPanel usersPanel = new UsersPanel();
                contentPanel.add(usersPanel, BorderLayout.CENTER);
                contentPanel.revalidate();
                contentPanel.repaint();
            }
        }
    }

    public static void main(String[] args) {
        new AdminHomePage();
    }
}
