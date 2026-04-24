package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterFrame extends JFrame {

    JTextField nameField, emailField;
    JPasswordField passwordField;
    JButton registerBtn, loginBtn;

    public RegisterFrame() {

        setTitle("Register User");
        setSize(520, 500);   // Bigger height
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel title = new JLabel("Create Account");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setBounds(145, 30, 250, 35);
        add(title);

        JLabel name = new JLabel("Full Name:");
        name.setBounds(60, 110, 100, 30);
        add(name);

        nameField = new JTextField();
        nameField.setBounds(180, 110, 240, 30);
        add(nameField);

        JLabel email = new JLabel("Email:");
        email.setBounds(60, 170, 100, 30);
        add(email);

        emailField = new JTextField();
        emailField.setBounds(180, 170, 240, 30);
        add(emailField);

        JLabel pass = new JLabel("Password:");
        pass.setBounds(60, 230, 100, 30);
        add(pass);

        passwordField = new JPasswordField();
        passwordField.setBounds(180, 230, 240, 30);
        add(passwordField);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(90, 330, 140, 40);
        add(registerBtn);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(270, 330, 140, 40);
        add(loginBtn);

        registerBtn.addActionListener(e -> registerUser());

        loginBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        setVisible(true);
    }

    private void registerUser() {
        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO users(full_name,email,password) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, String.valueOf(passwordField.getPassword()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Registration Successful!");

            dispose();
            new LoginFrame();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Registration Failed!");
        }
    }
}