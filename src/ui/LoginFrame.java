package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton loginBtn, registerBtn;

    public LoginFrame() {

        setTitle("Resume Builder - Login");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel heading = new JLabel("Smart Resume Builder");
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setBounds(110, 20, 300, 30);
        add(heading);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(70, 90, 100, 30);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(180, 90, 200, 30);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(70, 140, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(180, 140, 200, 30);
        add(passwordField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(100, 220, 120, 35);
        add(loginBtn);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(250, 220, 120, 35);
        add(registerBtn);

        loginBtn.addActionListener(e -> loginUser());

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        setVisible(true);
    }

    private void loginUser() {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, emailField.getText());
            ps.setString(2, String.valueOf(passwordField.getPassword()));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");

                String name = rs.getString("full_name");

                dispose();
                new DashboardFrame(name);

            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Login Failed!");
        }
    }
}