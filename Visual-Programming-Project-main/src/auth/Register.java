package auth;

import db.DBConnection;
import java.sql.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import auth.test;
import exceptions.AuthException;
// import auth.DataBaseSim;

//Class for the Register JFrame
public class Register extends JFrame implements ActionListener {
        private JTextField User_Name_Field, Email_Field;
        private JPasswordField Password_Field;
        private JLabel User_Name_Label, Password_Label, Email_Label, Tit;
        private JButton Register_Button, Return_Button;

        public Register() {
                setTitle("Register");
                setSize(450, 550);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null); // Center on screen
                // Components Definition
                User_Name_Field = new JTextField();
                User_Name_Label = new JLabel("User Name");
                Password_Label = new JLabel("Password");
                Password_Field = new JPasswordField();
                Register_Button = new JButton("Register");
                Email_Field = new JTextField();
                Email_Label = new JLabel("Email");
                Tit = new JLabel("Create an Account");
                Return_Button = new JButton("Go Back");

                // Component Modifications(Design)
                User_Name_Label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                User_Name_Label.setBounds(70, 120, 100, 30);
                User_Name_Field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                User_Name_Field.setBounds(70, 150, 300, 35);
                User_Name_Field.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
                                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

                Return_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
                Return_Button.setBounds(20, 450, 100, 45);
                Return_Button.setBorder(null);
                Return_Button.setBackground(Color.gray);
                Return_Button.setForeground(Color.white);

                Password_Label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                Password_Label.setBounds(70, 200, 100, 30);
                Password_Field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                Password_Field.setBounds(70, 230, 300, 35);
                Password_Field.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
                                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
                // email
                Email_Label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                Email_Label.setBounds(70, 280, 100, 30);
                Email_Field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                Email_Field.setBounds(70, 310, 300, 35);
                Email_Field.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY),
                                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

                Register_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
                Register_Button.setBounds(70, 380, 300, 45);
                Register_Button.setBorder(null);
                Register_Button.setBackground(Color.GRAY);
                Register_Button.setForeground(Color.white);

                Tit.setFont(new Font("Segoe UI", Font.BOLD, 23));
                Tit.setBounds(120, 40, 200, 40); // x, y, width, height

                // Register Panel
                JPanel p1 = (JPanel) this.getContentPane();
                p1.setLayout(null);
                p1.setBackground(Color.white);
                p1.add(Tit);
                p1.add(User_Name_Label);
                p1.add(User_Name_Field);
                p1.add(Password_Label);
                p1.add(Password_Field);
                p1.add(Email_Field);
                p1.add(Email_Label);
                p1.add(Register_Button);
                p1.add(Return_Button);
                Return_Button.addActionListener(this);
                Register_Button.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
                Login log;
                if (e.getSource() == Return_Button) {// returns to previuos panel
                        this.setVisible(false);
                        log = new Login();
                        log.setVisible(true);
                }

                // this code is using real database
                if (e.getSource() == Register_Button) {
                        String User_Name, Email;
                        try {
                                Email = Email_Field.getText();
                                User_Name = User_Name_Field.getText();
                                char[] pwd = Password_Field.getPassword();
                                String password = new String(pwd);

                                if (User_Name.isEmpty())
                                        throw new AuthException("Enter User Name");
                                if (password.isEmpty())
                                        throw new AuthException("Enter Password");
                                if (Email.isEmpty())
                                        throw new AuthException("Enter Email");
                                if (!Email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                                        throw new AuthException("Enter a valid email address");
                                }

                                Connection conn = DBConnection.getConnection();
                                String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, User_Name);
                                stmt.setString(2, Email);
                                stmt.setString(3, password);
                                stmt.executeUpdate();
                                stmt.close();
                                conn.close();

                                JOptionPane.showMessageDialog(null,
                                                "Welcome: " + User_Name,
                                                "Success", JOptionPane.INFORMATION_MESSAGE);

                                this.setVisible(false);
                                new Login().setVisible(true);

                        } catch (AuthException e1) {
                                JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        } catch (SQLException e2) {
                                JOptionPane.showMessageDialog(null, "Database Error: " + e2.getMessage(), "Error",
                                                JOptionPane.ERROR_MESSAGE);
                        }
                }
        }
}
