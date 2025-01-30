package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.border.Border;

class TeacherButton extends JButton {
    private Color hoverColor = new Color(0, 153, 255);
    private Color pressColor = new Color(0, 51, 153);
    
    public TeacherButton(String text) {
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

 
public class UpdateTeacher extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfid, tfdob, tfaddress, tfphone, tfemail, tfnid;
    JButton submit, cancel;
    
    int id;
    String Username;
  
    UpdateTeacher(int id) {
        this.id = id;
        setSize(900, 700);
        setLocation(350, 50);
        setLayout(null);

        // Set background color
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        JLabel heading = new JLabel("Update Teacher Details");
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

        JLabel lblid = new JLabel("ID Number");
        lblid .setBounds(50, 200, 200, 30);
        lblid .setFont(new Font("Raleway", Font.BOLD, 20));
        lblid .setForeground(new Color(0, 0, 128));
        add(lblid);

        tfid = new JTextField();
        tfid.setBounds(200, 200, 150, 30);
        tfid.setEditable(false); // Make this field non-editable
        tfid.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfid);

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

        JLabel lblnid= new JLabel("NID Card No");
        lblnid.setBounds(400, 300, 200, 30);
        lblnid.setFont(new Font("Raleway", Font.BOLD, 20));
        lblnid.setForeground(new Color(0, 0, 128));
        add(lblnid);

        tfnid = new JTextField();
        tfnid.setBounds(600, 300, 150, 30);
        tfnid.setFont(new Font("Raleway", Font.PLAIN, 18));
        add(tfnid);

        try {
            Connect c = new Connect();
            String query = "SELECT * FROM teacher WHERE id = ?";
            PreparedStatement stmt = c.c.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            //System.out.println(id);

            if (rs.next()) {
                tfname.setText(rs.getString("name"));
                tffname.setText(rs.getString("fname"));
                tfid.setText(rs.getString("id"));
                tfdob.setText(rs.getString("dob"));
                tfaddress.setText(rs.getString("address"));
                tfphone.setText(rs.getString("phone"));
                tfemail.setText(rs.getString("email"));
                tfnid.setText(rs.getString("nid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            Connect c = new Connect();
            String query = "SELECT * FROM register WHERE Roll = ?";
            PreparedStatement stmt = c.c.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            

            if (rs.next()) {
                Username = rs.getString("username");
             //   System.out.println(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        submit = new TeacherButton("Update");
        submit.setBounds(250, 500, 120, 40);
        submit.setBackground(new Color(0, 128, 0)); // Green background
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Raleway", Font.BOLD, 18));
        submit.addActionListener(this);
        add(submit);

        cancel = new TeacherButton("Cancel");
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
            String id = tfid.getText();
            String name = tfname.getText();
            String fname = tffname.getText();
            String dob = tfdob.getText();
            String address = tfaddress.getText();
            String phone = tfphone.getText();
            String email = tfemail.getText();
            String nid = tfnid.getText();
            
             
            try {
                String query = "UPDATE teacher SET name=?, fname=?, dob=?, address=?, phone=?, email=?, nid=? WHERE id=?";
                Connect con = new Connect();
                PreparedStatement stmt = con.c.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, fname);
                stmt.setString(3, dob);
                stmt.setString(4, address);
                stmt.setString(5, phone);
                stmt.setString(6, email);
                stmt.setString(7, nid);
                stmt.setString(8, id);
                
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                  //   JOptionPane.showMessageDialog(null, "Teacher Details Updated Successfully");
                    setVisible(false);
                }
            } catch (Exception e) {
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
                stmt.setString(6, nid);
                stmt.setString(7, id);
                
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                   //  JOptionPane.showMessageDialog(null, "Teacher Details Updated Successfully");
                    setVisible(false);
                    new TeacherDashboard(Username);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateTeacher(11117584);
    }
}