
package school.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.border.Border;

class VSButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public VSButton(String text) {
        super(text);
        if("Cancel".equals(text)){
            hoverColor = new Color(255, 99, 71);  
            pressColor = new Color(178, 34, 34);
            setBackground(new Color(255, 0, 0));
        }else{
            setBackground(new Color(0, 102, 204));
        }
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setForeground(Color.WHITE);
        
        setBorder(new RoundedBorder(15));
        setPreferredSize(new Dimension(200, 45));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if("Cancel".equals(text)){
             
                 setBackground(new Color(255, 0, 0));
               }else{
                 setBackground(new Color(0, 102, 204));
               }
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        super.paintComponent(g);
    }
}

 

public class ViewStudentDetails extends JFrame {

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(42, 53, 81);
    private final Color SECONDARY_COLOR = new Color(67, 141, 255);
    private final Color BACKGROUND_COLOR = new Color(240, 245, 249);
    
    VSButton searchButton;
    VSButton resetButton;
    JComboBox<String> classDropdown;
    JTextField searchField;

    public ViewStudentDetails() {
        // Frame properties
        setTitle("Student Management System - Student Details");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Search Panel
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel classLabel = createStyledLabel("Filter by Class:");
        classDropdown = new JComboBox<>(fetchClasses());
        styleComboBox(classDropdown);

        JLabel searchLabel = createStyledLabel("Search by Roll:");
        searchField = createStyledTextField(15);

        searchButton = new VSButton("Search");
        resetButton = new VSButton("Reset");

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(classLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchPanel.add(classDropdown, gbc);

        gbc.gridx = 2;
        searchPanel.add(searchLabel, gbc);

        gbc.gridx = 3;
        searchPanel.add(searchField, gbc);

        gbc.gridx = 4;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 5;
        searchPanel.add(resetButton, gbc);

        // Table Setup
        JTable table = new JTable();
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(255, 223, 186));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setBorder(new LineBorder(PRIMARY_COLOR));
        header.setReorderingAllowed(false);

        // Table Model
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] columns = {"Name", "Father's Name", "Roll", "Date of Birth", 
                           "Address", "Phone", "Email", "Birth Cert. No", "Class"};
        for (String col : columns) {
            tableModel.addColumn(col);
        }

        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Button Actions
        searchButton.addActionListener(e -> {
            String selectedClass = (String) classDropdown.getSelectedItem();
            String rollInput = searchField.getText().trim();
            fetchData(tableModel, selectedClass, rollInput);
        });

        resetButton.addActionListener(e -> {
            classDropdown.setSelectedIndex(0);
            searchField.setText("");
            fetchData(tableModel, "", "");
        });

        // Initial Data Load
        fetchData(tableModel, "", "");

        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set column widths
        setColumnWidths(table);

        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(PRIMARY_COLOR);
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void setColumnWidths(JTable table) {
        int[] columnWidths = {150, 150, 80, 100, 200, 120, 200, 150, 100};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private String[] fetchClasses() {
        try {
            Connect connect = new Connect();
            ResultSet rs = connect.s.executeQuery("SELECT DISTINCT class FROM student");

            java.util.List<String> classes = new java.util.ArrayList<>();
            classes.add("All Classes");

            while (rs.next()) {
                classes.add(rs.getString("class"));
            }
            return classes.toArray(new String[0]);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return new String[]{"All Classes"};
        }
    }

    private void fetchData(DefaultTableModel tableModel, String selectedClass, String roll) {
        tableModel.setRowCount(0);
        try {
            Connect connect = new Connect();
            StringBuilder query = new StringBuilder("SELECT name, fname, rollno, dob, address, phone, email, bc, class FROM student");
            
            java.util.List<String> params = new java.util.ArrayList<>();
            if (!selectedClass.equals("All Classes") && !selectedClass.isEmpty()) {
                query.append(" WHERE class = ?");
                params.add(selectedClass);
            }
            if (!roll.isEmpty()) {
                query.append(params.isEmpty() ? " WHERE " : " AND ");
                query.append("rollno = ?");
                params.add(roll);
            }

            PreparedStatement pstmt = connect.c.prepareStatement(query.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("name"),
                    rs.getString("fname"),
                    rs.getInt("rollno"),
                    rs.getString("dob"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("bc"),
                    rs.getString("class")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewStudentDetails();
        });
    }
}