package school.management.system;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import java.sql.*;

class RoundedButton extends JButton {
    private int arcWidth = 25;
    private int arcHeight = 25;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Raleway", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(new Color(70, 130, 180));
        setPreferredSize(new Dimension(180, 45));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arcWidth, arcHeight));
        super.paintComponent(g2);
        g2.dispose();
    }
}

public class Login extends JFrame implements ActionListener {
    RoundedButton login, cancel, register, forgetpass;
    JTextField tfusername;
    JPasswordField tfpassword;
    String person;

    Login(String person) {
        this.person = person;
        setTitle("Login - School Management System");
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 248, 255));

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(173, 216, 230);
                Color color2 = new Color(240, 248, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new GridBagLayout());
        add(gradientPanel, BorderLayout.CENTER);

        JLabel heading = new JLabel("Login as " + person);
        heading.setFont(new Font("Raleway", Font.BOLD, 28));
        heading.setForeground(new Color(50, 50, 150));

        JLabel lblusername = new JLabel("Username:");
        lblusername.setFont(new Font("Raleway", Font.BOLD, 18));
        lblusername.setForeground(new Color(70, 70, 70));

        tfusername = new JTextField(20);
        tfusername.setFont(new Font("Raleway", Font.PLAIN, 16));
        tfusername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        JLabel lblpassword = new JLabel("Password:");
        lblpassword.setFont(new Font("Raleway", Font.BOLD, 18));
        lblpassword.setForeground(new Color(70, 70, 70));

        tfpassword = new JPasswordField(20);
        tfpassword.setFont(new Font("Raleway", Font.PLAIN, 16));
        tfpassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));

        login = new RoundedButton("Login");
        cancel = new RoundedButton("Cancel");
        register = new RoundedButton("Register");
        forgetpass = new RoundedButton("Forget Password");

        addButtonEffects(login);
        addButtonEffects(cancel);
        addButtonEffects(register);
        addButtonEffects(forgetpass);

        login.addActionListener(this);
        cancel.addActionListener(this);
        register.addActionListener(this);
        forgetpass.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        // Heading
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gradientPanel.add(heading, gbc);

        // Username Row
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gradientPanel.add(lblusername, gbc);
        gbc.gridx = 1;
        gradientPanel.add(tfusername, gbc);

        // Password Row
        gbc.gridy = 2;
        gbc.gridx = 0;
        gradientPanel.add(lblpassword, gbc);
        gbc.gridx = 1;
        gradientPanel.add(tfpassword, gbc);

        // Login/Cancel Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 30, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(login);
        buttonPanel.add(cancel);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gradientPanel.add(buttonPanel, gbc);

        // Additional Buttons for non-admin
        if (!"admin".equals(person)) {
            JPanel extraButtonPanel = new JPanel(new GridLayout(0, 2, 30, 0));
            extraButtonPanel.setOpaque(false);
            extraButtonPanel.add(register);
            extraButtonPanel.add(forgetpass);

            gbc.gridy = 4;
            gradientPanel.add(extraButtonPanel, gbc);
        }

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addButtonEffects(RoundedButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
                button.setSize(button.getWidth() + 2, button.getHeight() + 2);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
                button.setSize(button.getWidth() - 2, button.getHeight() - 2);
            }

            public void mousePressed(MouseEvent e) {
                button.setBackground(new Color(50, 100, 150));
            }

            public void mouseReleased(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == login) {
            String username = tfusername.getText();
            String password = new String(tfpassword.getPassword());

            if ("admin".equals(person) && "admin".equals(username) && "1234".equals(password)) {
                setVisible(false);
                new AdminDashboard();
                return;
            }

            String query = "SELECT * FROM register WHERE username = '" + username + "' AND password = '" + password + "'";
            try {
                Connect c = new Connect();
                ResultSet rs = c.s.executeQuery(query);

                if (rs.next()) {
                    setVisible(false);
                    if ("teacher".equals(person)) {
                        new TeacherDashboard(username);
                    } else {
                        new StudentDashboard(username);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
                c.s.close();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        else if (ae.getSource() == cancel) {
            dispose();
        } 
        else if (ae.getSource() == register) {
            new Register("student".equals(person) ? "login1" : "login2");
            setVisible(false);
        } 
        else if (ae.getSource() == forgetpass) {
            String prompt = "teacher".equals(person) ? "Enter your ID:" : "Enter your roll number:";
            String input = JOptionPane.showInputDialog(this, prompt, "Password Recovery", JOptionPane.QUESTION_MESSAGE);
            
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int id = Integer.parseInt(input.trim());
                    String query = "SELECT username, password FROM register WHERE Roll = ?";
                    Connect connect = new Connect();
                    PreparedStatement ps = connect.c.prepareStatement(query);
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        String msg = "Username: " + rs.getString("username") + "\nPassword: " + rs.getString("password");
                        JOptionPane.showMessageDialog(this, msg, "Account Details", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "No user found", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException | SQLException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login("student"));
    }
}