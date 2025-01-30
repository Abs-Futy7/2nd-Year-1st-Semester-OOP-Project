package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.sql.*;
import java.net.URL;

class StudentCustomButton extends JButton {

    public StudentCustomButton(String text, int width, int height, Font font) {
        super(text);
        setPreferredSize(new Dimension(width, height));
        setFont(font);
        setBackground(new Color(34, 139, 34)); // Forest Green background
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(20, 100, 20)), 
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setContentAreaFilled(false);
    }

    public StudentCustomButton(String text) {
        this(text, 200, 50, new Font("Raleway", Font.BOLD, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int arc = 25;
        ButtonModel model = getModel();
        
        // Set button color based on state
        Color backgroundColor = getBackground();
        if (model.isPressed()) {
            backgroundColor = backgroundColor.darker();
        } else if (model.isRollover()) {
            backgroundColor = backgroundColor.brighter();
        }

        // Paint background
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // Paint text
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground().darker().darker());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
        g2.dispose();
    }
}

public class StudentDashboard extends JFrame implements ActionListener {

    private JLabel studentNameLabel, studentIdLabel, studentEmailLabel, studentImageLabel, studentdobLabel, studentphoneLabel, studentaddressLabel, additionalImageLabel;
    private String username;
    StudentCustomButton viewAttendance, updateInfo, viewGrades, accessMaterials, logout,chat;
    
    int passroll;
    
    String name,sroll,cl;

    public StudentDashboard(String username) {
        this.username = username;

        setTitle("Student Dashboard");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(245, 245, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243)); // Material Blue
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        JLabel headerLabel = new JLabel("Student Dashboard");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(headerLabel);

        // Left Panel - Profile Section
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(15, 15));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 225, 225)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Student Image
        studentImageLabel = new JLabel("", SwingConstants.CENTER);
        studentImageLabel.setPreferredSize(new Dimension(250, 250));
        studentImageLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        studentImageLabel.setBackground(new Color(250, 250, 250));
        studentImageLabel.setOpaque(true);

        // Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 16);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 16);
        Color labelColor = new Color(100, 100, 100);

        studentNameLabel = createInfoLabel("Name:", labelFont, labelColor);
        studentIdLabel = createInfoLabel("Student ID:", labelFont, labelColor);
        studentEmailLabel = createInfoLabel("Email:", labelFont, labelColor);
        studentdobLabel = createInfoLabel("Date of Birth:", labelFont, labelColor);
        studentphoneLabel = createInfoLabel("Phone:", labelFont, labelColor);
        studentaddressLabel = createInfoLabel("Address:", labelFont, labelColor);

        infoPanel.add(studentNameLabel);
        infoPanel.add(studentIdLabel);
        infoPanel.add(studentEmailLabel);
        infoPanel.add(studentdobLabel);
        infoPanel.add(studentphoneLabel);
        infoPanel.add(studentaddressLabel);

        leftPanel.add(studentImageLabel, BorderLayout.NORTH);
        leftPanel.add(infoPanel, BorderLayout.CENTER);

        // Right Panel - Actions
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(6, 1, 15, 15));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(Color.WHITE);

        // Create buttons with solid colors and clear borders
        viewAttendance = DashBoardButton("View Attendance", new Color(153, 50, 204)); // Blue
        viewGrades = DashBoardButton("Payment", new Color(0, 153, 255)); // Green
        accessMaterials = DashBoardButton("Study Materials", new Color(153, 50, 204)); // Orange
        updateInfo = DashBoardButton("Update Profile", new Color(0, 153, 255)); // Purple
        chat = DashBoardButton("Chat Room", new Color(153, 50, 204)); // Teal
        logout = DashBoardButton("Logout", new Color(244, 67, 54)); // Red

        rightPanel.add(viewAttendance);
        rightPanel.add(viewGrades);
        rightPanel.add(accessMaterials);
        rightPanel.add(updateInfo);
        rightPanel.add(chat);
        rightPanel.add(logout);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
        retrieveStudentData();
        setVisible(true);
    }

    private JLabel createInfoLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return label;
    }

    private StudentCustomButton DashBoardButton(String text, Color bgColor) {
    StudentCustomButton button = new StudentCustomButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int arc = 25;
            ButtonModel model = getModel();
            
            Color currentColor = bgColor;
            if (model.isPressed()) {
                currentColor = bgColor.darker();
            } else if (model.isRollover()) {
                currentColor = bgColor.brighter();
            }

            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            // Paint text
            super.paintComponent(g2);
            g2.dispose();
        }
    };
    
    button.setFont(new Font("Segoe UI", Font.BOLD, 18));
    button.setBackground(bgColor); // Set base background color
    button.setForeground(Color.WHITE);
    button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
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
                
                name = fullName;
                sroll = String.valueOf(passroll);
                
                // Set the text data to the labels
                studentNameLabel.setText("Name: " + fullName);
                studentIdLabel.setText("Roll:" + roll);
                studentEmailLabel.setText("Email: " + email);
                studentdobLabel.setText("Date of Birth: " + dob);
                studentphoneLabel.setText("Phone No: " + phone);
                studentaddressLabel.setText("Address: " + address);

                // If image exists, set it in the JLabel
                if (imageBytes != null) {
                    InputStream is = new ByteArrayInputStream(imageBytes);
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(is.readAllBytes()).getImage()
                            .getScaledInstance(200, 200, Image.SCALE_SMOOTH));  
                    studentImageLabel.setIcon(imageIcon);
                    studentImageLabel.setText(null);  // Remove default text if image is found
                } else {
                    studentImageLabel.setText("No image available.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Student not found!");
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
        if (ae.getSource() == viewAttendance) {
        //    JOptionPane.showMessageDialog(this, "View My Attendance - Feature Coming Soon");
            SwingUtilities.invokeLater(() -> {
                new StudentAttendance();
             });
            
        } else if (ae.getSource() == viewGrades) {
            SwingUtilities.invokeLater(() -> {
                String className = JOptionPane.showInputDialog("Enter the class name:");
                new FeePayment(name,sroll,className);
             });
           // JOptionPane.showMessageDialog(this, "View My Grades - Feature Coming Soon");
        } else if (ae.getSource() == accessMaterials) {
            SwingUtilities.invokeLater(() -> {
                new  StudentMaterials();
             });
           // JOptionPane.showMessageDialog(this, "Access Study Materials - Feature Coming Soon");
        } else if(ae.getSource() == updateInfo) {
            SwingUtilities.invokeLater(() -> {
                new  UpdateStudent(passroll);
             });
        } else if (ae.getSource() == logout) {
          //  JOptionPane.showMessageDialog(this, "Logged Out");
            setVisible(false);
            //new Login("student");
            new MainPage();
        } else if(ae.getSource() == chat){
            SwingUtilities.invokeLater(() -> {
                 new ChatClient();
             });
           
        }
    }

    public static void main(String[] args) {
        new StudentDashboard("ynwa");  // Replace with actual username
    }
}

 


