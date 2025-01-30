package school.management.system;

import com.toedter.calendar.JDateChooser; // Import JDateChooser
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;

public class TeacherAttendanceSubmission extends JFrame {

    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> classDropdown;
    private JComboBox<String> subjectDropdown;
    private JDateChooser dateChooser; // Add the date chooser
    private Connect dbConnection;

    public TeacherAttendanceSubmission(String username) {
        dbConnection = new Connect();

        // Frame properties
        setTitle("Submit Attendance");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 72, 192)); // Darker blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headerPanel.setPreferredSize(new Dimension(800, 100));

        JLabel headerLabel = new JLabel("Submit Attendance", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Selection Panel
        JPanel selectionPanel = new JPanel(new GridLayout(1, 7, 10, 10));
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 20, 20, 20),
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1)
        ));
        selectionPanel.setBackground(Color.WHITE);

        // Class Dropdown
        JPanel classPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        classPanel.setBackground(Color.WHITE);
        JLabel classLabel = new JLabel("Class:");
        classLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classDropdown = new JComboBox<>(fetchClasses());
        classDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classDropdown.setPreferredSize(new Dimension(120, 30));
        classPanel.add(classLabel);
        classPanel.add(classDropdown);
        selectionPanel.add(classPanel);

        // Subject Dropdown
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.setBackground(Color.WHITE);
        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectDropdown = new JComboBox<>(new String[]{"Math", "Physics", "Chemistry", "Biology"});
        subjectDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectDropdown.setPreferredSize(new Dimension(120, 30));
        subjectPanel.add(subjectLabel);
        subjectPanel.add(subjectDropdown);
        selectionPanel.add(subjectPanel);

        // Date Chooser
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.setBackground(Color.WHITE);
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateChooser.setPreferredSize(new Dimension(150, 30));
        datePanel.add(dateLabel);
        datePanel.add(dateChooser);
        selectionPanel.add(datePanel);

        // Fetch Button
        JButton fetchButton = new JButton("Fetch Students");
        styleButton(fetchButton, new Color(33, 72, 192), Color.WHITE);
        fetchButton.addActionListener(e -> fetchStudentData());
        selectionPanel.add(fetchButton);

        add(selectionPanel, BorderLayout.NORTH);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(20, 20, 20, 20),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                "Mark Attendance",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(33, 72, 192)
            )
        ));
        tablePanel.setBackground(Color.WHITE);

        // Table Model and Configuration
        String[] columnNames = {"Roll Number", "Student Name", "Attendance (P/A)"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        attendanceTable = new JTable(tableModel);
        attendanceTable.setRowHeight(30);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        attendanceTable.setGridColor(new Color(225, 225, 225));
        attendanceTable.setShowGrid(true);
        
        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < attendanceTable.getColumnCount(); i++) {
            attendanceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Add ComboBox for Attendance column
        JComboBox<String> attendanceCombo = new JComboBox<>(new String[]{"P", "A"});
        attendanceCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        attendanceTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(attendanceCombo));

        // Table Header Styling
        JTableHeader tableHeader = attendanceTable.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHeader.setBackground(new Color(33, 72, 192));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 35));

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.getViewport().setBackground(Color.WHITE);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        footerPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        footerPanel.setBackground(Color.WHITE);

        JButton submitButton = new JButton("Submit Attendance");
        styleButton(submitButton, new Color(33, 150, 83), Color.WHITE);
        submitButton.addActionListener(e -> submitAttendance());
        footerPanel.add(submitButton);

        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private String[] fetchClasses() {
        try {
            Statement stmt = dbConnection.c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT class FROM student");
            java.util.List<String> classes = new java.util.ArrayList<>();
            while (rs.next()) {
                classes.add(rs.getString("class"));
            }
            return classes.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    private void fetchStudentData() {
        String selectedClass = (String) classDropdown.getSelectedItem();
        tableModel.setRowCount(0); // Clear existing rows

        try {
            PreparedStatement pstmt = dbConnection.c.prepareStatement("SELECT rollno, name FROM student WHERE class = ?");
            pstmt.setString(1, selectedClass);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int rollNumber = rs.getInt("rollno");
                String name = rs.getString("name");
                tableModel.addRow(new Object[]{rollNumber, name, ""});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching student data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitAttendance() {
        int rowCount = tableModel.getRowCount();
        String selectedClass = (String) classDropdown.getSelectedItem();
        String selectedSubject = (String) subjectDropdown.getSelectedItem();

        // Get the selected date from date chooser
        Date selectedDate = dateChooser.getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a date!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

        try {
            PreparedStatement pstmt = dbConnection.c.prepareStatement(
                    "INSERT INTO attendance_records (roll_number, class, subject, date, attendance) VALUES (?, ?, ?, ?, ?)");

            for (int i = 0; i < rowCount; i++) {
                int rollNumber = (int) tableModel.getValueAt(i, 0);
                String attendance = (String) tableModel.getValueAt(i, 2);

                if (attendance == null || attendance.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please mark attendance for all students!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                pstmt.setInt(1, rollNumber);
                pstmt.setString(2, selectedClass);
                pstmt.setString(3, selectedSubject);
                pstmt.setString(4, formattedDate); // Use the selected date
                pstmt.setString(5, attendance);
                pstmt.addBatch(); // Add to batch for efficiency
            }

            pstmt.executeBatch(); // Execute batch update
            JOptionPane.showMessageDialog(this, "Attendance submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting attendance.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new TeacherAttendanceSubmission("sir");
    }
}