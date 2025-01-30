
package school.management.system;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AssignClass extends JFrame {
    private DefaultListModel<String> teacherListModel;
    private JList<String> teacherList;
    private Map<String, JCheckBox> subjectCheckboxes;
    private Map<String, JComboBox<String>> classComboboxes;
    private Map<String, JComboBox<String>> timeComboboxes;
    private Map<String, JComboBox<String>> roomComboboxes;
    
    int empid;

    public AssignClass() {
        setTitle("Teacher Assignment System");
        setSize(1200, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        initComponents();
        layoutComponents();
        loadTeachersFromDatabase();
        
        setVisible(true);
    }

    private void initComponents() {
        teacherListModel = new DefaultListModel<>();
        teacherList = new JList<>(teacherListModel);
        teacherList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teacherList.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        subjectCheckboxes = new HashMap<>();
        classComboboxes = new HashMap<>();
        
        String[] subjects = {"Physics", "Chemistry", "Mathematics", "Biology"};
        String[] classes = {"10A", "10B", "9A", "9B"};
        String[] timeSlots = {"08:00 AM", "09:30 AM", "11:00 AM", "01:00 PM", "02:30 PM"};
        String[] classrooms = {"Room 101", "Room 102", "Room 201", "Room 202", "Lab 1", "Lab 2"};
        
        timeComboboxes = new HashMap<>();
        roomComboboxes = new HashMap<>();

        for (String subject : subjects) {
            subjectCheckboxes.put(subject, new JCheckBox(subject));
        }

        for (String subject : subjects) {
            JComboBox<String> combo = new JComboBox<>(classes);
            combo.setEnabled(false);
            classComboboxes.put(subject, combo);
        }
        
        for (String subject : subjects) {
            JComboBox<String> combo = new JComboBox<>(classes);
            JComboBox<String> timeCombo = new JComboBox<>(timeSlots);
            JComboBox<String> roomCombo = new JComboBox<>(classrooms);
            
            combo.setEnabled(false);
            timeCombo.setEnabled(false);
            roomCombo.setEnabled(false);
            
            classComboboxes.put(subject, combo);
            timeComboboxes.put(subject, timeCombo);
            roomComboboxes.put(subject, roomCombo);
        }
    }


    private void layoutComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left Panel - Teacher List
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new TitledBorder("Teachers"));
        leftPanel.add(new JScrollPane(teacherList), BorderLayout.CENTER);

 

        // Right Panel - Assignment Details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new TitledBorder("Assignment Details"));

 
        
        JPanel subjectPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        subjectPanel.setBorder(new TitledBorder("Assigned Subjects"));
        
        for (String subject : subjectCheckboxes.keySet()) {
            JPanel rowPanel = new JPanel(new GridLayout(1, 5, 5, 5));
            
            JCheckBox cb = subjectCheckboxes.get(subject);
            JComboBox<String> classCombo = classComboboxes.get(subject);
            JComboBox<String> timeCombo = timeComboboxes.get(subject);
            JComboBox<String> roomCombo = roomComboboxes.get(subject);
            
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            cb.addActionListener(e -> updateSelections(cb));
            
            rowPanel.add(cb);
            rowPanel.add(new JLabel("Class:"));
            rowPanel.add(classCombo);
            rowPanel.add(new JLabel("Time:"));
            rowPanel.add(timeCombo);
            rowPanel.add(new JLabel("Room:"));
            rowPanel.add(roomCombo);
            
            subjectPanel.add(rowPanel);
        }


        JButton saveButton = createStyledButton("Save Changes", Color.BLUE);
        saveButton.setPreferredSize(new Dimension(120, 35));

        rightPanel.add(subjectPanel, BorderLayout.CENTER);
        rightPanel.add(saveButton, BorderLayout.SOUTH);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }
    private void updateSelections(JCheckBox cb) {
        String subject = cb.getText();
        boolean enabled = cb.isSelected();
        classComboboxes.get(subject).setEnabled(enabled);
        timeComboboxes.get(subject).setEnabled(enabled);
        roomComboboxes.get(subject).setEnabled(enabled);
    }
    
    private void loadTeachersFromDatabase() {
        try {
            Connect connect = new Connect();
            ResultSet rs = connect.s.executeQuery("SELECT name,id FROM teacher");
            
            teacherListModel.clear();
            while (rs.next()) {
                teacherListModel.addElement( rs.getString("name")+ " - " + rs.getInt("id"));
            }           
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading teachers: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            
            if (text.equals("Save Changes")) {
                saveAssignmentsToDatabase();
            }
        });
        
        return button;
    }
    
    
   
    private void saveAssignmentsToDatabase() {
        String selectedTeacher = teacherList.getSelectedValue();
        
        if (selectedTeacher == null) {
            JOptionPane.showMessageDialog(this, "Please select a teacher first!");
            return;
        }
        String[] parts = selectedTeacher.split(" - ");
        if (parts.length != 2) {
        JOptionPane.showMessageDialog(this, "Invalid teacher selection!");
        return;
        }
        

        try {
            
             empid = Integer.parseInt(parts[1]);
            String teacherName = parts[0];
            Connect connect = new Connect();
            Connection conn = connect.c;
            conn.setAutoCommit(false);

            // Clear existing assignments
            PreparedStatement clearStmt = conn.prepareStatement(
            "DELETE FROM assignclass WHERE id = ?");
            clearStmt.setInt(1, empid);
            clearStmt.executeUpdate();

            // Insert new assignments
            PreparedStatement insertStmt = conn.prepareStatement(
                "INSERT INTO assignclass (id,teacher_name, subject, class, time, classroom) VALUES (?,?,?,?, ?, ?)");

            for (String subject : subjectCheckboxes.keySet()) {
            JCheckBox cb = subjectCheckboxes.get(subject);
            if (cb.isSelected()) {
                String className = (String) classComboboxes.get(subject).getSelectedItem();
                String time = (String) timeComboboxes.get(subject).getSelectedItem();
                String room = (String) roomComboboxes.get(subject).getSelectedItem();
                
                
                
                insertStmt.setInt(1, empid);
                insertStmt.setString(2, teacherName);
                insertStmt.setString(3, subject);
                insertStmt.setString(4, className);
                insertStmt.setString(5, time);
                insertStmt.setString(6, room);
                insertStmt.addBatch();
            }
        }

            insertStmt.executeBatch();
            conn.commit();
            JOptionPane.showMessageDialog(this, "Assignments saved successfully!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error saving assignments: " + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateClassSelection(JCheckBox cb) {
        String subject = cb.getText();
        classComboboxes.get(subject).setEnabled(cb.isSelected());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AssignClass();
        });
    }
}