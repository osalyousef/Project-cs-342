package sections;

import javax.swing.*;
import javax.swing.border.EmptyBorder; // FIXES ERROR
import java.awt.*;
import auth.Session;
import auth.StyleUtils;
import models.Event;
import services.EventService;

public class HomeContentPanel extends JPanel {
    // --- PUBLIC VARIABLES (FIXES HOMEPAGE ERROR) ---
    public JTextField searchField;
    public JButton textSearchButton, comboSearchButton;
    public JComboBox<String> dateBox, categoryBox, locationBox;
    public JLabel titleLabel;
    public JPanel eventsPanel;

    public HomeContentPanel(String[] dates, String[] categories, String[] locations) {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_COLOR);

        // Header Section
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, new Color(200,200,200)));

        // Title Row
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        titleRow.setBackground(Color.WHITE);
        titleLabel = new JLabel("Explore Events");
        titleLabel.setFont(StyleUtils.FONT_HEADER);
        titleLabel.setForeground(StyleUtils.PRIMARY_COLOR);
        titleRow.add(titleLabel);

        // Filter Bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterBar.setBackground(Color.WHITE);
        filterBar.setBorder(new EmptyBorder(0, 15, 15, 15)); // Uses imported EmptyBorder

        // Initialize Components
        searchField = new JTextField(15);
        StyleUtils.styleTextField(searchField);
        
        textSearchButton = new JButton("Search");
        StyleUtils.styleButton(textSearchButton);

        // Initialize ComboBoxes
        dateBox = new JComboBox<>(dates);
        categoryBox = new JComboBox<>(categories);
        locationBox = new JComboBox<>(locations);
        
        // Basic styling for boxes
        dateBox.setBackground(Color.WHITE);
        categoryBox.setBackground(Color.WHITE);
        locationBox.setBackground(Color.WHITE);
        
        comboSearchButton = new JButton("Filter"); 
        StyleUtils.styleButton(comboSearchButton);

        // Add to Filter Bar
        filterBar.add(new JLabel("Search:"));
        filterBar.add(searchField);
        filterBar.add(textSearchButton);
        filterBar.add(new JSeparator(SwingConstants.VERTICAL));
        filterBar.add(new JLabel("Date:"));
        filterBar.add(dateBox);
        filterBar.add(new JLabel("Category:"));
        filterBar.add(categoryBox);
        filterBar.add(new JLabel("Location:"));
        filterBar.add(locationBox);

        headerPanel.add(titleRow);
        headerPanel.add(filterBar);
        add(headerPanel, BorderLayout.NORTH);

        // Events Grid
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));  // Grid layout with wrapping
        eventsPanel.setBackground(StyleUtils.BG_COLOR);
        eventsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addEventCard(int eventId, String name, String location, String date) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(250, 150));
        card.setBackground(Color.LIGHT_GRAY);
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel locLabel = new JLabel("Location: " + location);
        locLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        JLabel dateLabel = new JLabel("Date: " + date);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 11));

        JButton attendBtn = new JButton("Register");
        attendBtn.setBackground(Color.GRAY);
        attendBtn.setForeground(Color.WHITE);
        attendBtn.addActionListener(e -> {
             int confirm = JOptionPane.showConfirmDialog(null, "Register for " + name + "?", "Confirm", JOptionPane.YES_NO_OPTION);
             if(confirm == JOptionPane.YES_OPTION) {
                 EventService.registerUserForEvent(Session.getUserId(), eventId, Session.getUsername());
             }
        });

        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(locLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(dateLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(attendBtn);

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
            JLabel noRes = new JLabel("No events found.");
            noRes.setFont(StyleUtils.FONT_BOLD);
            eventsPanel.add(noRes);
        }
        eventsPanel.revalidate();
        eventsPanel.repaint();
    }
}