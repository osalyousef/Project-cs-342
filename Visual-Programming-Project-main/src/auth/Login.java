package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import exceptions.AuthException;
import pages.HomePage;
import db.DBConnection;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    private JTextField User_Name_Field;
    private JPasswordField Password_Field;
    private JButton Login_Button, Create_Account_Button, Return_Button;

    public Login() {
        setTitle("Sign In");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(StyleUtils.BG_COLOR);
        add(mainPanel);

        // Card Setup
        JPanel card = StyleUtils.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 480)); 
        card.setBorder(new EmptyBorder(40, 50, 40, 50)); 

        // Title
        JLabel title = new JLabel("Welcome Back");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields
        User_Name_Field = new JTextField();
        StyleUtils.styleTextField(User_Name_Field);
        User_Name_Field.setHorizontalAlignment(JTextField.CENTER); // Center Text Input
        
        Password_Field = new JPasswordField();
        StyleUtils.styleTextField(Password_Field);
        Password_Field.setHorizontalAlignment(JTextField.CENTER); // Center Text Input

        // Buttons
        Login_Button = new JButton("Login");
        StyleUtils.styleButton(Login_Button);
        Login_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Login_Button.setMaximumSize(new Dimension(300, 40));

        Create_Account_Button = new JButton("Create Account");
        StyleUtils.styleButton(Create_Account_Button);
        Create_Account_Button.setBackground(Color.LIGHT_GRAY);
        Create_Account_Button.setForeground(Color.BLACK);
        Create_Account_Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        Create_Account_Button.setMaximumSize(new Dimension(300, 40));

        Return_Button = new JButton("Go Back");
        Return_Button.setContentAreaFilled(false);
        Return_Button.setBorderPainted(false);
        Return_Button.setForeground(StyleUtils.TEXT_LIGHT);
        Return_Button.setFont(StyleUtils.FONT_SMALL);
        Return_Button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Layout
        card.add(title);
        card.add(Box.createVerticalStrut(40));
        card.add(createLabel("Username"));
        card.add(User_Name_Field);
        card.add(Box.createVerticalStrut(15));
        card.add(createLabel("Password"));
        card.add(Password_Field);
        card.add(Box.createVerticalStrut(30));
        card.add(Login_Button);
        card.add(Box.createVerticalStrut(10));
        card.add(Create_Account_Button);
        card.add(Box.createVerticalStrut(10));
        card.add(Return_Button);

        mainPanel.add(card);

        Return_Button.addActionListener(this);
        Create_Account_Button.addActionListener(this);
        Login_Button.addActionListener(this);
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
        } else if (e.getSource() == Create_Account_Button) { 
            this.setVisible(false);
            new Register().setVisible(true);
        } else if (e.getSource() == Login_Button) {
            try {
                String user = User_Name_Field.getText();
                String pass = new String(Password_Field.getPassword());
                
                if(user.isEmpty() || pass.isEmpty()) throw new AuthException("Fill all fields");

                Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
                stmt.setString(1, user);
                stmt.setString(2, pass);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    Session.setUser(rs.getInt("user_id"), rs.getString("username"), rs.getString("email"), rs.getString("password"), rs.getString("role"));
                    new HomePage().setVisible(true);
                    this.setVisible(false);
                } else {
                    throw new AuthException("Invalid Credentials");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }
}