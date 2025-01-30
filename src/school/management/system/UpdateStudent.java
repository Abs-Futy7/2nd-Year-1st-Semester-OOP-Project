package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.Border;

class UpdateButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public UpdateButton(String text) {
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


public class UpdateStudent extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfrollno, tfdob, tfaddress, tfphone, tfemail, tfbc;
    JButton submit, cancel;
    
    int roll;
    
    String Username;

    UpdateStudent(int roll) {
        
        this.roll = roll;
        
        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);

        // Set background color
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel heading = new JLabel("Update Student Details");
        heading.setBounds(250, 10, 500, 50);
        heading.setFont(new Font("Raleway", Font.BOLD, 35));
        heading.setForeground(new Color(25, 25, 112)); // Dark blue text
        add(heading);
 

        JLabel lblname = new JLabel("Name");
        lblname.setBounds(50, 150, 100, 30);
        lblname.setFont(new Font("Raleway", Font.BOLD, 20));
        lblname.setForeground(new Color(0, 0, 128));
        add(lblname);

        tfname = new JTextField();
        tfname.setBounds(200, 150, 150, 30);
        tfname.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfname);

        JLabel lblfname = new JLabel("Father's Name");
        lblfname.setBounds(400, 150, 200, 30);
        lblfname.setFont(new Font("Raleway", Font.BOLD, 20));
        lblfname.setForeground(new Color(0, 0, 128));
        add(lblfname);

        tffname = new JTextField();
        tffname.setBounds(600, 150, 150, 30);
        tffname.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tffname);

        JLabel lblrollno = new JLabel("Roll Number");
        lblrollno.setBounds(50, 200, 200, 30);
        lblrollno.setFont(new Font("Raleway", Font.BOLD, 20));
        lblrollno.setForeground(new Color(0, 0, 128));
        add(lblrollno);

        tfrollno = new JTextField();
        tfrollno.setBounds(200, 200, 150, 30);
        tfrollno.setEditable(false); // Make this field non-editable
        tfrollno.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfrollno);

        JLabel lbldob = new JLabel("Date of Birth");
        lbldob.setBounds(400, 200, 200, 30);
        lbldob.setFont(new Font("Raleway", Font.BOLD, 20));
        lbldob.setForeground(new Color(0, 0, 128));
        add(lbldob);

        tfdob = new JTextField();
        tfdob.setBounds(600, 200, 150, 30);
        tfdob.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfdob);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(50, 250, 200, 30);
        lbladdress.setFont(new Font("Raleway", Font.BOLD, 20));
        lbladdress.setForeground(new Color(0, 0, 128));
        add(lbladdress);

        tfaddress = new JTextField();
        tfaddress.setBounds(200, 250, 150, 30);
        tfaddress.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfaddress);

        JLabel lblphone = new JLabel("Phone");
        lblphone.setBounds(400, 250, 200, 30);
        lblphone.setFont(new Font("Raleway", Font.BOLD, 20));
        lblphone.setForeground(new Color(0, 0, 128));
        add(lblphone);

        tfphone = new JTextField();
        tfphone.setBounds(600, 250, 150, 30);
        tfphone.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfphone);

        JLabel lblemail = new JLabel("Email Id");
        lblemail.setBounds(50, 300, 200, 30);
        lblemail.setFont(new Font("Raleway", Font.BOLD, 20));
        lblemail.setForeground(new Color(0, 0, 128));
        add(lblemail);

        tfemail = new JTextField();
        tfemail.setBounds(200, 300, 150, 30);
        tfemail.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfemail);

        JLabel lblbc = new JLabel("Birth Certificate No");
        lblbc.setBounds(400, 300, 200, 30);
        lblbc.setFont(new Font("Raleway", Font.BOLD, 20));
        lblbc.setForeground(new Color(0, 0, 128));
        add(lblbc);

        tfbc = new JTextField();
        tfbc.setBounds(600, 300, 150, 30);
        tfbc.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfbc);

        try {
            Connect c = new Connect();
            String query = "SELECT * FROM student WHERE rollno = ?";
            PreparedStatement stmt = c.c.prepareStatement(query);
            stmt.setInt(1, roll);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                tfrollno.setText(rs.getString("rollno"));
                tfdob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfbc.setText(rs.getString("bc"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
            try {
            Connect c = new Connect();
            String query = "SELECT * FROM register WHERE Roll = ?";
            PreparedStatement stmt = c.c.prepareStatement(query);
            stmt.setInt(1, roll);
            ResultSet rs = stmt.executeQuery();
            
            

            if (rs.next()) {
                Username = rs.getString("username");
             //   System.out.println(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        submit = new UpdateButton("Update");
        submit.setBounds(250, 500, 120, 40);
        submit.setBackground(new Color(0, 128, 0)); // Green background
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Raleway", Font.BOLD, 18));
        submit.addActionListener(this);
        add(submit);

        cancel = new UpdateButton("Cancel");
        cancel.setBounds(450, 500, 120, 40);
        cancel.setBackground(new Color(178, 34, 34)); // Red background
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Raleway", Font.BOLD, 18));
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String rollno = tfrollno.getText();
            String name = tfname.getText();
            String fname = tffname.getText();
            String dob = tfdob.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String bc = tfbc.getText();

            try {
                String query = "UPDATE student SET name=?, fname=?, dob=?, address=?, phone=?, email=?, bc=? WHERE rollno=?";
                Connect con = new Connect();
                PreparedStatement stmt = con.c.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, fname);
                stmt.setString(3, dob);
                stmt.setString(4, address);
                stmt.setString(5, phone);
                stmt.setString(6, email);
                stmt.setString(7, bc);
                stmt.setString(8, rollno);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Student Details Updated Successfully");
                    setVisible(false);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            try {
                String query = "UPDATE register SET FullName=?,dob=?, address=?, phone=?, email=?, bc=? WHERE Roll=?";
                Connect con = new Connect();
                PreparedStatement stmt = con.c.prepareStatement(query);
                stmt.setString(1, name);
              
                stmt.setString(2, dob);
                stmt.setString(3, address);
                stmt.setString(4, phone);
                stmt.setString(5, email);
                stmt.setString(6, bc);
                stmt.setString(7, rollno);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Student Details Updated Successfully");
                    setVisible(false);
                    
                    new StudentDashboard(Username);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            
            
            
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateStudent(21);
    }
}

