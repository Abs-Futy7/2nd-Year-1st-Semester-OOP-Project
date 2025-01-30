 
package school.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UploadMaterials {
    private Connect connect;
    private static final Color PRIMARY_COLOR = new Color(44, 62, 80);
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public UploadMaterials() {
        connect = new Connect();
        createUploadUI();
    }

    private void createUploadUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Upload Learning Materials");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Main container panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Upload Study Materials");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        headerPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2), "Material Details", 
                TitledBorder.LEFT, TitledBorder.TOP, LABEL_FONT, PRIMARY_COLOR),
            new EmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setBackground(Color.WHITE);

        // Class selection
        JPanel classPanel = createInputPanel("Class:", new JComboBox<>(new String[]{"9A", "9B", "10A", "10B"}));
        // Subject selection
        JPanel subjectPanel = createInputPanel("Subject:", new JComboBox<>(new String[]{"Mathematics", "Physics", "Chemistry", "Biology"}));
        // File type selection
        JPanel fileTypePanel = createInputPanel("File Type:", new JComboBox<>(new String[]{"Image", "PDF"}));
        // Drive link input
        JPanel linkPanel = createInputPanel("Drive Link:", new JTextField());
        // Description input
        JPanel descPanel = createInputPanel("Description:", new JTextField());

        formPanel.add(classPanel);
        formPanel.add(subjectPanel);
        formPanel.add(fileTypePanel);
        formPanel.add(linkPanel);
        formPanel.add(descPanel);

        // Upload button
        JButton uploadButton = new JButton("Upload Material");
        styleButton(uploadButton, SECONDARY_COLOR, Color.BLACK, 16);
        uploadButton.setPreferredSize(new Dimension(200, 40));

        // Uploaded files panel
        JPanel filesPanel = new JPanel(new BorderLayout());
        filesPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2), "Uploaded Materials", 
                TitledBorder.LEFT, TitledBorder.TOP, LABEL_FONT, PRIMARY_COLOR),
            new EmptyBorder(10, 10, 10, 10)
        ));
        filesPanel.setBackground(Color.WHITE);

        JTextArea uploadedFilesArea = new JTextArea();
        uploadedFilesArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        uploadedFilesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(uploadedFilesArea);
        filesPanel.add(scrollPane);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(uploadButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(filesPanel, BorderLayout.CENTER);

        // Upload button action
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<?> classCombo = (JComboBox<?>) ((JPanel)formPanel.getComponent(0)).getComponent(1);
                JComboBox<?> subjectCombo = (JComboBox<?>) ((JPanel)formPanel.getComponent(1)).getComponent(1);
                JComboBox<?> fileTypeCombo = (JComboBox<?>) ((JPanel)formPanel.getComponent(2)).getComponent(1);
                JTextField linkField = (JTextField) ((JPanel)formPanel.getComponent(3)).getComponent(1);
                JTextField descField = (JTextField) ((JPanel)formPanel.getComponent(4)).getComponent(1);

                String cl = classCombo.getSelectedItem().toString();
                String subject = subjectCombo.getSelectedItem().toString();
                String fileType = fileTypeCombo.getSelectedItem().toString();
                String driveLink = linkField.getText().trim();
                String description = descField.getText().trim();

                if (driveLink.isEmpty() || description.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill all required fields!", "Input Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveToDatabase(cl, subject, fileType, driveLink, description);
                uploadedFilesArea.append(String.format("[%s] %s - %s\n%s\n\n", 
                    new java.util.Date(), subject, fileType, driveLink));
                
                linkField.setText("");
                descField.setText("");
            }
        });

        frame.setVisible(true);
    }

    private JPanel createInputPanel(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(Color.WHITE);
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(LABEL_FONT);
        jLabel.setForeground(PRIMARY_COLOR);
        
        component.setFont(INPUT_FONT);
        component.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(5, 8, 5, 8)
        ));
        
        if (component instanceof JTextField) {
            ((JTextField) component).setColumns(20);
        }
        
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
        
        return panel;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor, int fontSize) {
        button.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(8, 20, 8, 20)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void saveToDatabase(String cl, String subject, String fileType, String driveLink, String description) {
        try {
            String query = "INSERT INTO materials (class, subject, file_type, drive_link, description) VALUES (?,?,?,?,?)";
            Connection c = connect.c;
            PreparedStatement preparedStatement = c.prepareStatement(query);
            preparedStatement.setString(1, cl);
            preparedStatement.setString(2, subject);
            preparedStatement.setString(3, fileType);
            preparedStatement.setString(4, driveLink);
            preparedStatement.setString(5, description);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UploadMaterials());
    }
}
