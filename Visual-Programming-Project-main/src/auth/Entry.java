package auth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Class for the first JFrame which moves you to login
public class Entry extends JFrame implements ActionListener {
    JTextArea Entry;
    JButton Admin_Button, User_Button;

    Register r = new Register();
    AdminLogin ad = new AdminLogin();
    JPanel p;

    public Entry() {
        setTitle("Entry");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Components Definition
        Entry = new JTextArea("Welcome to our event managment project\n           are you signinig in as?");
        Admin_Button = new JButton("Admin");
        User_Button = new JButton("User");

        // Component Modifications(Design)
        Entry.setFont(new Font("Segoe UI", Font.BOLD, 20));
        Entry.setBounds(30, 30, 450, 100);

        User_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        User_Button.setBounds(70, 170, 150, 45);
        User_Button.setBorder(null);
        User_Button.setBackground(Color.gray);
        User_Button.setForeground(Color.white);

        Admin_Button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        Admin_Button.setBounds(265, 170, 150, 45);
        Admin_Button.setBorder(null);
        Admin_Button.setBackground(Color.GRAY);
        Admin_Button.setForeground(Color.white);

        p = (JPanel) this.getContentPane();
        p.setLayout(null);
        p.setBackground(Color.white);
        p.add(Entry);
        p.add(Admin_Button);
        p.add(User_Button);
        User_Button.addActionListener(this);
        Admin_Button.addActionListener(this);
        this.setVisible(true);
    }

    // function to move you to either User login or Admin Login using Admin and User
    // Buttons
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == User_Button) {// Moves to USER Login JFrame panel
            this.setVisible(false);
            Login a = new Login();
            a.setVisible(true);
        } else { // Moves to ADMIN Login JFrame panel
            this.setVisible(false);
            ad.setVisible(true);
        }
    }

}
