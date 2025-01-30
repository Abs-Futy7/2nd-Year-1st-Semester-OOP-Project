package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import javax.swing.border.Border;

class SButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public SButton(String text) {
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

class RoundedBorder implements Border {
    private int radius;

    RoundedBorder(int radius) {
        this.radius = radius;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+1, this.radius+1);
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getForeground());
        g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}

public class AddNewStudent extends JFrame implements ActionListener {
    JTextField tfname, tffname,tfroll, tfaddress, tfphone, tfemail, tfbc,tfclass;
 
    JDateChooser dcdob;

    JButton submit, cancel;

    //Random ran = new Random();
    //long first4 = Math.abs((ran.nextLong() % 9000L) + 1000L);

    AddNewStudent() {
        
        setSize(1000, 700);
        setLocation(350, 50);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main container panel
        JPanel container = new JPanel();
        container.setBackground(new Color(235, 245, 245)); // Lighter background
        container.setLayout(null);
        add(container);

        JLabel heading = new JLabel("New Student Details");
        heading.setBounds(300, 30, 500, 50);
        heading.setFont(new Font("Raleway", Font.BOLD, 30));
        heading.setForeground(new Color(70, 130, 180));
        container.add(heading);

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(50, 120, 150, 30);
        lblname.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblname);

        tfname = new JTextField();
        tfname.setBounds(250, 120, 200, 30);
        tfname.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tfname.setBackground(Color.WHITE);
        container.add(tfname);

        JLabel lblfname = new JLabel("Father's Name");
        lblfname.setBounds(500, 120, 150, 30);
        lblfname.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblfname);

        tffname = new JTextField();
        tffname.setBounds(700, 120, 200, 30);
        tffname.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tffname.setBackground(Color.WHITE);
        container.add(tffname);

        JLabel lblrollno = new JLabel("Roll Number");
        lblrollno.setBounds(50, 180, 150, 30);
        lblrollno.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblrollno);

        tfroll = new JTextField();
        tfroll.setBounds(250, 180, 200, 30);
        tfroll.setFont(new Font("Raleway", Font.BOLD, 18));
        tfroll.setForeground(new Color(70, 130, 180));
        container.add(tfroll);

        JLabel lbldob = new JLabel("Date of Birth");
        lbldob.setBounds(500, 180, 150, 30);
        lbldob.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lbldob);

        dcdob = new JDateChooser();
        dcdob.setBounds(700, 180, 200, 30);
        dcdob.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        dcdob.setBackground(Color.WHITE);
        container.add(dcdob);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(50, 240, 150, 30);
        lbladdress.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(250, 240, 200, 30);
        tfaddress.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tfaddress.setBackground(Color.WHITE);
        container.add(tfaddress);

        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(500, 240, 150, 30);
        lblphone.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(700, 240, 200, 30);
        tfphone.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tfphone.setBackground(Color.WHITE);
        container.add(tfphone);

        JLabel lblemail = new JLabel("Email ID");
        lblemail.setBounds(50, 300, 150, 30);
        lblemail.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(250, 300, 200, 30);
        tfemail.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tfemail.setBackground(Color.WHITE);
        container.add(tfemail);

        JLabel lblbc = new JLabel("Birth Certificate No");
        lblbc.setBounds(500, 300, 200, 30);
        lblbc.setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblbc);
        

        tfbc = new JTextField();
        tfbc.setBounds(700, 300, 200, 30);
        tfbc.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tfbc.setBackground(Color.WHITE);
        container.add(tfbc);
        
        JLabel lblclass = new JLabel("Class");
        lblclass .setBounds(50, 350, 150, 30);
        lblclass .setFont(new Font("Raleway", Font.BOLD, 18));
        container.add(lblclass);
        
        tfclass = new JTextField();
        tfclass.setBounds(250, 350, 200, 30);
        tfclass.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        tfclass.setBackground(Color.WHITE);
        container.add(tfclass);

        submit = new SButton("Submit");
        submit.setBounds(300, 450, 150, 40);
        submit.setBackground(new Color(70, 130, 180));
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Raleway", Font.BOLD, 18));
        submit.setFocusPainted(false);
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submit.addActionListener(this);
        container.add(submit);

        cancel = new SButton("Cancel");
        cancel.setBounds(500, 450, 150, 40);
        cancel.setBackground(new Color(220, 53, 69));
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Raleway", Font.BOLD, 18));
        cancel.setFocusPainted(false);
        cancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancel.addActionListener(this);
        container.add(cancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            JOptionPane.showMessageDialog(this, "Details Submitted!");

            String name = tfname.getText();
            String fname = tffname.getText();
            String rolltext = tfroll.getText();
            
            String dob = ((JTextField) dcdob.getDateEditor().getUiComponent()).getText();
            
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String bc = tfbc.getText();
            String cl = tfclass.getText();

            if (name.isEmpty() || fname.isEmpty() || email.isEmpty() || rolltext.isEmpty() || dob.isEmpty() || bc.isEmpty() || address.isEmpty() || phone.isEmpty() || cl.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill out all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int roll = Integer.parseInt(rolltext);

            String query = "INSERT INTO student (name,fname, rollno, dob, address, phone, email, bc,class) VALUES ('"
                    + name + "', '" + fname + "', " + roll + ", '" + dob + "', '" + address + "', '" + phone + "', '" + email + "', '" + bc + "','" + cl + "')";

            try {
                Connect c = new Connect();
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(this, "Student add Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddNewStudent();
    }
}

