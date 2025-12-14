package sections;

import javax.swing.*;
import auth.Session;
import services.RegistrationService;
import java.awt.*;

public class TicketsPanel extends JPanel {
    public JLabel ticketsLabel;
    public JPanel registeredBox;

    public TicketsPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 600);

        ticketsLabel = new JLabel("My Tickets", SwingConstants.CENTER);
        ticketsLabel.setFont(new Font("Arial", Font.BOLD, 25));
        ticketsLabel.setBounds(0, 0, 800, 80);
        add(ticketsLabel);

        registeredBox = new JPanel();
        registeredBox.setLayout(new BoxLayout(registeredBox, BoxLayout.Y_AXIS));
        registeredBox.setBackground(Color.WHITE);

        JScrollPane registeredScrollPane = new JScrollPane(registeredBox);
        registeredScrollPane.setBounds(20, 100, 740, 450);
        registeredScrollPane.setBorder(BorderFactory.createTitledBorder("Upcoming Events"));
        registeredScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(registeredScrollPane);

        loadRegisteredEvents();
    }

    // load events registered by user
    public void loadRegisteredEvents() {
        registeredBox.removeAll();
        java.util.List<RegistrationService.RegisteredEvent> events = RegistrationService.getRegisteredEvents(Session.getUserId());

        if (events.isEmpty()) {
            registeredBox.add(new JLabel("  No tickets found."));
        } else {
            for (RegistrationService.RegisteredEvent event : events) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(new Color(240, 240, 240));
                card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                card.setMaximumSize(new Dimension(700, 80));

                JPanel textP = new JPanel(new GridLayout(2, 1));
                textP.setOpaque(false);
                textP.add(new JLabel("Event: " + event.getEventName()));
                textP.add(new JLabel("Date: " + event.getFormattedDate() + " | Loc: " + event.getLocation()));

                JButton cancelBtn = new JButton("Cancel");
                cancelBtn.setBackground(new Color(220, 53, 69));
                cancelBtn.setForeground(Color.WHITE);
                cancelBtn.setFocusPainted(false);
                cancelBtn.addActionListener(e -> {
                    int opt = JOptionPane.showConfirmDialog(null, "Cancel this booking?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (opt == JOptionPane.YES_OPTION) {
                        if (RegistrationService.cancelRegistration(Session.getUserId(), event.getEventId())) {
                            JOptionPane.showMessageDialog(null, "Booking Cancelled.");
                            loadRegisteredEvents(); 
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to cancel.");
                        }
                    }
                });

                card.add(textP, BorderLayout.CENTER);
                card.add(cancelBtn, BorderLayout.EAST);
                
                registeredBox.add(card);
                registeredBox.add(Box.createVerticalStrut(10));
            }
        }
        registeredBox.revalidate();
        registeredBox.repaint();
    }
}