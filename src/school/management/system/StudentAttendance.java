package school.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentAttendance extends JFrame {

    private JTextField rollNumberField;
    private JButton checkButton;
    private JTextArea resultArea;

    public StudentAttendance() {
        setTitle("Student Attendance Checker");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Set modern UI style
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        JLabel titleLabel = new JLabel("Student Attendance Portal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Input panel with horizontal layout
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
                "Enter Student Details",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(0, 102, 204)));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel rollLabel = new JLabel("Student Roll Number:");
        rollLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        rollNumberField = new JTextField(15);
        rollNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        rollNumberField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        checkButton = new JButton("Check Attendance");
        styleButton(checkButton);

        inputPanel.add(rollLabel);
        inputPanel.add(rollNumberField);
        inputPanel.add(checkButton);
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Results area
        resultArea = new JTextArea(10, 30);  // Set preferred rows and columns
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new TitledBorder(
                BorderFactory.createEmptyBorder(),
                "Attendance Results",
                TitledBorder.LEADING,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(0, 102, 204)));
        
        // Adjust layout proportions
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Button click event
        checkButton.addActionListener(e -> {
            try {
                int rollNumber = Integer.parseInt(rollNumberField.getText());
                displayAttendance(rollNumber);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid roll number", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 102, 204)); // Blue color
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 76, 153)),
                BorderFactory.createEmptyBorder(8, 25, 8, 25)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 76, 153)); // Darker blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 102, 204)); // Original blue
            }
        });
    }
    private void displayAttendance(int rollNumber) {
        // Clear previous results
        resultArea.setText("");
        
        // Create SQL query and connection
        String query = "SELECT subject, COUNT(*) AS total_classes, " +
                       "SUM(CASE WHEN attendance = 'P' THEN 1 ELSE 0 END) AS present_classes " +
                       "FROM attendance_records WHERE roll_number = ? GROUP BY subject";
        
         
        try {
            
            Connect c = new Connect();
            
            PreparedStatement statement = c.c.prepareStatement(query);

            // Set roll number parameter in query
            statement.setInt(1, rollNumber);
            ResultSet resultSet = statement.executeQuery();
            
            // Process results and display in resultArea
            boolean found = false;
            while (resultSet.next()) {
                String subject = resultSet.getString("subject");
                int totalClasses = resultSet.getInt("total_classes");
                int presentClasses = resultSet.getInt("present_classes");
                double attendancePercentage = (double) presentClasses / totalClasses * 100;
                
                resultArea.append(String.format("Subject: %s\n", subject));
                resultArea.append(String.format("Total Classes: %d\n", totalClasses));
                resultArea.append(String.format("Present Classes: %d\n", presentClasses));
                resultArea.append(String.format("Attendance Percentage: %.2f%%\n\n", attendancePercentage));
                
                found = true;
            }

            if (!found) {
                resultArea.append("No attendance records found for this roll number.\n");
            }

        } catch (SQLException e) {
            resultArea.append("Error retrieving attendance data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new StudentAttendance();
    }
}
