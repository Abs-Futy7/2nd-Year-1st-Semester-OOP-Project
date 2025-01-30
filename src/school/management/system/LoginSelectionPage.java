package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;

class SectionButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public SectionButton(String text) {
        super(text);
        if("Back".equals(text)){
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
                if("Back".equals(text)){
             
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

 
public class LoginSelectionPage extends JFrame implements ActionListener {

    SectionButton adminLogin, teacherLogin, studentLogin, backButton;

    LoginSelectionPage() {
        // Frame setup
        setTitle("Login Selection");
        setSize(800, 400);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Set background color
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(new Color(60, 141, 199)); // Background color
        backgroundPanel.setBounds(0, 0, 800, 400);
        backgroundPanel.setLayout(null); // Use null layout for manual positioning
        add(backgroundPanel);

        // Main Heading
        JLabel heading = new JLabel("How do you want to login?", SwingConstants.CENTER);
        heading.setFont(new Font("Raleway", Font.BOLD, 28));
        heading.setForeground(Color.WHITE); // White text color
        heading.setBounds(100, 30, 600, 50); // Adjusted width and positioning
        backgroundPanel.add(heading);

        // Buttons for login options
        adminLogin =new SectionButton("Admin Login");
        adminLogin.setBounds(300, 100, 200, 40);
        adminLogin.addActionListener(this);
        backgroundPanel.add(adminLogin);

        teacherLogin =new SectionButton("Teacher Login");
        teacherLogin.setBounds(300, 160, 200, 40);
        teacherLogin.addActionListener(this);
        backgroundPanel.add(teacherLogin);

        studentLogin =new SectionButton("Student Login");
        studentLogin.setBounds(300, 220, 200, 40);
        studentLogin.addActionListener(this);
        backgroundPanel.add(studentLogin);

        // Back Button
        backButton = new SectionButton("Back");
        backButton.setBounds(300, 280, 200, 40);
        backButton.addActionListener(this);
        backgroundPanel.add(backButton);

        // Make the frame visible
        setVisible(true);
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == adminLogin) {
            new Login("admin");
            setVisible(false);
        } else if (e.getSource() == teacherLogin) {
            new Login("teacher");
            setVisible(false);
        } else if (e.getSource() == studentLogin) {
            new Login("student");
            setVisible(false);
        } else if (e.getSource() == backButton) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
           // new MainPage();
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new LoginSelectionPage();
    }
}
