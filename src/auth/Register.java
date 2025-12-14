package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBConnection;
import exceptions.AuthException;

public class Register extends JFrame implements ActionListener {
    private JTextField User_Name_Field, Email_Field;
    private JPasswordField Password_Field;
    private JButton Register_Button, Return_Button;

    public Register() {
        setTitle("Create Account");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.BG_COLOR);
        add(mainPanel);

        // Card Setup
        JPanel card = StyleUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 520)); 
        card.setBorder(new EmptyBorder(40, 50, 40, 50));

        // Title
        JLabel title = new JLabel("Join Us");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inputs
        User_Name_Field = new JTextField();
        StyleUtils.styleTextField(User_Name_Field);
        User_Name_Field.setHorizontalAlignment(JTextField.CENTER); // Center

        Email_Field = new JTextField();
        StyleUtils.styleTextField(Email_Field);
        Email_Field.setHorizontalAlignment(JTextField.CENTER); // Center

        Password_Field = new JPasswordField();
        StyleUtils.styleTextField(Password_Field);
        Password_Field.setHorizontalAlignment(JTextField.CENTER); // Center

        // Buttons
        Register_Button = new JButton("Register");
        StyleUtils.styleButton(Register_Button);
        Register_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Register_Button.setMaximumSize(new Dimension(300, 40));

        Return_Button = new JButton("Already have an account? Login");
        Return_Button.setContentAreaFilled(false);
        Return_Button.setBorderPainted(false);
        Return_Button.setForeground(StyleUtils.ACCENT_COLOR);
        Return_Button.setFont(StyleUtils.FONT_SMALL);
        Return_Button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Return_Button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add to Card
        card.add(title);
        card.add(Box.createVerticalStrut(30));
        
        card.add(createLabel("Username"));
        card.add(User_Name_Field);
        card.add(Box.createVerticalStrut(10));
        
        card.add(createLabel("Email"));
        card.add(Email_Field);
        card.add(Box.createVerticalStrut(10));
        
        card.add(createLabel("Password"));
        card.add(Password_Field);
        card.add(Box.createVerticalStrut(30));
        
        card.add(Register_Button);
        card.add(Box.createVerticalStrut(10));
        card.add(Return_Button);

        mainPanel.add(card);

        Register_Button.addActionListener(this);
        Return_Button.addActionListener(this);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(StyleUtils.FONT_BOLD);
        l.setForeground(StyleUtils.TEXT_LIGHT);
        // Changed to CENTER alignment
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        return l;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Return_Button) {
            this.setVisible(false);
            new Login().setVisible(true);
        } else if (e.getSource() == Register_Button) {
            try {
                String user = User_Name_Field.getText();
                String email = Email_Field.getText();
                String pass = new String(Password_Field.getPassword());

                if (user.isEmpty() || pass.isEmpty() || email.isEmpty()) throw new AuthException("Fill all fields");
                if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) throw new AuthException("Invalid Email");

                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, 'user')");
                stmt.setString(1, user);
                stmt.setString(2, email);
                stmt.setString(3, pass);
                stmt.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(this, "Account Created! Please Login.");
                this.setVisible(false);
                new Login().setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }
}