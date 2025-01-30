package school.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.EmptyBorder;

class MButton extends JButton {
    private Color defaultColor = new Color(0, 122, 255); // Modern blue
    private Color hoverColor = new Color(0, 90, 200);
    private Color pressedColor = new Color(0, 60, 150);
    private int cornerRadius = 8;

    public MButton(String text) {
        super(text);
        setUIProperties();
        addInteractivity();
    }

    private void setUIProperties() {
        // Basic styling
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addInteractivity() {
        // Hover and press effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
                setBorder(BorderFactory.createEmptyBorder(2, 2, 0, 0));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(defaultColor);
                setBorder(new EmptyBorder(10, 20, 10, 20));
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(pressedColor);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(hoverColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Rounded rectangle background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isArmed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(defaultColor);
        }

        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optional: Add border styling
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 0.1f));
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        g2.dispose();
    }

    // Optional: Size customization
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(140, 60);
    }
}

public class StudentMaterials extends JFrame {

    private Color primaryColor = new Color(0, 102, 204); // Primary blue color
    private Color secondaryColor = new Color(245, 245, 245); // Light gray background

    public StudentMaterials() {
        // Frame properties
        setTitle("Student Study Materials");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        // Add components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        createMainSection(mainPanel);

        add(mainPanel);
        setVisible(true);
    }

    private void createMainSection(JPanel mainPanel) {
        // Top panel for subject selection
        JPanel topPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        topPanel.setBackground(Color.WHITE);
        
        JLabel classLabel = createStyledLabel("Select Class:");
        JComboBox<String> classDropdown = createStyledComboBox(new String[]{"9A", "9B", "10A", "10B"});
        JLabel subjectLabel = createStyledLabel("Select Subject:");
        JComboBox<String> subjectDropdown = createStyledComboBox(new String[]{"Mathematics", "Physics", "Chemistry", "Biology"});
        MButton fetchButton = new MButton("Fetch Materials");
        
        topPanel.add(classLabel);
        topPanel.add(classDropdown);
        topPanel.add(subjectLabel);
        topPanel.add(subjectDropdown);
        topPanel.add(fetchButton);

        // Table to display materials
        String[] columns = {"Description", "Link", "Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable materialsTable = new JTable(tableModel);
        styleTable(materialsTable);
        JScrollPane scrollPane = new JScrollPane(materialsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Open link when clicked
        materialsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = materialsTable.rowAtPoint(evt.getPoint());
                int col = materialsTable.columnAtPoint(evt.getPoint());
                if (col == 1) { // Link column
                    String url = (String) materialsTable.getValueAt(row, col);
                    try {
                        Desktop.getDesktop().browse(new java.net.URI(url));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Failed to open link!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Action listener for the fetch button
        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedClass = (String) classDropdown.getSelectedItem();
                String selectedSubject = (String) subjectDropdown.getSelectedItem();
                tableModel.setRowCount(0);
                
                List<Material> materials = fetchMaterialsFromDatabase(selectedClass, selectedSubject);
                for (Material material : materials) {
                    tableModel.addRow(new Object[]{material.getDescription(), material.getLink(), material.getType()});
                }
            }
        });

        // Add panels to the main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(primaryColor);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 204, 204)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return comboBox;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(primaryColor);
            }
        });
        
        return button;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(200); // Description
        table.getColumnModel().getColumn(1).setPreferredWidth(600); // Link
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Type
        
        // Link column renderer
        table.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                label.setForeground(new Color(0, 102, 204));
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return label;
            }
        });
        
        // Type column center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        
        // Row striping
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                    isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? new Color(248, 248, 248) : Color.WHITE);
                return c;
            }
        });
    }

    private List<Material> fetchMaterialsFromDatabase(String cl, String subject) {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT description, drive_link, file_type FROM materials " +
               "WHERE subject = '" + subject + "' AND class = '" + cl + "'";

        try {
            Connect connect = new Connect();
            ResultSet resultSet = connect.s.executeQuery(query);
            
            while (resultSet.next()) {
                String description = resultSet.getString("description");
                String link = resultSet.getString("drive_link");
                String type = resultSet.getString("file_type");
                materials.add(new Material(description, link, type));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return materials;
    }

    public static void main(String[] args) {
        new StudentMaterials();
    }

    static class Material {
        private String description;
        private String link;
        private String type;

        public Material(String description, String link, String type) {
            this.description = description;
            this.link = link;
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public String getLink() {
            return link;
        }

        public String getType() {
            return type;
        }
    }
}