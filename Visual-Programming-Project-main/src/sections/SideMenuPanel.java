package sections;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import auth.StyleUtils;

public class SideMenuPanel extends JPanel {
    public JButton homeButton, ticketsButton, eventMgmtButton, profileButton, notificationsButton;

    public SideMenuPanel(ActionListener listener) {
        setLayout(new GridLayout(6, 1, 0, 5)); // 6 rows, 1 column, vertical gap of 5
        setPreferredSize(new Dimension(250, 0));
        setBackground(StyleUtils.PRIMARY_COLOR);
        setBorder(new EmptyBorder(20, 10, 20, 10)); // Padding around the whole panel

        // Brand Label (Already centered)
        JLabel brand = new JLabel("EVENT APP");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brand.setForeground(Color.WHITE);
        brand.setHorizontalAlignment(SwingConstants.CENTER);
        add(brand);

        // Buttons - Icons removed from text strings
        homeButton = createMenuBtn("Home", listener);
        ticketsButton = createMenuBtn("Tickets", listener);
        eventMgmtButton = createMenuBtn("Manage", listener);
        profileButton = createMenuBtn("Profile", listener);
        notificationsButton = createMenuBtn("Alerts", listener);

        add(homeButton);
        add(ticketsButton);
        add(eventMgmtButton);
        add(profileButton);
        add(notificationsButton);
    }

    private JButton createMenuBtn(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setBackground(StyleUtils.PRIMARY_COLOR);
        btn.setForeground(new Color(200, 210, 220)); // Soft white text
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(StyleUtils.FONT_BOLD);
        
        // --- CHANGES HERE ---
        // 1. Center alignment
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        // 2. Symmetrical padding for centered text (top, left, bottom, right)
        btn.setBorder(new EmptyBorder(10, 10, 10, 10));
        // --------------------
        
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        return btn;
    }
}