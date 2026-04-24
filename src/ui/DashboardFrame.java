package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardFrame extends JFrame {

    JLabel totalLabel, avgLabel, readyLabel, welcomeLabel;
    JButton createBtn, viewBtn, atsBtn, refreshBtn, themeBtn, logoutBtn;

    boolean darkMode = false;

    Color lightBg = new Color(245, 247, 250);
    Color darkBg = new Color(32, 34, 37);

    Color lightCard = Color.WHITE;
    Color darkCard = new Color(45, 47, 51);

    Color lightText = new Color(30, 30, 30);
    Color darkText = new Color(240, 240, 240);

    JPanel card1, card2, card3;

    public DashboardFrame(String userName) {

        setTitle("Resume Builder");
        setSize(1120, 740);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        buildUI(userName);
        applyTheme();
        loadStats();

        setVisible(true);
    }

    private void buildUI(String userName) {

        JLabel title = new JLabel("Smart Resume Builder");
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setBounds(30, 20, 420, 42);
        add(title);

        welcomeLabel = new JLabel("Welcome, " + userName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        welcomeLabel.setBounds(32, 70, 380, 30);
        add(welcomeLabel);

        themeBtn = new JButton("Dark Mode");
        themeBtn.setBounds(900, 20, 160, 38);
        add(themeBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(900, 68, 160, 38);
        add(refreshBtn);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(900, 116, 160, 38);
        add(logoutBtn);

        totalLabel = new JLabel("0");
        avgLabel = new JLabel("0%");
        readyLabel = new JLabel("0");

        card1 = createCard("Total Resumes", totalLabel);
        card1.setBounds(45, 180, 300, 160);
        add(card1);

        card2 = createCard("Average ATS Score", avgLabel);
        card2.setBounds(395, 180, 300, 160);
        add(card2);

        card3 = createCard("Interview Ready", readyLabel);
        card3.setBounds(745, 180, 300, 160);
        add(card3);

        createBtn = new JButton("Create Resume");
        createBtn.setBounds(85, 420, 260, 58);
        add(createBtn);

        viewBtn = new JButton("View Resumes");
        viewBtn.setBounds(420, 420, 260, 58);
        add(viewBtn);

        atsBtn = new JButton("ATS Analyzer");
        atsBtn.setBounds(755, 420, 260, 58);
        add(atsBtn);

        JLabel quote = new JLabel("Build. Analyze. Get Hired.");
        quote.setFont(new Font("Segoe UI", Font.BOLD, 28));
        quote.setBounds(350, 560, 420, 40);
        add(quote);

        JLabel footer = new JLabel("Developed using Java Swing + MySQL + JDBC");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        footer.setBounds(395, 665, 320, 20);
        add(footer);

        styleButton(createBtn);
        styleButton(viewBtn);
        styleButton(atsBtn);
        styleButton(refreshBtn);
        styleButton(themeBtn);
        styleButton(logoutBtn);

        createBtn.addActionListener(e -> new ResumeBuilderFrame());

        viewBtn.addActionListener(e -> new ViewResumeFrame());

        atsBtn.addActionListener(e -> new ATSAnalyzerFrame());

        refreshBtn.addActionListener(e -> loadStats());

        themeBtn.addActionListener(e -> {
            darkMode = !darkMode;
            themeBtn.setText(darkMode ? "Light Mode" : "Dark Mode");
            applyTheme();
        });

        logoutBtn.addActionListener(e -> logoutUser());
    }

    private JPanel createCard(String title, JLabel value) {

        JPanel panel = new JPanel(null);

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 20));
        t.setBounds(22, 18, 240, 30);

        value.setFont(new Font("Segoe UI", Font.BOLD, 46));
        value.setBounds(22, 72, 240, 52);

        panel.add(t);
        panel.add(value);

        return panel;
    }

    private void styleButton(JButton btn) {

        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void applyTheme() {

        Color bg = darkMode ? darkBg : lightBg;
        Color card = darkMode ? darkCard : lightCard;
        Color text = darkMode ? darkText : lightText;

        getContentPane().setBackground(bg);

        for (Component c : getContentPane().getComponents()) {

            if (c instanceof JLabel) {
                c.setForeground(text);
                c.setBackground(bg);
            }

            else if (c instanceof JButton) {
                c.setBackground(card);
                c.setForeground(text);
            }
        }

        styleCard(card1, card, text);
        styleCard(card2, card, text);
        styleCard(card3, card, text);

        repaint();
    }

    private void styleCard(JPanel panel, Color bg, Color text) {

        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createLineBorder(new Color(170, 170, 170)));

        for (Component c : panel.getComponents()) {

            if (c instanceof JLabel) {
                c.setForeground(text);
                c.setBackground(bg);
            }
        }
    }

    private void logoutUser() {

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            dispose();

            new LoginFrame();
        }
    }

    private void loadStats() {

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps1 =
                    con.prepareStatement("SELECT COUNT(*) FROM resumes");

            ResultSet rs1 = ps1.executeQuery();

            if (rs1.next()) {
                totalLabel.setText(rs1.getString(1));
            }

            PreparedStatement ps2 =
                    con.prepareStatement("SELECT IFNULL(AVG(score),0) FROM resumes");

            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                avgLabel.setText(rs2.getInt(1) + "%");
            }

            PreparedStatement ps3 =
                    con.prepareStatement("SELECT COUNT(*) FROM resumes WHERE score >= 80");

            ResultSet rs3 = ps3.executeQuery();

            if (rs3.next()) {
                readyLabel.setText(rs3.getString(1));
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to load dashboard statistics."
            );
        }
    }
}