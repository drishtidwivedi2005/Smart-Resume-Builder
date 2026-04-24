package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;

public class ViewResumeFrame extends JFrame {

    JTable table;
    DefaultTableModel model;
    JTextField searchField;

    JButton viewBtn, editBtn, exportBtn, deleteBtn, refreshBtn, searchBtn, themeBtn;

    boolean darkMode = false;

    Color lightBg = new Color(245, 247, 250);
    Color darkBg = new Color(32, 34, 37);

    Color lightCard = Color.WHITE;
    Color darkCard = new Color(45, 47, 51);

    Color lightText = new Color(30, 30, 30);
    Color darkText = new Color(240, 240, 240);

    public ViewResumeFrame() {

        setTitle("Resume Manager");
        setSize(1180, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        buildUI();
        applyTheme();
        loadData();

        setVisible(true);
    }

    private void buildUI() {

        JLabel title = new JLabel("Saved Resume Manager");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setBounds(30, 18, 380, 35);
        add(title);

        themeBtn = new JButton("Dark Mode");
        themeBtn.setBounds(980, 18, 160, 36);
        add(themeBtn);

        themeBtn.addActionListener(e -> {
            darkMode = !darkMode;
            themeBtn.setText(darkMode ? "Light Mode" : "Dark Mode");
            applyTheme();
        });

        searchField = new JTextField();
        searchField.setBounds(30, 78, 320, 38);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(searchField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(370, 78, 130, 38);
        add(searchBtn);

        model = new DefaultTableModel(
                new String[]{"ID", "Resume Name", "Candidate", "Phone", "ATS Score"}, 0) {

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(66, 133, 244));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(25, 140, 1120, 400);
        add(sp);

        viewBtn = new JButton("Live View");
        viewBtn.setBounds(30, 580, 150, 42);
        add(viewBtn);

        editBtn = new JButton("Edit Name");
        editBtn.setBounds(210, 580, 150, 42);
        add(editBtn);

        exportBtn = new JButton("Export PDF/DOC");
        exportBtn.setBounds(390, 580, 180, 42);
        add(exportBtn);

        deleteBtn = new JButton("Delete");
        deleteBtn.setBounds(600, 580, 140, 42);
        add(deleteBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(770, 580, 140, 42);
        add(refreshBtn);

        styleButton(searchBtn);
        styleButton(viewBtn);
        styleButton(editBtn);
        styleButton(exportBtn);
        styleButton(deleteBtn);
        styleButton(refreshBtn);
        styleButton(themeBtn);

        searchBtn.addActionListener(e -> searchData());
        refreshBtn.addActionListener(e -> loadData());
        viewBtn.addActionListener(e -> viewResume());
        editBtn.addActionListener(e -> editResume());
        exportBtn.addActionListener(e -> exportResume());
        deleteBtn.addActionListener(e -> deleteResume());
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

            if (c instanceof JTextField) {
                c.setBackground(card);
                c.setForeground(text);
            }
        }

        table.setBackground(card);
        table.setForeground(text);
        table.getTableHeader().setBackground(card);
        table.getTableHeader().setForeground(text);

        repaint();
    }

    private void loadData() {

        try {

            model.setRowCount(0);

            Connection con = DBConnection.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT resume_id,title,candidate_name,phone,score FROM resumes ORDER BY resume_id DESC"
            );

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchData() {

        try {

            model.setRowCount(0);

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT resume_id,title,candidate_name,phone,score FROM resumes WHERE title LIKE ? OR candidate_name LIKE ?"
            );

            String key = "%" + searchField.getText() + "%";

            ps.setString(1, key);
            ps.setString(2, key);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getInt(5)
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getSelectedId() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a resume first.");
            return -1;
        }

        return Integer.parseInt(model.getValueAt(row, 0).toString());
    }

    private void viewResume() {

        try {

            int id = getSelectedId();
            if (id == -1) return;

            Connection con = DBConnection.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT * FROM resumes WHERE resume_id=" + id
            );

            if (rs.next()) {

                JTextArea area = new JTextArea();
                area.setEditable(false);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                area.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                area.setText(buildResumeText(rs));

                JScrollPane pane = new JScrollPane(area);
                pane.setPreferredSize(new Dimension(760, 540));

                JOptionPane.showMessageDialog(
                        this,
                        pane,
                        "Live Resume Preview",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editResume() {

        try {

            int id = getSelectedId();
            if (id == -1) return;

            String newName = JOptionPane.showInputDialog(
                    this,
                    "Enter New Resume Name:"
            );

            if (newName == null || newName.trim().isEmpty()) return;

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE resumes SET title=? WHERE resume_id=?"
            );

            ps.setString(1, newName.trim());
            ps.setInt(2, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Resume name updated successfully.");

            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportResume() {

        try {

            int id = getSelectedId();
            if (id == -1) return;

            Connection con = DBConnection.getConnection();

            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT * FROM resumes WHERE resume_id=" + id
            );

            if (rs.next()) {

                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Save Resume File");

                chooser.setSelectedFile(
                        new File(rs.getString("title") + ".txt")
                );

                int option = chooser.showSaveDialog(this);

                if (option == JFileChooser.APPROVE_OPTION) {

                    File file = chooser.getSelectedFile();

                    FileWriter fw = new FileWriter(file);
                    fw.write(buildResumeText(rs));
                    fw.close();

                    JOptionPane.showMessageDialog(
                            this,
                            "Resume Exported Successfully:\n" + file.getAbsolutePath()
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Export failed.");
        }
    }

    private String buildResumeText(ResultSet rs) throws Exception {

        return
                "=====================================\n" +
                rs.getString("candidate_name") + "\n" +
                "=====================================\n\n" +

                "Email: " + rs.getString("email") + "\n" +
                "Phone: " + rs.getString("phone") + "\n" +
                "LinkedIn: " + rs.getString("linkedin") + "\n" +
                "GitHub: " + rs.getString("github") + "\n\n" +

                "SUMMARY\n" +
                rs.getString("objective") + "\n\n" +

                "SKILLS\n" +
                rs.getString("skills") + "\n\n" +

                "EDUCATION\n" +
                rs.getString("education") + "\n\n" +

                "PROJECTS\n" +
                rs.getString("projects") + "\n\n" +

                "EXPERIENCE\n" +
                rs.getString("experience") + "\n\n" +

                "CERTIFICATIONS\n" +
                rs.getString("certifications") + "\n\n" +

                "ATS SCORE: " + rs.getInt("score") + "%";
    }

    private void deleteResume() {

        try {

            int id = getSelectedId();
            if (id == -1) return;

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Delete selected resume?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            Connection con = DBConnection.getConnection();

            con.createStatement().executeUpdate(
                    "DELETE FROM resumes WHERE resume_id=" + id
            );

            JOptionPane.showMessageDialog(this, "Resume deleted.");

            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}