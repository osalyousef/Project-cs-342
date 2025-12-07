package sections;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import auth.Session;

import services.EventService;
import java.sql.Timestamp;

import java.util.List;
// import models.Event;

public class EventManagementPanel extends JPanel {
    public JLabel titleLabel, registeredLabel, yourEventsLabel;
    public JButton createEventButton;
    public JPanel registeredBox;
    public JPanel eventListPanel;

    public EventManagementPanel() {

        setLayout(null);
        setBounds(0, 0, 800, 600);
        setBackground(Color.WHITE);

        titleLabel = new JLabel("Event Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(0, 0, 800, 80);
        titleLabel.setForeground(Color.DARK_GRAY);
        add(titleLabel);

        createEventButton = new JButton("Create");
        createEventButton.setBounds(20, 100, 100, 35);
        createEventButton.setBackground(Color.GRAY);
        createEventButton.setForeground(Color.WHITE);
        createEventButton.setBorderPainted(false);
        createEventButton.setFocusPainted(false);
        createEventButton.setFont(new Font("Arial", Font.BOLD, 14));
        createEventButton.addActionListener(e -> openCreateEventDialog());
        add(createEventButton);

        if (!"organizer".equals(Session.getRole())) {
            createEventButton.setVisible(false);
        }

        eventListPanel = new JPanel();
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        eventListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(eventListPanel);
        scrollPane.setBounds(20, 180, 740, 350);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        add(scrollPane);

        loadCreatedEvents();
    }

    // this method creates an event and saves it to the database
    private void createEvent(String eventName, String location, Timestamp dateTime, int totalSeats) {
        if (EventService.createEvent(eventName, location, dateTime, totalSeats)) {
            JOptionPane.showMessageDialog(this, "Event created successfully!");
            loadCreatedEvents();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create event.");
        }
    }

    // to edit an existing event
    private void openEditEventDialog(EventService.CreatedEvent event) {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // Prefill current event data
        JTextField eventNameField = new JTextField(event.getName());
        JTextField locationField = new JTextField(event.getLocation());
        JTextField totalSeatsField = new JTextField(String.valueOf(event.getTotalSeats()));

        // Setup date spinner with current value
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(timeEditor);

        try {
            java.util.Date parsedDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event.getDate());
            dateSpinner.setValue(parsedDate);
        } catch (Exception e) {
            dateSpinner.setValue(new java.util.Date()); // fallback
        }

        panel.add(new JLabel("Event Name:"));
        panel.add(eventNameField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Date and Time:"));
        panel.add(dateSpinner);
        panel.add(new JLabel("Total Seats:"));
        panel.add(totalSeatsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Event",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newName = eventNameField.getText().trim();
                String newLocation = locationField.getText().trim();
                int newSeats = Integer.parseInt(totalSeatsField.getText().trim());
                java.util.Date newDate = (java.util.Date) dateSpinner.getValue();
                // last edit was here
                if (newName.isEmpty() || newLocation.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled.");
                    return;
                }

                boolean updated = EventService.updateEvent(
                        event.getEventId(),
                        newName,
                        newLocation,
                        new java.sql.Timestamp(newDate.getTime()),
                        newSeats);

                if (updated) {
                    JOptionPane.showMessageDialog(this, "Event updated successfully!");
                    loadCreatedEvents();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update event.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }

        }
    }

    // this method opens a dialog to create an event
    private void openCreateEventDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JTextField eventNameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField totalSeatsField = new JTextField();
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(timeEditor);

        panel.add(new JLabel("Event Name:"));
        panel.add(eventNameField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Date and Time:"));
        panel.add(dateSpinner);
        panel.add(new JLabel("Total Seats:"));
        panel.add(totalSeatsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Event",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            String eventName = eventNameField.getText();
            String location = locationField.getText();
            Date date = (Date) dateSpinner.getValue();

            if (eventName.isEmpty() || location.isEmpty() || totalSeatsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill in all fields.");
                return;
            }
            int totalSeats = Integer.parseInt(totalSeatsField.getText());

            // Save to database
            createEvent(eventName, location, new java.sql.Timestamp(date.getTime()), totalSeats);
        }
    }

    // this method loads the events created by the organizer
    public void loadCreatedEvents() {
        System.out.println("Organizer ID: " + Session.getUserId());
        eventListPanel.removeAll();

        List<EventService.CreatedEvent> createdEvents = EventService.getCreatedEvents(Session.getUserId());
        System.out.println("Created Events Found: " + createdEvents.size());

        if (createdEvents.isEmpty()) {
            JLabel emptyLabel = new JLabel("You haven't created any events.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            eventListPanel.add(emptyLabel);
        } else {
            for (EventService.CreatedEvent event : createdEvents) {
                JButton edit_Button = new JButton("Edit");
                edit_Button.setPreferredSize(new Dimension(60, 25));
                edit_Button.setFocusPainted(false);
                edit_Button.setBorderPainted(true);
                edit_Button.setContentAreaFilled(false);
                edit_Button.addActionListener(ev -> {
                    openEditEventDialog(event);
                });

                JButton delete_Button = new JButton("delete");
                delete_Button.setPreferredSize(new Dimension(60, 45));
                delete_Button.setFocusPainted(false);
                delete_Button.setBorderPainted(true);
                delete_Button.setContentAreaFilled(false);
                delete_Button.addActionListener(ev -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Are you sure you want to delete this event?",
                            "Delete Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deleted = EventService.deleteEvent(event.getEventId());
                        if (deleted) {
                            JOptionPane.showMessageDialog(null, "Event deleted successfully!");
                            loadCreatedEvents();
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete event.");
                        }
                    }
                });

                // NEW LAYOUT STARTS HERE
                JPanel eventBox = new JPanel(new BorderLayout());
                eventBox.setBackground(Color.LIGHT_GRAY);
                eventBox.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                eventBox.setPreferredSize(new Dimension(720, 80));
                eventBox.setMaximumSize(new Dimension(720, 80));
                eventBox.setMinimumSize(new Dimension(720, 80));

                // LEFT SIDE TEXT
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setBackground(Color.LIGHT_GRAY);

                JLabel nameLabel = new JLabel("[Event Name] " + event.getName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel locationLabel = new JLabel("[Location] " + event.getLocation());
                locationLabel.setFont(new Font("Arial", Font.PLAIN, 13));

                JLabel dateLabel = new JLabel("[Date] " + event.getDate());
                dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                dateLabel.setForeground(Color.DARK_GRAY);

                textPanel.add(nameLabel);
                textPanel.add(locationLabel);
                textPanel.add(dateLabel);

                // RIGHT SIDE: BUTTONS
                JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
                buttonPanel.setBackground(Color.LIGHT_GRAY);
                buttonPanel.setPreferredSize(new Dimension(100, 60));
                buttonPanel.setMaximumSize(new Dimension(100, 60));

                edit_Button.setPreferredSize(new Dimension(90, 25));
                delete_Button.setPreferredSize(new Dimension(90, 25));

                buttonPanel.add(edit_Button);
                buttonPanel.add(delete_Button);

                eventBox.add(textPanel, BorderLayout.CENTER);
                eventBox.add(buttonPanel, BorderLayout.EAST);
                // NEW LAYOUT ENDS HERE

                eventListPanel.add(Box.createVerticalStrut(10));
                eventListPanel.add(eventBox);
            }
        }

        eventListPanel.revalidate();
        eventListPanel.repaint();
    }

}
