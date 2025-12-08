package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBConnection;

public class AdminLogin extends JFrame implements ActionListener {
    private JTextField User_Name_Field;
    private JPasswordField Password_Field;
    private JButton Login_Button, Return_Button;

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.BG_COLOR);
        add(mainPanel);

        // Matching style to User Login
        JPanel card = StyleUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 450)); 
        card.setBorder(new EmptyBorder(40, 50, 40, 50)); 

        // Title
        JLabel title = new JLabel("Admin Portal");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        User_Name_Field = new JTextField();
        StyleUtils.styleTextField(User_Name_Field);
        User_Name_Field.setHorizontalAlignment(JTextField.CENTER); // Center

        Password_Field = new JPasswordField();
        StyleUtils.styleTextField(Password_Field);
        Password_Field.setHorizontalAlignment(JTextField.CENTER); // Center

        // Buttons
        Login_Button = new JButton("Dashboard Login");
        StyleUtils.styleButton(Login_Button);
        Login_Button.setBackground(StyleUtils.PRIMARY_COLOR); 
        Login_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Login_Button.setMaximumSize(new Dimension(300, 40));

        Return_Button = new JButton("Return to Main");
        Return_Button.setContentAreaFilled(false);
        Return_Button.setBorderPainted(false);
        Return_Button.setForeground(StyleUtils.TEXT_LIGHT);
        Return_Button.setFont(StyleUtils.FONT_SMALL);
        Return_Button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assembly
        card.add(title);
        card.add(Box.createVerticalStrut(40));
        card.add(createLabel("Admin Username"));
        card.add(User_Name_Field);
        card.add(Box.createVerticalStrut(15));
        card.add(createLabel("Admin Password"));
        card.add(Password_Field);
        card.add(Box.createVerticalStrut(30));
        card.add(Login_Button);
        card.add(Box.createVerticalStrut(15));
        card.add(Return_Button);

        mainPanel.add(card);

        Login_Button.addActionListener(this);
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
            new Entry().setVisible(true);
        } else if (e.getSource() == Login_Button) {
            String adminUser = User_Name_Field.getText().trim();
            String adminPass = new String(Password_Field.getPassword()).trim();

            if (adminUser.isEmpty() || adminPass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, adminUser);
                stmt.setString(2, adminPass);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    this.setVisible(false);
                    new AdminHomePage().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Admin Credentials", "Access Denied", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}