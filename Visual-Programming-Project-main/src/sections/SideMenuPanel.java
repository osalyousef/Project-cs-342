package sections;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SideMenuPanel extends JPanel {
    public JButton homeButton, ticketsButton, eventMgmtButton, profileButton, notificationsButton;

    public SideMenuPanel(ActionListener listener) {
        setLayout(new GridLayout(5, 1, 10, 10));
        setPreferredSize(new Dimension(200, 0));
        setBackground(Color.GRAY);

        homeButton = createButton("Home", listener);
        ticketsButton = createButton("Tickets", listener);
        eventMgmtButton = createButton("Event Management", listener);
        profileButton = createButton("Profile", listener);
        notificationsButton = createButton("Notifications", listener);
        
        

        add(homeButton);
        add(ticketsButton);
        add(eventMgmtButton);
        add(profileButton);
        add(notificationsButton);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.addActionListener(listener);
        return btn;
    }
}
