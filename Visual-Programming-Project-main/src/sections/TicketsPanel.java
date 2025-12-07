package sections;

import javax.swing.*;

import auth.Session;
import services.RegistrationService;

import java.awt.*;
import java.util.List;

public class TicketsPanel extends JPanel {
    public JLabel ticketsLabel, registeredLabel;
    public JPanel registeredBox;

    public TicketsPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 600);

        ticketsLabel = new JLabel("Tickets", SwingConstants.CENTER);
        ticketsLabel.setFont(new Font("Arial", Font.BOLD, 25));
        ticketsLabel.setBounds(0, 0, 800, 80);
        ticketsLabel.setOpaque(false); // No background
        ticketsLabel.setForeground(Color.GRAY); // Match the home page text color
        add(ticketsLabel);

        registeredBox = new JPanel();
        registeredBox.setLayout(new BoxLayout(registeredBox, BoxLayout.Y_AXIS));
        registeredBox.setBackground(Color.WHITE);

        JScrollPane registeredScrollPane = new JScrollPane(registeredBox);
        registeredScrollPane.setBounds(20, 180, 740, 380); // Bigger height
        registeredScrollPane.setBorder(BorderFactory.createTitledBorder("Registered Events"));
        registeredScrollPane.getVerticalScrollBar().setUnitIncrement(10);

        add(registeredScrollPane);
        loadRegisteredEvents();
    }

    public void loadRegisteredEvents() {
        registeredBox.removeAll();

        List<RegistrationService.RegisteredEvent> registeredEvents = RegistrationService
                .getRegisteredEvents(Session.getUserId());

        if (registeredEvents.isEmpty()) {
            JLabel emptyLabel = new JLabel("No events registered.");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(Color.GRAY);
            registeredBox.add(emptyLabel);
        } else {
            for (RegistrationService.RegisteredEvent event : registeredEvents) {
                JPanel eventPanel = new JPanel(new GridLayout(0, 1));
                eventPanel.setBackground(Color.LIGHT_GRAY);
                eventPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                JLabel nameLabel = new JLabel("[Event Name] " + event.getEventName());
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

                JLabel locationLabel = new JLabel("[Location] " + event.getLocation());
                locationLabel.setFont(new Font("Arial", Font.PLAIN, 13));

                JLabel dateLabel = new JLabel("[Time] " + event.getFormattedDate());
                dateLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                dateLabel.setForeground(Color.DARK_GRAY);

                eventPanel.add(nameLabel);
                eventPanel.add(locationLabel);
                eventPanel.add(dateLabel);

                registeredBox.add(Box.createVerticalStrut(10));
                registeredBox.add(eventPanel);
            }
        }

        registeredBox.revalidate();
        registeredBox.repaint();
    }

}
