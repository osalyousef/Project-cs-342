package sections;

import javax.swing.*;

import services.NotificationService;
import models.Notification;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;
import auth.Session;

public class NotificationPanel extends JPanel {
    public JLabel titleLabel;
    private JPanel notificationListPanel;

    public NotificationPanel() {
        setLayout(null);
        setBounds(0, 0, 800, 600);
        setBackground(Color.WHITE);

        // Title
        titleLabel = new JLabel("Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(0, 0, 800, 80);
        titleLabel.setForeground(Color.DARK_GRAY);
        add(titleLabel);

        // ============ F Notification Data ============
        notificationListPanel = new JPanel();
        notificationListPanel.setLayout(new BoxLayout(notificationListPanel, BoxLayout.Y_AXIS));
        notificationListPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(notificationListPanel);
        scrollPane.setBounds(30, 100, 740, 450);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        add(scrollPane);
        loadNotifications();
    }

    // ===== Helper Method for Relative Time =====
    private String getRelativeTime(Date past) {
        long diff = System.currentTimeMillis() - past.getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutes < 1)
            return "Just now";
        if (minutes < 60)
            return minutes + "m ago";
        if (hours < 24)
            return hours + "h ago";
        return days + "d ago";
    }

    public void clearNotifications() {
        notificationListPanel.removeAll();
        notificationListPanel.revalidate();
        notificationListPanel.repaint();
    }

    public void addNotificationCard(String message, Timestamp sentTime) {
        JPanel notifCard = new JPanel();
        notifCard.setLayout(new BorderLayout(10, 5));
        notifCard.setBackground(Color.WHITE);
        notifCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        // Icon
        JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.informationIcon"));

        // Message (title and description together)
        JLabel notifTitleLabel = new JLabel(message);
        notifTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel timeLabel = new JLabel(getRelativeTime(sentTime));
        timeLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // Text Panel (title + time)
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(notifTitleLabel, BorderLayout.NORTH);
        textPanel.add(timeLabel, BorderLayout.SOUTH);

        // Card Layout
        notifCard.add(iconLabel, BorderLayout.WEST);
        notifCard.add(textPanel, BorderLayout.CENTER);

        notificationListPanel.add(Box.createVerticalStrut(10));
        notificationListPanel.add(notifCard);
    }

    public void loadNotifications() {
        clearNotifications(); // clear old ones
        for (Notification notif : NotificationService.getNotificationsForUser(Session.getUserId())) {
            addNotificationCard(notif.getMessage(), notif.getSentTime());
        }
    }

}
