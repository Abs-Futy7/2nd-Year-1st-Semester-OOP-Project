package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.border.EmptyBorder;
 
import java.awt.geom.RoundRectangle2D;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
class MainPageButton extends JButton {
    private Color defaultColor = new Color(0, 122, 255); // Modern blue
    private Color hoverColor = new Color(0, 90, 200);
    private Color pressedColor = new Color(0, 60, 150);
    private int cornerRadius = 8;

    public MainPageButton(String text) {
        super(text);
        setUIProperties();
        addInteractivity();
    }

    private void setUIProperties() {
        // Basic styling
        setForeground(Color.WHITE);
        setFont(new Font("Raleway", Font.BOLD, 16));
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
public class MainPage extends JFrame implements ActionListener {

    MainPageButton loginButton, chatButton, aboutUsButton, registerButton;

    MainPage() {
        // Frame setup
        setTitle("School Management System");
        setSize(1200, 600);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background image
        ImageIcon bg = new ImageIcon(getClass().getResource("/assets/bg1.png"));
        Image i2 = bg.getImage().getScaledInstance(1200, 600, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel background = new JLabel(i3);
        background.setBounds(0, 0, 1200, 600);
        add(background);

        // School logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/DU_Scholars_Academy.png"));
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(resizedLogo);
        logoLabel.setBounds(0, -50, 250, 250);
        background.add(logoLabel);

        // Main Heading
        JLabel mainHeading = new JLabel("Welcome to DU Scholars Academy", SwingConstants.CENTER);
        mainHeading.setFont(new Font("Raleway", Font.BOLD, 48));
        mainHeading.setForeground(new Color(211, 230, 253));
        mainHeading.setBounds(150, 200, 900, 60);
        background.add(mainHeading);

        // Subheading
        JLabel subHeading = new JLabel("Empowering Students to Learn, Grow, and Succeed", SwingConstants.CENTER);
        subHeading.setFont(new Font("Raleway", Font.PLAIN, 24));
        subHeading.setForeground(new Color(60, 141, 199));
        subHeading.setBounds(300, 270, 600, 50);
        background.add(subHeading);

        

        // Panel for other buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(650, 50, 500, 40); // Position at the bottom
        buttonPanel.setOpaque(false); // Transparent panel
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 0)); // 1 row, 3 columns with spacing

        // Add other buttons to the panel
        buttonPanel.add(createChatButton());
        buttonPanel.add(createAboutUsButton());
        buttonPanel.add(createLoginButton());
        buttonPanel.add(createRegisterButton());

        // Add button panel to the background
        background.add(buttonPanel);

        // Make the frame visible
        setVisible(true);
    }
   
    private JButton createLoginButton(){
        
        loginButton = new MainPageButton("Login");
        loginButton.addActionListener(this);
        return loginButton;
    }

    private JButton createChatButton() {
        chatButton =new MainPageButton("FAQ");
        chatButton.addActionListener(this);
        return chatButton;
    }

    private JButton createAboutUsButton() {
        aboutUsButton = new MainPageButton("About Us");
        aboutUsButton.addActionListener(this);
        return aboutUsButton;
    }

    private JButton createRegisterButton() {
        registerButton =new MainPageButton("Register");
        registerButton.addActionListener(this);
        return registerButton;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            new LoginSelectionPage();
           // setVisible(false);
        } else if (e.getSource() == chatButton) {
            Thread serverThread = new Thread(() -> Server.startServer());
            serverThread.start();
            Client.startClient();
        } else if (e.getSource() == aboutUsButton) {
            //JOptionPane.showMessageDialog(this, "Navigating to About Us...");
            new AboutUs();
        } else if (e.getSource() == registerButton) {
            //JOptionPane.showMessageDialog(this, "Navigating to Register...");
            new Register("mainpage");
        }
    }

    public static void main(String[] args) {
        new MainPage();
    }
}