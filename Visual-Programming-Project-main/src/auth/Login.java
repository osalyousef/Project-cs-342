package auth;

import javax.swing.*;

import exceptions.AuthException;
// import exceptions.MessageBox;
import pages.HomePage;

import java.awt.*;
import java.awt.event.*;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import pages.HomePage;




//class for USER Login JFrame
public class Login extends JFrame implements ActionListener {
    private JTextField User_Name_Field;
    private JPasswordField Password_Field;
    private JLabel User_Name_Label, Password_Label, Tit;
    private JButton Login_Button, Create_Account_Button, Return_Button;

    public Login() {
        setTitle("Login");
        setSize(450, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        // Components Definition
        User_Name_Field = new JTextField();
        User_Name_Label = new JLabel("User Name");
        Password_Label = new JLabel("Password");
        Password_Field = new JPasswordField();
        Create_Account_Button = new JButton("Create Account");
        Login_Button = new JButton("Login");
        Tit = new JLabel("Sign In");
        Return_Button = new JButton("Go Back");

        // Component Modifications(Design)
        User_Name_Label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        User_Name_Label.setBounds(70, 120, 100, 30);
        User_Name_Field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        User_Name_Field.setBounds(70, 150, 300, 35);
        User_Name_Field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        Password_Label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Password_Label.setBounds(70, 200, 100, 30);
        Password_Field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        Password_Field.setBounds(70, 230, 300, 35);
        Password_Field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        Return_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Return_Button.setBounds(20, 450, 100, 45);
        Return_Button.setBorder(null);
        Return_Button.setBackground(Color.gray);
        Return_Button.setForeground(Color.white);

        Login_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Login_Button.setBounds(70, 320, 140, 45);
        Login_Button.setBorder(null);
        Login_Button.setBackground(Color.gray);
        Login_Button.setForeground(Color.white);

        Create_Account_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Create_Account_Button.setBounds(220, 320, 140, 45);
        Create_Account_Button.setBorder(null);
        Create_Account_Button.setBackground(Color.GRAY);
        Create_Account_Button.setForeground(Color.white);

        Tit.setFont(new Font("Segoe UI", Font.BOLD, 32));
        Tit.setBounds(160, 40, 200, 40); // x, y, width, height

        // User Login Panel
        JPanel p1 = (JPanel) this.getContentPane();
        p1.setLayout(null);
        p1.setBackground(Color.white);
        p1.add(Tit);
        p1.add(User_Name_Label);
        p1.add(User_Name_Field);
        p1.add(Password_Label);
        p1.add(Password_Field);
        p1.add(Login_Button);
        p1.add(Create_Account_Button);
        p1.add(Return_Button);
        Return_Button.addActionListener(this);
        Create_Account_Button.addActionListener(this); // dont forget to add actionlistener
        Login_Button.addActionListener(this);
    }

    // Function for the return and register(Create account) button to go back to
    // previus panel
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == Return_Button) { // return to previous JFrame panel
            this.setVisible(false);
            Entry ent = new Entry();
        } else if (e.getSource() == Create_Account_Button) { // moves user to register(Create Account) JFrame panel
            this.setVisible(false);
            Register reg = new Register();
            reg.setVisible(true);
        } else { // This is for the LOGIN button
            String User_Name;
            boolean found;
            try {
                found = false;
                User_Name = User_Name_Field.getText();
                char pwd[] = Password_Field.getPassword();
                String password = new String(pwd);
                if (User_Name.isEmpty()) {
                    throw new AuthException("Enter User Name");
                }
                if (password.isEmpty()) {
                    throw new AuthException("Enter Password");
                }
                // Database validation
                ResultSet rs = null;
                PreparedStatement stmt = null;
                try {
                    Connection conn = DBConnection.getConnection();
                    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, User_Name);
                    stmt.setString(2, password);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        found = true;
                        int userId = rs.getInt("user_id");
                        String username = rs.getString("username");
                        String email = rs.getString("email"); 
                        String pass = rs.getString("password"); 
                        String role = rs.getString("role");

                        Session.setUser(userId, username, email, pass, role);  // Save globally

                    
                        HomePage hp = new HomePage();
                        hp.setVisible(true);
                        this.setVisible(false);
                    }
                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(null, "Database error: " + sqlException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                    } catch (SQLException sqlException) {
                        JOptionPane.showMessageDialog(null, "Error closing resources: " + sqlException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                if (!found) {
                    throw new AuthException("User Name or password is incorrect");
                }
        
                // Only shows when login is valid

                    } catch (AuthException e1) {
                JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
