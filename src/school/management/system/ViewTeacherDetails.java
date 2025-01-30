package school.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.border.Border;


class VTButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public VTButton(String text) {
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

 

public class ViewTeacherDetails extends JFrame {

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(42, 53, 81);
    private final Color SECONDARY_COLOR = new Color(67, 141, 255);
    private final Color BACKGROUND_COLOR = new Color(240, 245, 249);
    
    VTButton searchButton;
    VTButton resetButton;
    

    public ViewTeacherDetails() {
        // Frame properties
        setTitle("Teacher Management System - Teacher Details");
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

        JLabel searchLabel = new JLabel("Search Teacher by ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(PRIMARY_COLOR);

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(5, 8, 5, 8)
        ));

        searchButton = new VTButton("Search");
        resetButton = new VTButton("Reset");

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(searchLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        searchPanel.add(searchField, gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 3;
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
                return false; // Make table non-editable
            }
        };

        String[] columns = {"Name", "Father's Name", "ID", "Date of Birth", "Address", "Phone", "Email", "NID"};
        for (String col : columns) {
            tableModel.addColumn(col);
        }

        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Button Actions
        searchButton.addActionListener(e -> {
            String id = searchField.getText().trim();
            fetchData(tableModel, id);
        });

        resetButton.addActionListener(e -> {
            searchField.setText("");
            fetchData(tableModel, "");
        });

        // Initial Data Load
        fetchData(tableModel, "");

        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set column widths
        setColumnWidths(table);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void setColumnWidths(JTable table) {
        int[] columnWidths = {150, 150, 80, 100, 200, 120, 200, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
        }
    }

    private void fetchData(DefaultTableModel tableModel, String id) {
        tableModel.setRowCount(0);
        try {
            Connect connect = new Connect();
            String query = "SELECT name, fname, id, dob, address, phone, email, nid FROM teacher";
            
            if (!id.isEmpty()) {
                query += " WHERE id = ?";
                try (PreparedStatement pstmt = connect.c.prepareStatement(query)) {
                    pstmt.setString(1, id);
                    ResultSet rs = pstmt.executeQuery();
                    addRowsToModel(tableModel, rs);
                }
            } else {
                ResultSet rs = connect.s.executeQuery(query);
                addRowsToModel(tableModel, rs);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRowsToModel(DefaultTableModel model, ResultSet rs) throws SQLException {
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("name"),
                rs.getString("fname"),
                rs.getInt("id"),
                rs.getString("dob"),
                rs.getString("address"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("nid")
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewTeacherDetails();
        });
    }
}