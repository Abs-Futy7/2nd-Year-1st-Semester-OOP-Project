package school.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndividualTeacherClass extends JFrame {

    private JTable classesTable;
    private DefaultTableModel tableModel;
    private JTextField teacherIdField;
    int teacherid;

    public IndividualTeacherClass(int id) {
        this.teacherid = id;
        // Frame properties
        setTitle("Teacher Class Assignment System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Initialize UI components
        initializeUI();
        handleSearch(teacherid);
        setVisible(true);
    }

    private void initializeUI() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel headerLabel = new JLabel("Teacher Class Assignments", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        
        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

      //  mainPanel.add(createSearchPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        return mainPanel;
    }

//    private JPanel createSearchPanel() {
//        JPanel searchPanel = new JPanel(new GridBagLayout());
//        searchPanel.setBackground(Color.WHITE);
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//
//        JLabel idLabel = new JLabel("Enter Teacher ID:");
//        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
//        
//        teacherIdField = new JTextField(15);
//        teacherIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//        
//        JButton searchButton = new JButton("Search");
//        styleButton(searchButton, new Color(0, 102, 204));
//
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        searchPanel.add(idLabel, gbc);
//        
//        gbc.gridx = 1;
//        searchPanel.add(teacherIdField, gbc);
//        
//        gbc.gridx = 2;
//        searchPanel.add(searchButton, gbc);
//        
//        searchButton.addActionListener(e -> handleSearch());
//
//        return searchPanel;
//    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2), 
                "Assigned Classes", 
                TitledBorder.LEFT, 
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(0, 102, 204)),
            new EmptyBorder(10, 10, 10, 10)
        ));

        String[] columnNames = {"Class", "Subject", "Time", "Classroom"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        classesTable = new JTable(tableModel);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(classesTable);
        scrollPane.setPreferredSize(new Dimension(900, 400));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void styleTable() {
        classesTable.setRowHeight(30);
        classesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        classesTable.setGridColor(new Color(224, 224, 224));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < classesTable.getColumnCount(); i++) {
            classesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = classesTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        footerPanel.setBackground(Color.WHITE);

        JButton backButton = new JButton("Back to Dashboard");
        styleButton(backButton, new Color(102, 102, 102));

        backButton.addActionListener(e -> {
            dispose();
            // new TeacherDashboard().setVisible(true);
        });

        footerPanel.add(backButton);
        return footerPanel;
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void handleSearch(int teacherid) {
 //       String teacherId = teacherIdField.getText().trim();
//        if (teacherId.isEmpty()) {
//            showError("Please enter a Teacher ID");
//            return;
//        }
//
//        if (!teacherId.matches("\\d+")) {
//            showError("Invalid Teacher ID. Please enter numbers only.");
//            return;
//        }

        Connection connection = null;
        try {
            Connect connect = new Connect();
            connection = connect.c;
            List<ClassAssignment> assignments = fetchAssignments(connection, teacherid);
            
            SwingUtilities.invokeLater(() -> {
                tableModel.setRowCount(0);
                if (assignments.isEmpty()) {
                    showInfo("No classes found for Teacher ID: " + teacherid);
                } else {
                    for (ClassAssignment assignment : assignments) {
                        tableModel.addRow(new Object[]{
                            assignment.getClassName(),
                            assignment.getSubject(),
                            assignment.getClassTime(),
                            assignment.getClassroom()
                        });
                    }
                }
            });
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private List<ClassAssignment> fetchAssignments(Connection connection, int teacherId) throws SQLException {
        List<ClassAssignment> assignments = new ArrayList<>();
        
        String sql = "SELECT class, subject, time, classroom FROM assignclass WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,  teacherId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    assignments.add(new ClassAssignment(
                        rs.getString("class"),
                        rs.getString("subject"),
                        rs.getString("time"),
                        rs.getString("classroom")
                    ));
                }
            }
        }
        return assignments;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private static class ClassAssignment {
        private final String className;
        private final String subject;
        private final String classTime;
        private final String classroom;

        public ClassAssignment(String className, String subject, String classTime, String classroom) {
            this.className = className;
            this.subject = subject;
            this.classTime = classTime;
            this.classroom = classroom;
        }

        public String getClassName() { return className; }
        public String getSubject() { return subject; }
        public String getClassTime() { return classTime; }
        public String getClassroom() { return classroom; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IndividualTeacherClass(11117584));
    }
}