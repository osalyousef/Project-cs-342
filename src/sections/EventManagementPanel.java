package sections;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import auth.Session;
import services.EventService;
import java.sql.Timestamp;
import java.util.List;

public class EventManagementPanel extends JPanel {
    public JLabel titleLabel;
    public JButton createEventButton;
    public JPanel eventListPanel;

    public EventManagementPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 600);
        setBackground(Color.WHITE);

        titleLabel = new JLabel("Organizer Dashboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(0, 0, 800, 80);
        add(titleLabel);

        createEventButton = new JButton("Create New Event");
        createEventButton.setBounds(20, 100, 180, 35);
        createEventButton.setBackground(new Color(67, 97, 238));
        createEventButton.setForeground(Color.WHITE);
        createEventButton.addActionListener(e -> openCreateEventDialog());
        add(createEventButton);

        if (!"organizer".equals(Session.getRole()) && !"admin".equals(Session.getRole())) {
            createEventButton.setVisible(false);
        }

        eventListPanel = new JPanel();
        eventListPanel.setLayout(new BoxLayout(eventListPanel, BoxLayout.Y_AXIS));
        eventListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(eventListPanel);
        scrollPane.setBounds(20, 150, 740, 400);
        add(scrollPane);

        loadCreatedEvents();
    }

    // create event dialog
    private void openCreateEventDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JTextField eventNameField = new JTextField();
        JTextField locationField = new JTextField();
        JTextField totalSeatsField = new JTextField();
        
        String[] categories = EventService.getAllCategories();
        JComboBox<String> categoryBox = new JComboBox<>(categories);

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(timeEditor);

        panel.add(new JLabel("Event Name:")); panel.add(eventNameField);
        panel.add(new JLabel("Category:")); panel.add(categoryBox);
        panel.add(new JLabel("Location:")); panel.add(locationField);
        panel.add(new JLabel("Date/Time:")); panel.add(dateSpinner);
        panel.add(new JLabel("Total Seats:")); panel.add(totalSeatsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Event", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = eventNameField.getText();
            String location = locationField.getText();
            String category = (String) categoryBox.getSelectedItem();
            Date date = (Date) dateSpinner.getValue();
            
            if (name.isEmpty() || totalSeatsField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fields cannot be empty"); return;
            }
            try {
                int seats = Integer.parseInt(totalSeatsField.getText());
                if (EventService.createEvent(name, location, new Timestamp(date.getTime()), seats, category)) {
                    JOptionPane.showMessageDialog(this, "Event Created!");
                    loadCreatedEvents();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed");
                }
            } catch (NumberFormatException e) {
                 JOptionPane.showMessageDialog(this, "Seats must be a number");
            }
        }
    }

    // edit event dialog (added)
    private void openEditEventDialog(EventService.CreatedEvent event) {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JTextField eventNameField = new JTextField(event.getName());
        JTextField locationField = new JTextField(event.getLocation());
        JTextField totalSeatsField = new JTextField(String.valueOf(event.getTotalSeats()));
        
        String[] categories = EventService.getAllCategories();
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        // select current category
        if (event.getCategory() != null) {
            categoryBox.setSelectedItem(event.getCategory());
        }

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm:ss");
        dateSpinner.setEditor(timeEditor);
        
        // set current date
        try {
            // simple conversion assuming standard format stored
            Timestamp ts = Timestamp.valueOf(event.getDate()); 
            dateSpinner.setValue(ts);
        } catch (Exception e) {
            // fallback if format issue
            dateSpinner.setValue(new Date()); 
        }

        panel.add(new JLabel("Event Name:")); panel.add(eventNameField);
        panel.add(new JLabel("Category:")); panel.add(categoryBox);
        panel.add(new JLabel("Location:")); panel.add(locationField);
        panel.add(new JLabel("Date/Time:")); panel.add(dateSpinner);
        panel.add(new JLabel("Total Seats:")); panel.add(totalSeatsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Event", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = eventNameField.getText();
            String location = locationField.getText();
            String category = (String) categoryBox.getSelectedItem();
            Date date = (Date) dateSpinner.getValue();
            
            try {
                int seats = Integer.parseInt(totalSeatsField.getText());
                if (EventService.updateEvent(event.getEventId(), name, location, new Timestamp(date.getTime()), seats, category)) {
                    JOptionPane.showMessageDialog(this, "Event Updated!");
                    loadCreatedEvents();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update");
                }
            } catch (Exception e) {
                 JOptionPane.showMessageDialog(this, "Invalid input");
            }
        }
    }

    // load created events 
     public void loadCreatedEvents() {
        eventListPanel.removeAll();
        List<EventService.CreatedEvent> createdEvents = EventService.getCreatedEvents(Session.getUserId());
        if (createdEvents.isEmpty()) {
            eventListPanel.add(new JLabel("  No events created yet."));
        } else {
            for (EventService.CreatedEvent event : createdEvents) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                card.setMaximumSize(new Dimension(700, 60));
                
                JPanel textP = new JPanel(new FlowLayout(FlowLayout.LEFT));
                textP.add(new JLabel("<html><b>" + event.getName() + "</b> (" + event.getDate() + ")<br>Cat: " + event.getCategory() + "</html>"));
                
                JPanel btnP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton editBtn = new JButton("Edit");
                JButton delBtn = new JButton("Delete");
                
                editBtn.addActionListener(e -> openEditEventDialog(event));
                
                delBtn.addActionListener(e -> {
                     int confirm = JOptionPane.showConfirmDialog(null, "Delete this event?", "Confirm", JOptionPane.YES_NO_OPTION);
                     if (confirm == JOptionPane.YES_OPTION) {
                         if(EventService.deleteEvent(event.getEventId())) loadCreatedEvents();
                     }
                });
                
                btnP.add(editBtn);
                btnP.add(delBtn);
                
                card.add(textP, BorderLayout.CENTER);
                card.add(btnP, BorderLayout.EAST);
                
                eventListPanel.add(card);
                eventListPanel.add(Box.createVerticalStrut(5));
            }
        }
        eventListPanel.revalidate(); eventListPanel.repaint();
    }
}