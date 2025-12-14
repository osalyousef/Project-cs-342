package sections;

import javax.swing.*;
import java.awt.*;

public class ProfilePanel extends JPanel {
    public JLabel titleLabel, usernameLabel, emailLabel, passwordLabel;

    public ProfilePanel() {
        setLayout(null);
        setBounds(0, 0, 800, 600);
        setBackground(Color.WHITE);

        titleLabel = new JLabel("Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setBounds(0, 0, 800, 80);
        titleLabel.setForeground(Color.DARK_GRAY);
        add(titleLabel);

        usernameLabel = new JLabel("Username: ");
        usernameLabel.setBounds(50, 120, 300, 30);
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(usernameLabel);

        emailLabel = new JLabel("Email: ");
        emailLabel.setBounds(50, 160, 300, 30);
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(emailLabel);

        passwordLabel = new JLabel("Password: *****");
        passwordLabel.setBounds(50, 200, 300, 30);
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(passwordLabel);
    }
}
