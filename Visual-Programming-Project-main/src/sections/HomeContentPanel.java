package sections;

import javax.swing.*;
import services.EventService;
import models.Event;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// import sesstion
import auth.Session;

// import service for registration
import services.EventService;

public class HomeContentPanel extends JPanel {
    public JTextField searchField;
    public JButton textSearchButton, comboSearchButton;
    public JComboBox<String> dateBox, categoryBox, locationBox;
    public JPanel eventsPanel;
    public JLabel titleLabel;

    public HomeContentPanel(String[] dates, String[] categories, String[] locations) {
        setLayout(null);
        setBackground(Color.WHITE);

        // Title
        titleLabel = new JLabel("Current Available Events", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(0, 0, 800, 80);
        add(titleLabel);

        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setBounds(10, 85, 100, 20);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(10, 250, 320, 30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
        searchField.setBackground(Color.WHITE);
        searchField.setForeground(Color.DARK_GRAY);
        add(searchField);

        // Dropdowns
        dateBox = new JComboBox<>(dates);
        dateBox.setBounds(320, 110, 100, 30);
        dateBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        dateBox.setBackground(Color.WHITE);
        dateBox.setForeground(Color.DARK_GRAY);
        dateBox.setFont(new Font("Arial", Font.PLAIN, 14));
        add(dateBox);

        categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(430, 110, 100, 30);
        categoryBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setForeground(Color.DARK_GRAY);
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));
        add(categoryBox);

        locationBox = new JComboBox<>(locations);
        locationBox.setBounds(540, 110, 100, 30);
        locationBox.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        locationBox.setBackground(Color.WHITE);
        locationBox.setForeground(Color.DARK_GRAY);
        locationBox.setFont(new Font("Arial", Font.PLAIN, 14));
        add(locationBox);


        // Events display panel
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        eventsPanel.setBounds(0, 140, 800, 400);
        eventsPanel.setBackground(Color.WHITE);
        eventsPanel.setPreferredSize(new Dimension(760, 1000));
        // add(eventsPanel);
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBounds(0, 140, 800, 400);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(scrollPane);

    }

    public void addEventCard(int eventId, String name, String location, String date) {
        JButton card = new JButton();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(200, 100));
        card.setBackground(new Color(245, 245, 245));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel nameLabel = new JLabel("Title: " + name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel locationLabel = new JLabel("Location: " + location);
        locationLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        dateLabel.setForeground(Color.DARK_GRAY);

        card.add(nameLabel);
        card.add(locationLabel);
        card.add(dateLabel);

        card.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    null,
                    "Do you want to attend \"" + name + "\"?",
                    "Register for Event",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                EventService.registerUserForEvent(Session.getUserId(), eventId, Session.getUsername()); // call the
                // method
            }
        });

        eventsPanel.add(card);
        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

    public void searchEvents(String keyword) {
        eventsPanel.removeAll();
        keyword = keyword.toLowerCase().trim();

        boolean found = false;
        for (Event ev : EventService.getAllEvents()) {
            if (ev.getName().toLowerCase().contains(keyword)) {
                addEventCard(ev.getEventId(), ev.getName(), ev.getLocation(), ev.getDate());
                found = true;
            }
        }

        if (!found) {
            JLabel noResults = new JLabel("No events found.");
            noResults.setFont(new Font("Arial", Font.BOLD, 14));
            noResults.setForeground(Color.RED);
            eventsPanel.add(noResults);
        }

        eventsPanel.revalidate();
        eventsPanel.repaint();
    }

}
