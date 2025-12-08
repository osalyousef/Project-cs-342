package auth;

import javax.swing.*;
import java.awt.*;

public class Entry extends JFrame {

    public Entry() {
        setTitle("Welcome");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.BG_COLOR);
        add(mainPanel);

        // Container
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(StyleUtils.BG_COLOR);

        // Title
        JLabel title = new JLabel("Event Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(StyleUtils.PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please select your role to continue");
        subtitle.setFont(StyleUtils.FONT_REGULAR);
        subtitle.setForeground(StyleUtils.TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
        buttonsPanel.setBackground(StyleUtils.BG_COLOR);

        buttonsPanel.add(createRoleCard("User", "Sign in to browse and book events", e -> {
            this.setVisible(false);
            new Login().setVisible(true);
        }));

        buttonsPanel.add(createRoleCard("Admin", "Manage users, events, and settings", e -> {
            this.setVisible(false);
            new AdminLogin().setVisible(true);
        }));

        container.add(title);
        container.add(Box.createVerticalStrut(10));
        container.add(subtitle);
        container.add(Box.createVerticalStrut(40));
        container.add(buttonsPanel);

        mainPanel.add(container);
        
        // --- IMPORTANT: This makes the window show up ---
        setVisible(true);
    }

    private JPanel createRoleCard(String role, String desc, java.awt.event.ActionListener action) {
        JPanel card = StyleUtils.createCard();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 180));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel roleLabel = new JLabel(role, SwingConstants.CENTER);
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        roleLabel.setForeground(StyleUtils.PRIMARY_COLOR);

        JLabel descLabel = new JLabel("<html><center>" + desc + "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(StyleUtils.TEXT_LIGHT);

        JButton btn = new JButton("Select");
        StyleUtils.styleButton(btn);
        btn.addActionListener(action);

        card.add(roleLabel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);

        return card;
    }
}