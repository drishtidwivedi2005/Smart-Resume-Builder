package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ResumeBuilderFrame extends JFrame {

    JTextField resumeName, fullName, email, phone, linkedin, github;
    JTextArea summary, skills, education, projects, experience, certifications;

    JButton saveBtn, previewBtn, exportDocBtn, exportPdfBtn, themeBtn;

    JPanel mainPanel;
    JScrollPane scrollPane;

    boolean darkMode = false;

    Color lightBg = new Color(245, 247, 250);
    Color darkBg = new Color(32, 34, 37);

    Color lightCard = Color.WHITE;
    Color darkCard = new Color(45, 47, 51);

    Color lightText = new Color(30, 30, 30);
    Color darkText = new Color(240, 240, 240);

    public ResumeBuilderFrame() {

        setTitle("Resume Builder");
        setSize(1100, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        buildTopBar();
        buildMainForm();

        applyTheme();

        setVisible(true);
    }

    private void buildTopBar() {

        JPanel topBar = new JPanel(null);
        topBar.setPreferredSize(new Dimension(1100, 65));

        JLabel title = new JLabel("Create ATS Friendly Resume");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setBounds(30, 15, 500, 35);
        topBar.add(title);

        themeBtn = new JButton("Dark Mode");
        themeBtn.setBounds(920, 15, 130, 35);
        topBar.add(themeBtn);

        themeBtn.addActionListener(e -> {
            darkMode = !darkMode;
            themeBtn.setText(darkMode ? "Light Mode" : "Dark Mode");
            applyTheme();
        });

        add(topBar, BorderLayout.NORTH);
    }

    private void buildMainForm() {

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1050, 1300));

        int leftX = 40;
        int rightX = 550;

        int y = 30;

        resumeName = addField("Resume Name", leftX, y);
        fullName = addField("Full Name", rightX, y);
        y += 70;

        email = addField("Email", leftX, y);
        phone = addField("Phone", rightX, y);
        y += 70;

        linkedin = addField("LinkedIn", leftX, y);
        github = addField("GitHub", rightX, y);
        y += 90;

        summary = addArea("Professional Summary", 40, y, 950, 90);
        y += 130;

        skills = addArea("Skills", 40, y, 950, 90);
        y += 130;

        education = addArea("Education", 40, y, 950, 90);
        y += 130;

        projects = addArea("Projects", 40, y, 950, 110);
        y += 150;

        experience = addArea("Experience", 40, y, 950, 110);
        y += 150;

        certifications = addArea("Certifications", 40, y, 950, 90);
        y += 140;

        saveBtn = new JButton("Save Resume");
        saveBtn.setBounds(120, y, 180, 42);
        mainPanel.add(saveBtn);

        previewBtn = new JButton("Live Preview");
        previewBtn.setBounds(340, y, 180, 42);
        mainPanel.add(previewBtn);

        exportDocBtn = new JButton("Export .DOC");
        exportDocBtn.setBounds(560, y, 180, 42);
        mainPanel.add(exportDocBtn);

        exportPdfBtn = new JButton("Export .PDF");
        exportPdfBtn.setBounds(780, y, 180, 42);
        mainPanel.add(exportPdfBtn);

        styleButton(saveBtn);
        styleButton(previewBtn);
        styleButton(exportDocBtn);
        styleButton(exportPdfBtn);

        saveBtn.addActionListener(e -> saveResume());
        previewBtn.addActionListener(e -> showPreview());
        exportDocBtn.addActionListener(e -> exportFile(".doc"));
        exportPdfBtn.addActionListener(e -> exportFile(".pdf"));

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField addField(String label, int x, int y) {

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setBounds(x, y, 180, 25);
        mainPanel.add(lbl);

        JTextField tf = new JTextField();
        tf.setBounds(x, y + 30, 430, 35);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mainPanel.add(tf);

        return tf;
    }

    private JTextArea addArea(String label, int x, int y, int w, int h) {

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setBounds(x, y, 250, 25);
        mainPanel.add(lbl);

        JTextArea ta = new JTextArea();
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ta.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane sp = new JScrollPane(ta);
        sp.setBounds(x, y + 30, w, h);
        mainPanel.add(sp);

        return ta;
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
        mainPanel.setBackground(bg);

        applyThemeToComponents(mainPanel, bg, card, text);
        repaint();
    }

    private void applyThemeToComponents(Container container, Color bg, Color card, Color text) {

        for (Component c : container.getComponents()) {

            if (c instanceof JLabel) {
                c.setForeground(text);
                c.setBackground(bg);
            }

            else if (c instanceof JTextField || c instanceof JTextArea) {
                c.setBackground(card);
                c.setForeground(text);
            }

            else if (c instanceof JButton) {
                c.setBackground(card);
                c.setForeground(text);
            }

            else if (c instanceof JScrollPane) {
                c.setBackground(bg);
                ((JScrollPane) c).getViewport().setBackground(card);
            }

            if (c instanceof Container) {
                applyThemeToComponents((Container) c, bg, card, text);
            }
        }
    }

    private void showPreview() {

        JTextArea area = new JTextArea(buildResumeText());
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(700, 520));

        JOptionPane.showMessageDialog(
                this,
                sp,
                "Live Resume Preview",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void exportFile(String extension) {

        try {

            String fileName = resumeName.getText().trim();

            if (fileName.isEmpty()) {
                fileName = "Resume";
            }

            fileName += extension;

            FileWriter fw = new FileWriter(fileName);
            fw.write(buildResumeText());
            fw.close();

            JOptionPane.showMessageDialog(
                    this,
                    "Exported Successfully:\n" + fileName
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Export Failed");
        }
    }

    private String buildResumeText() {

        return
                "==============================\n" +
                fullName.getText() + "\n" +
                "==============================\n\n" +

                "Email: " + email.getText() + "\n" +
                "Phone: " + phone.getText() + "\n" +
                "LinkedIn: " + linkedin.getText() + "\n" +
                "GitHub: " + github.getText() + "\n\n" +

                "PROFESSIONAL SUMMARY\n" +
                summary.getText() + "\n\n" +

                "SKILLS\n" +
                skills.getText() + "\n\n" +

                "EDUCATION\n" +
                education.getText() + "\n\n" +

                "PROJECTS\n" +
                projects.getText() + "\n\n" +

                "EXPERIENCE\n" +
                experience.getText() + "\n\n" +

                "CERTIFICATIONS\n" +
                certifications.getText() + "\n";
    }

    private void saveResume() {

        try {

            Connection con = DBConnection.getConnection();

            int score = calculateATS();

            String sql =
                    "INSERT INTO resumes(title,candidate_name,email,phone,linkedin,github,objective,skills,education,projects,experience,certifications,score,address) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, resumeName.getText());
            ps.setString(2, fullName.getText());
            ps.setString(3, email.getText());
            ps.setString(4, phone.getText());
            ps.setString(5, linkedin.getText());
            ps.setString(6, github.getText());
            ps.setString(7, summary.getText());
            ps.setString(8, skills.getText());
            ps.setString(9, education.getText());
            ps.setString(10, projects.getText());
            ps.setString(11, experience.getText());
            ps.setString(12, certifications.getText());
            ps.setInt(13, score);
            ps.setString(14, "");

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Resume Saved Successfully!\nATS Score: " + score + "%"
            );

        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Save Failed");
        }
    }

    private int calculateATS() {

        int score = 0;

        if (!fullName.getText().trim().isEmpty()) score += 10;
        if (!email.getText().trim().isEmpty()) score += 10;
        if (!phone.getText().trim().isEmpty()) score += 10;
        if (!linkedin.getText().trim().isEmpty()) score += 10;
        if (!github.getText().trim().isEmpty()) score += 5;
        if (!summary.getText().trim().isEmpty()) score += 10;
        if (!skills.getText().trim().isEmpty()) score += 15;
        if (!education.getText().trim().isEmpty()) score += 10;
        if (!projects.getText().trim().isEmpty()) score += 10;
        if (!experience.getText().trim().isEmpty()) score += 10;
        if (!certifications.getText().trim().isEmpty()) score += 10;

        if (score > 100) score = 100;

        return score;
    }
}