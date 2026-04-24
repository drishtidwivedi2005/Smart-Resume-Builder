package ui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ATSAnalyzerFrame extends JFrame {

    JComboBox<String> resumeBox;
    JTextArea resultArea;

    JButton analyzeBtn, refreshBtn, themeBtn;

    JLabel scoreLabel, meterLabel;

    int[] ids = new int[1000];
    int count = 0;

    boolean darkMode = false;

    Color lightBg = new Color(245, 247, 250);
    Color darkBg = new Color(32, 34, 37);

    Color lightCard = Color.WHITE;
    Color darkCard = new Color(45, 47, 51);

    Color lightText = new Color(30, 30, 30);
    Color darkText = new Color(240, 240, 240);

    public ATSAnalyzerFrame() {

        setTitle("Resume Builder");
        setSize(1020, 740);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        buildUI();
        applyTheme();
        loadResumes();

        setVisible(true);
    }

    private void buildUI() {

        JLabel title = new JLabel("ATS Resume Analyzer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setBounds(250, 20, 520, 38);
        add(title);

        themeBtn = new JButton("Dark Mode");
        themeBtn.setBounds(830, 20, 150, 36);
        add(themeBtn);

        JLabel select = new JLabel("Select Resume:");
        select.setFont(new Font("Segoe UI", Font.BOLD, 16));
        select.setBounds(45, 95, 130, 30);
        add(select);

        resumeBox = new JComboBox<>();
        resumeBox.setBounds(180, 95, 460, 36);
        resumeBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(resumeBox);

        analyzeBtn = new JButton("Analyze");
        analyzeBtn.setBounds(670, 95, 130, 36);
        add(analyzeBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(825, 95, 130, 36);
        add(refreshBtn);

        scoreLabel = new JLabel("ATS Score: --");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        scoreLabel.setBounds(45, 165, 280, 35);
        add(scoreLabel);

        meterLabel = new JLabel("Status: Waiting");
        meterLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        meterLabel.setBounds(360, 168, 340, 30);
        add(meterLabel);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JScrollPane sp = new JScrollPane(resultArea);
        sp.setBounds(40, 230, 920, 430);
        add(sp);

        styleButton(analyzeBtn);
        styleButton(refreshBtn);
        styleButton(themeBtn);

        analyzeBtn.addActionListener(e -> analyzeResume());

        refreshBtn.addActionListener(e -> {
            resumeBox.removeAllItems();
            count = 0;
            loadResumes();
            resultArea.setText("");
            scoreLabel.setText("ATS Score: --");
            meterLabel.setText("Status: Refreshed");
        });

        themeBtn.addActionListener(e -> {
            darkMode = !darkMode;
            themeBtn.setText(darkMode ? "Light Mode" : "Dark Mode");
            applyTheme();
        });
    }

    private void styleButton(JButton btn) {

        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void applyTheme() {

        Color bg = darkMode ? darkBg : lightBg;
        Color card = darkMode ? darkCard : lightCard;
        Color text = darkMode ? darkText : lightText;

        getContentPane().setBackground(bg);

        for (Component c : getContentPane().getComponents()) {

            if (c instanceof JLabel) {
                c.setForeground(text);
            }

            if (c instanceof JButton) {
                c.setBackground(card);
                c.setForeground(text);
            }

            if (c instanceof JComboBox) {
                c.setBackground(card);
                c.setForeground(text);
            }
        }

        resultArea.setBackground(card);
        resultArea.setForeground(text);

        repaint();
    }

    private void loadResumes() {

        try {

            Connection con = DBConnection.getConnection();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT resume_id,title FROM resumes ORDER BY title ASC"
            );

            while (rs.next()) {

                ids[count] = rs.getInt(1);
                resumeBox.addItem(rs.getString("title"));
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analyzeResume() {

        try {

            int index = resumeBox.getSelectedIndex();

            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Please select resume first.");
                return;
            }

            int id = ids[index];

            Connection con = DBConnection.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT * FROM resumes WHERE resume_id=" + id
            );

            if (rs.next()) {

                String text =
                        safe(rs.getString("candidate_name")) + " " +
                        safe(rs.getString("email")) + " " +
                        safe(rs.getString("phone")) + " " +
                        safe(rs.getString("linkedin")) + " " +
                        safe(rs.getString("github")) + " " +
                        safe(rs.getString("skills")) + " " +
                        safe(rs.getString("education")) + " " +
                        safe(rs.getString("projects")) + " " +
                        safe(rs.getString("experience")) + " " +
                        safe(rs.getString("certifications")) + " " +
                        safe(rs.getString("objective"));

                text = text.toLowerCase();

                int score = 0;
                StringBuilder tips = new StringBuilder();

                if (!safe(rs.getString("candidate_name")).isEmpty()) score += 8;
                else tips.append("• Add candidate full name\n");

                if (!safe(rs.getString("email")).isEmpty()) score += 10;
                else tips.append("• Add professional email address\n");

                if (!safe(rs.getString("phone")).isEmpty()) score += 8;
                else tips.append("• Add phone number\n");

                if (!safe(rs.getString("linkedin")).isEmpty()) score += 8;
                else tips.append("• Add LinkedIn profile\n");

                if (!safe(rs.getString("github")).isEmpty()) score += 6;

                if (!safe(rs.getString("skills")).isEmpty()) score += 15;
                else tips.append("• Add technical skills section\n");

                if (!safe(rs.getString("projects")).isEmpty()) score += 14;
                else tips.append("• Add projects with impact/result\n");

                if (!safe(rs.getString("experience")).isEmpty()) score += 14;
                else tips.append("• Add internships / experience\n");

                if (!safe(rs.getString("education")).isEmpty()) score += 10;
                else tips.append("• Add education details\n");

                if (!safe(rs.getString("certifications")).isEmpty()) score += 5;

                if (!safe(rs.getString("objective")).isEmpty()) score += 7;
                else tips.append("• Add summary / objective section\n");

                String[] keywords = {
                        "java", "sql", "mysql", "python", "html", "css",
                        "react", "node", "spring", "api", "aws",
                        "git", "github", "jdbc", "oop", "project",
                        "internship", "analytics", "excel"
                };

                int hits = 0;

                for (String k : keywords) {
                    if (text.contains(k)) hits++;
                }

                score += Math.min(hits * 2, 15);

                if (hits < 5) {
                    tips.append("• Add more relevant keywords (Java, SQL, API, Git etc.)\n");
                }

                if (text.length() > 350) score += 10;
                else tips.append("• Resume content too short. Add details.\n");

                if (score > 100) score = 100;

                String status;

                if (score >= 85)
                    status = "Excellent";
                else if (score >= 70)
                    status = "Strong";
                else if (score >= 55)
                    status = "Average";
                else
                    status = "Needs Improvement";

                if (tips.length() == 0) {
                    tips.append("Excellent resume. No major issue found.");
                }

                scoreLabel.setText("ATS Score: " + score + "/100");
                meterLabel.setText("Status: " + status);

                resultArea.setText(
                        "=========== ATS REPORT ===========\n\n" +
                        "Resume Name : " + rs.getString("title") + "\n" +
                        "Candidate   : " + rs.getString("candidate_name") + "\n\n" +

                        "Final Score : " + score + "/100\n" +
                        "Rating      : " + status + "\n\n" +

                        "Keyword Matches : " + hits + "\n\n" +

                        "Improvement Suggestions:\n\n" +
                        tips.toString()
                );

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE resumes SET score=? WHERE resume_id=?"
                );

                ps.setInt(1, score);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Analysis Failed.");
        }
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}