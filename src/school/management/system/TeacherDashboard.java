package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.sql.*;
import java.net.URL;

public class TeacherDashboard extends JFrame implements ActionListener {

    private JLabel TeacherNameLabel, TeacherIdLabel, TeacherEmailLabel, TeacherImageLabel, TeacherdobLabel, TeacherphoneLabel, TeacheraddressLabel;
    private String username;
    CustomButton SubmitAttendance, updateInfo, viewClasses, UploadMaterials, chat;
    JButton logout;
    int passroll;

    public TeacherDashboard(String username) {
        this.username = username;

        setTitle("Teacher Dashboard");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setPreferredSize(new Dimension(0, 100));

        JLabel headerLabel = new JLabel("Teacher Dashboard");
        headerLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createVerticalStrut(20));
        headerPanel.add(headerLabel);

        // Main Content Panel
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();

        // Left Panel (Profile Section)
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)  // Reduced padding
         ));
        leftPanel.setPreferredSize(new Dimension(280, 500));

        // Profile Image
        TeacherImageLabel = new JLabel();
        TeacherImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        TeacherImageLabel.setPreferredSize(new Dimension(120, 120));  // Smaller image
        TeacherImageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));  // Reduced padding
        leftPanel.add(TeacherImageLabel);

        // Profile Information
        
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
  

        addInfoRow(infoPanel, "Name:", TeacherNameLabel = new JLabel());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));  // Reduced spacing
        addInfoRow(infoPanel, "ID:", TeacherIdLabel = new JLabel());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addInfoRow(infoPanel, "Email:", TeacherEmailLabel = new JLabel());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addInfoRow(infoPanel, "Date of Birth:", TeacherdobLabel = new JLabel());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addInfoRow(infoPanel, "Phone:", TeacherphoneLabel = new JLabel());
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        addInfoRow(infoPanel, "Address:", TeacheraddressLabel = new JLabel());

        leftPanel.add(infoPanel);

        // Right Panel (Actions)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(240, 240, 240));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 15, 15));
        buttonPanel.setBackground(new Color(240, 240, 240));

        SubmitAttendance = createButton("Submit Attendance");
        viewClasses = createButton("View My Classes"   );
        UploadMaterials = createButton("Upload Materials" );
        updateInfo = createButton("Update Profile" );
        chat = createButton("Chat Room");
        logout = createButton("Logout");
        
        logout.setBackground(Color.RED);
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.setFont(new Font("Arial", Font.BOLD, 14));
        logout.setBorderPainted(false);
        logout.setOpaque(true);
        
        // Add hover effect
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logout.setBackground(new Color(200, 0, 0)); // Darker red on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                logout.setBackground(Color.RED);
            }
        });

        buttonPanel.add(SubmitAttendance);
        buttonPanel.add(viewClasses);
        buttonPanel.add(UploadMaterials);
        buttonPanel.add(updateInfo);
        buttonPanel.add(chat);
        buttonPanel.add(logout);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        rightPanel.add(buttonPanel, gbc);

        // Layout Configuration
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;  // Reduced from 0.3 to give more space to right panel
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;  // Increased from 0.7 to expand right panel
        contentPanel.add(rightPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        retrieveStudentData();
        setVisible(true);
    }

    private void addInfoRow(JPanel panel, String labelText, JLabel valueLabel) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setMaximumSize(new Dimension(280, 30));  // Increased height
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Raleway", Font.BOLD, 13));  // Slightly larger font
        label.setForeground(new Color(34, 42, 53));  // Darker color for better contrast
        
        valueLabel.setFont(new Font("Raleway", Font.PLAIN, 13));
        valueLabel.setForeground(new Color(100, 100, 100));  // Softer text color
        
        rowPanel.add(label, BorderLayout.WEST);
        rowPanel.add(valueLabel, BorderLayout.CENTER);
        
        panel.add(rowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));  // Increased spacing between rows
    }
    private CustomButton createButton(String text) {
        CustomButton button = new CustomButton(text);
        button.setFont(new Font("Raleway", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(72, 132, 219));  // More vibrant blue
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 30, 15, 30),
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 2)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        
        button.addActionListener(this);
        return button;
    }
     

   private void retrieveStudentData() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish connection using the Connect class
            Connect connect = new Connect(); 
            connection = connect.c; // Use the existing connection from Connect class

            String query = "SELECT FullName, Roll, email, dob, phone, address, image FROM register WHERE username = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, username);  // Pass the username to the query

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String fullName = resultSet.getString("FullName");
                int roll = resultSet.getInt("Roll");
                String email = resultSet.getString("email");
                String dob = resultSet.getString("dob");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                byte[] imageBytes = resultSet.getBytes("image");
                 
                // passroll update
                
                passroll = roll;
                // Set the text data to the labels
                TeacherNameLabel.setText(fullName);
                TeacherIdLabel.setText(String.valueOf(roll));
                TeacherEmailLabel.setText(email);
                TeacherdobLabel.setText(dob);
                TeacherphoneLabel.setText(phone);
                TeacheraddressLabel.setText(address);

                // If image exists, set it in the JLabel
                if (imageBytes != null) {
                    InputStream is = new ByteArrayInputStream(imageBytes);
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(is.readAllBytes()).getImage()
                            .getScaledInstance(200, 200, Image.SCALE_SMOOTH));  
                    TeacherImageLabel.setIcon(imageIcon);
                    TeacherImageLabel.setText(null);  // Remove default text if image is found
                } else {
                    TeacherImageLabel.setText("No image available.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Teacher not found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing image: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
        } finally {
            // Close resources to prevent memory leaks
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae.getSource());
        if (ae.getSource() == SubmitAttendance) {
            SwingUtilities.invokeLater(() -> new TeacherAttendanceSubmission(username));
        } else if (ae.getSource() == viewClasses) {
            
            SwingUtilities.invokeLater(() -> new IndividualTeacherClass(passroll));
            
        } else if (ae.getSource() == UploadMaterials) {
             SwingUtilities.invokeLater(() -> new UploadMaterials());
            //JOptionPane.showMessageDialog(this, "Access Study Materials - Feature Coming Soon");
        } else if(ae.getSource() == updateInfo) {
             
             SwingUtilities.invokeLater(() -> new UpdateTeacher(passroll));
            // setVisible(false);
        } else if (ae.getSource() == logout) {
           // JOptionPane.showMessageDialog(this, "Logged Out");
            
           this.dispose();
            setVisible(false);
           
            new MainPage();
        } else if(ae.getSource() == chat){
            new ChatServer();
            new ChatClient();
        }
    }
    class CustomButton extends JButton {
        public CustomButton(String text) {
            super(text);
            setContentAreaFilled(false);
        }

         @Override
        protected void paintComponent(Graphics g) {
            // Gradient background for buttons
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color startColor = getBackground();
            Color endColor = getBackground().darker();
            if (getModel().isPressed()) {
                startColor = getBackground().darker();
                endColor = startColor.darker();
            } else if (getModel().isRollover()) {
                startColor = getBackground().brighter();
                endColor = startColor.darker();
            }
            
            GradientPaint gp = new GradientPaint(0, 0, startColor, 0, getHeight(), endColor);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g2);
            g2.dispose();
        }
    

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new TeacherDashboard("sir");  // Replace with actual username
    }
}
