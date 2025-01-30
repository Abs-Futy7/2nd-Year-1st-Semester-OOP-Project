package school.management.system;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;


// CustomButton Class
class AdminButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public AdminButton(String text) {
        super(text);
        if("Logout".equals(text)|| "Delete Students".equals(text)|| "Delete Teachers".equals(text)){
            hoverColor = new Color(255, 99, 71);  
            pressColor = new Color(178, 34, 34);
            setBackground(new Color(255, 0, 0));
        }else{
            setBackground(new Color(0, 102, 204));
        }
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Raleway", Font.BOLD, 16));
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
                if("Logout".equals(text)|| "Delete Students".equals(text) || "Delete Teachers".equals(text)){
             
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


// AdminDashboard Class
public class AdminDashboard extends JFrame implements ActionListener {
    AdminButton manageStudents, manageTeachers, manageClasses, payment, logout;
    JTable dataTable;
    
    int st,te,cl;

    AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Gradient Header
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(0, 102, 204);
                Color color2 = new Color(0, 153, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };
        headerPanel.setPreferredSize(new Dimension(800, 100));
        JLabel headerLabel = new JLabel("Admin Dashboard", JLabel.CENTER);
        headerLabel.setFont(new Font("Raleway", Font.BOLD, 36));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Modern Sidebar
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(new Color(245, 245, 245));
        sidebarPanel.setPreferredSize(new Dimension(250, 80));
        sidebarPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setLayout(new GridLayout(5, 1, 0, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        manageStudents = new AdminButton("Manage Students");
        manageTeachers = new AdminButton("Manage Teachers");
        manageClasses = new AdminButton("Manage Classes");
        payment = new AdminButton("Payment");
        logout = new AdminButton("Logout");
         

        for(JButton btn : new JButton[]{manageStudents, manageTeachers, manageClasses, payment, logout}) {
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        sidebarPanel.add(buttonPanel, BorderLayout.CENTER);
        add(sidebarPanel, BorderLayout.WEST);
        
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Statistics Cards Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(Color.WHITE);

        // Create Statistics Cards
        getdata();
        statsPanel.add(createStatCard("Total Students", st, new Color(70, 130, 180),new Color(135,206,250)));
        statsPanel.add(createStatCard("Total Teachers", te, new Color(60, 179, 113),new Color(144,238,144)));
        statsPanel.add(createStatCard("Total Classes", cl, new Color(255, 165, 0),new Color(255,215,0)));

        mainContentPanel.add(statsPanel, BorderLayout.CENTER);
        add(mainContentPanel, BorderLayout.CENTER);

        setVisible(true);


       
         
    }
    
    public void getdata(){
        Connect c = new Connect(); // Use your connection class
        String query1 = "SELECT COUNT(*) FROM teacher"; // Replace with your table name
        String query2 = "SELECT COUNT(*) FROM student"; 
        String query3 = "SELECT COUNT(DISTINCT class, date) AS distinct_pairs_count FROM attendance_records;"; 

        try {
            PreparedStatement stmt = c.c.prepareStatement(query1);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                te = rs.getInt(1); // Retrieve the count
                System.out.println("Total rows: " + te);
            }

            // Close resources
            rs.close();
            stmt.close();
            c.s.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement stmt = c.c.prepareStatement(query2);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                st = rs.getInt(1); // Retrieve the count
                System.out.println("Total rows: " + st);
            }

            // Close resources
            rs.close();
            stmt.close();
            c.s.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement stmt = c.c.prepareStatement(query3);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cl = rs.getInt(1); // Retrieve the count
                System.out.println("Total rows: " + cl);
            }

            // Close resources
            rs.close();
            stmt.close();
            c.s.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private JPanel createStatCard(String title, int value, Color gradientStart, Color gradientEnd) {
        // Custom panel for gradient background
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, getWidth(), getHeight(), gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Title Label
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // White text for better contrast
        card.add(titleLabel, BorderLayout.NORTH);

        // Value Label
        String str = String.valueOf(value);
        JLabel valueLabel = new JLabel(str, JLabel.CENTER);
        valueLabel.setFont(new Font("Raleway", Font.BOLD, 36));
        valueLabel.setForeground(Color.WHITE); // White text for better contrast
        card.add(valueLabel, BorderLayout.CENTER);

        // Bottom Decoration
        JPanel decor = new JPanel();
        decor.setBackground(Color.WHITE); // Bottom border or decoration
        decor.setPreferredSize(new Dimension(50, 4));
        card.add(decor, BorderLayout.SOUTH);

        return card;
    }

    // Action Performed for Button Clicks
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == manageStudents) {
            new ManagePage("Students");
        } else if (ae.getSource() == manageTeachers) {
            new ManagePage("Teachers");
        } else if (ae.getSource() == manageClasses) {
            //System.out.println("Classssssss");
           // new AssignClass();
           
           new ManageClasses();
        } else if (ae.getSource() == payment) {
            SwingUtilities.invokeLater(() -> new PaymentForm());
        } else if (ae.getSource() == logout) {
            new MainPage();
            setVisible(false);
        }
    }

    // ManagePage Class for Students or Teachers
static class ManagePage extends JFrame implements ActionListener {
    String type;
    AdminButton updateInfo, totalInfo, addnewstudent,DeleteUser;

    ManagePage(String type) {
        this.type = type;
        setTitle("Manage " + type);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main layout
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE); // Background color for the frame

        // Header with styled label
        JLabel label = new JLabel("Manage " + type, JLabel.CENTER);
        label.setFont(new Font("Raleway", Font.BOLD, 24));
        label.setForeground(new Color(52, 152, 219)); // Blue text
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the label
        add(label, BorderLayout.NORTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Grid for buttons
        buttonPanel.setBackground(Color.WHITE); // Background for button panel
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Buttons
        addnewstudent = new AdminButton("Add New " + type);
        updateInfo = new AdminButton("Update Info");
        totalInfo = new AdminButton(type + " Info.");
        DeleteUser = new AdminButton("Delete "+type);
        addnewstudent.addActionListener(this);
        updateInfo.addActionListener(this);
        totalInfo.addActionListener(this);
        DeleteUser.addActionListener(this);

        // Add buttons to the panel
        buttonPanel.add(addnewstudent);
        buttonPanel.add(updateInfo);
        buttonPanel.add(totalInfo);
        buttonPanel.add(DeleteUser);

        add(buttonPanel, BorderLayout.CENTER);

        

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateInfo) {
            String input = JOptionPane.showInputDialog(this, "Enter ID or Roll:", "Update Info", JOptionPane.PLAIN_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
               // JOptionPane.showMessageDialog(this, "Proceeding to update info for: " + input, "Update Info", JOptionPane.INFORMATION_MESSAGE);
                int n = Integer.parseInt(input);
                if ("Students".equals(type)) {
                    new UpdateStudent(n);
                } else if ("Teachers".equals(type)) {
                    new UpdateTeacher(n);
                }
            } else {
                JOptionPane.showMessageDialog(this, "ID or Roll cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == totalInfo) {
            if ("Students".equals(type)) {
                new ViewStudentDetails();
            } else if ("Teachers".equals(type)) {
                new ViewTeacherDetails();
            }
        } else if (e.getSource() == addnewstudent) {
            if ("Students".equals(type)) {
                new AddNewStudent();
            } else if ("Teachers".equals(type)) {
                new AddNewTeacher();
            }
        }else if (e.getSource() == DeleteUser ) {
            String input = JOptionPane.showInputDialog(this, "Enter ID or Roll:", "Delete Info", JOptionPane.PLAIN_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
               // JOptionPane.showMessageDialog(this, "Proceeding to update info for: " + input, "Update Info", JOptionPane.INFORMATION_MESSAGE);
                int n = Integer.parseInt(input);
                if ("Students".equals(type)) {
                    
                    try{
                        String query = "DELETE from student Where rollno = ?";
                        Connect con = new Connect();
                        PreparedStatement stmt = con.c.prepareStatement(query);
                        stmt.setInt(1, n);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Students Delete Successfully");
                    }catch (SQLException ex) {
                       ex.printStackTrace();
                      }
                } else if ("Teachers".equals(type)) {
                    
                    try{
                        String query1 = "DELETE from teacher Where id = ?";
                        Connect con = new Connect();
                        PreparedStatement stmt = con.c.prepareStatement(query1);
                        stmt.setInt(1, n);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Teacher Delete Successfully");
                    }catch (SQLException ex) {
                       ex.printStackTrace();
                      }
                }
                
            } else {
                JOptionPane.showMessageDialog(this, "ID or Roll cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

   
     
    static class GenerateReports extends JFrame {

        GenerateReports() {
            setTitle("Generate Reports");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        }
    }
 

    // Removed 'static' modifier and implemented ActionListener
    class ManageClasses extends JFrame implements ActionListener {
    
    ManageClasses() {
        setTitle("Class Management");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel title = new JLabel("Class Management", SwingConstants.CENTER);
        title.setFont(new Font("Raleway", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        headerPanel.add(title);
        add(headerPanel, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        buttonPanel.setBackground(Color.WHITE);

        // Assign Class Button
        JButton assignButton = new AdminButton("Assign Class");
          
         
        styleButton(assignButton, new Color(76, 175, 80));
        assignButton.addActionListener(this); // Added action listener
        
        // View Classes Button
        JButton viewButton = new AdminButton("Assigned Classes");
        styleButton(viewButton, new Color(33, 150, 243));
        viewButton.addActionListener(this); // Added action listener

        buttonPanel.add(assignButton);
        buttonPanel.add(viewButton);

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Raleway", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // Implement actionPerformed to handle button clicks
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Assign Class")) {
            // Open Assign Class interface
            //JOptionPane.showMessageDialog(this, "Opening Assign Class...");
            // Add your implementation here
            new AssignClass();
        } else if (ae.getActionCommand().equals("Assigned Classes")) {
            // Open View Classes interface
            // JOptionPane.showMessageDialog(this, "Opening View Classes...");
            // Add your implementation here
            new TeacherViewClasses();
        }
    }

    
     
}

    
    
    // Main Method
    public static void main(String[] args) {
        new AdminDashboard();
    }
}
