package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.swing.border.EmptyBorder;

class PaymentButton extends JButton {
    private Color defaultColor = new Color(0, 122, 255); // Modern blue
    private Color hoverColor = new Color(0, 90, 200);
    private Color pressedColor = new Color(0, 60, 150);
    private int cornerRadius = 8;

    public PaymentButton(String text) {
        super(text);
        setUIProperties();
        addInteractivity();
    }

    private void setUIProperties() {
        // Basic styling
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
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

public class FeePayment extends JFrame {
    private JTextField studentNameField, studentRollField, studentClassField, totalDueField, givenAmountField, remainingAmountField;
    private PaymentButton calculateButton, printReceiptButton, cancelButton, payButton;
    private JLabel statusLabel;

    public FeePayment(String studentName, String studentRoll, String studentClass) {
        setTitle("Student Fee Payment");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(211, 230, 253));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 78, 254));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        JLabel titleLabel = new JLabel("Student Fee Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(211, 230, 253));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel studentNameLabel = new JLabel("Student Name:");
        studentNameLabel.setFont(new Font("Raleway", Font.PLAIN, 16));
        studentNameLabel.setForeground(new Color(60, 141, 199));
        mainPanel.add(studentNameLabel, gbc);

        gbc.gridx = 1;
        studentNameField = new JTextField(studentName);
        studentNameField.setFont(new Font("Raleway", Font.PLAIN, 16));
        studentNameField.setEditable(false);
        studentNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(studentNameField, gbc);

        // Student Roll Number
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel studentRollLabel = new JLabel("Roll Number:");
        studentRollLabel.setFont(new Font("Raleway", Font.PLAIN, 16));
        studentRollLabel.setForeground(new Color(60, 141, 199));
        mainPanel.add(studentRollLabel, gbc);

        gbc.gridx = 1;
        studentRollField = new JTextField(studentRoll);
        studentRollField.setFont(new Font("Raleway", Font.PLAIN, 16));
        studentRollField.setEditable(false);
        studentRollField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(studentRollField, gbc);

        // Student Class
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel studentClassLabel = new JLabel("Class:");
        studentClassLabel.setFont(new Font("Raleway", Font.PLAIN, 16));
        studentClassLabel.setForeground(new Color(60, 141, 199));
        mainPanel.add(studentClassLabel, gbc);

        gbc.gridx = 1;
        studentClassField = new JTextField(studentClass);
        studentClassField.setFont(new Font("Raleway", Font.PLAIN, 16));
        studentClassField.setEditable(false);
        studentClassField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(studentClassField, gbc);

        // Total Due Amount
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel totalDueLabel = new JLabel("Total Due Amount:");
        totalDueLabel.setFont(new Font("Raleway", Font.PLAIN, 16));
        totalDueLabel.setForeground(new Color(60, 141, 199));
        mainPanel.add(totalDueLabel, gbc);

        gbc.gridx = 1;
        totalDueField = new JTextField();
        totalDueField.setFont(new Font("Raleway", Font.PLAIN, 16));
        totalDueField.setEditable(false);
        totalDueField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(totalDueField, gbc);

        // Fetch total_amount from paymentfields table
        Connect connect = new Connect();
        try {
            String query = "SELECT total_amount FROM paymentfields WHERE id = 1";
            ResultSet rs = connect.s.executeQuery(query);
            if (rs.next()) {
                double totalAmount = rs.getDouble("total_amount");
                totalDueField.setText(String.format("%.2f", totalAmount));
            } else {
                totalDueField.setText("0.00");
                JOptionPane.showMessageDialog(this, "Total amount not found for id 1.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            totalDueField.setText("0.00");
            JOptionPane.showMessageDialog(this, "Error fetching total amount: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Given Amount
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel givenAmountLabel = new JLabel("Given Amount:");
        givenAmountLabel.setFont(new Font("Raleway", Font.PLAIN, 16));
        givenAmountLabel.setForeground(new Color(60, 141, 199));
        mainPanel.add(givenAmountLabel, gbc);

        gbc.gridx = 1;
        givenAmountField = new JTextField();
        givenAmountField.setFont(new Font("Raleway", Font.PLAIN, 16));
        givenAmountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(givenAmountField, gbc);

        // Remaining Amount
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel remainingAmountLabel = new JLabel("Remaining Amount:");
        remainingAmountLabel.setFont(new Font("Raleway", Font.PLAIN, 16));
        remainingAmountLabel.setForeground(new Color(60, 141, 199));
        mainPanel.add(remainingAmountLabel, gbc);

        gbc.gridx = 1;
        remainingAmountField = new JTextField();
        remainingAmountField.setFont(new Font("Raleway", Font.PLAIN, 16));
        remainingAmountField.setEditable(false);
        remainingAmountField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(remainingAmountField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(211, 230, 253));

        calculateButton = new PaymentButton("Calculate");
        payButton = new PaymentButton("Pay");
        printReceiptButton = new PaymentButton("Print Receipt");
        cancelButton = new PaymentButton("Cancel");

        buttonPanel.add(calculateButton);
        buttonPanel.add(payButton);
        buttonPanel.add(printReceiptButton);
        buttonPanel.add(cancelButton);

        calculateButton.addActionListener(e -> calculateRemainingAmount());
        payButton.addActionListener(e -> processPayment());
        printReceiptButton.addActionListener(e -> printReceipt());
        cancelButton.addActionListener(e -> dispose());

        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Raleway", Font.BOLD, 14));
        statusLabel.setForeground(new Color(34, 139, 34));
        statusLabel.setVisible(false);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        setVisible(true);
    }

    

    private void calculateRemainingAmount() {
        try {
            double totalDue = Double.parseDouble(totalDueField.getText());
            double givenAmount = Double.parseDouble(givenAmountField.getText());
            double remainingAmount = totalDue - givenAmount;

            if (givenAmount < 0 || givenAmount > totalDue) {
                JOptionPane.showMessageDialog(this, "Invalid payment amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            remainingAmountField.setText(String.format("%.2f", remainingAmount));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPayment() {
        try {
            double totalDue = Double.parseDouble(totalDueField.getText());
            double givenAmount = Double.parseDouble(givenAmountField.getText());
            double remainingAmount = totalDue - givenAmount;

            if (givenAmount < 0 || givenAmount > totalDue) {
                JOptionPane.showMessageDialog(this, "Invalid payment amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            remainingAmountField.setText(String.format("%.2f", remainingAmount));
            totalDueField.setText(String.format("%.2f", remainingAmount));

            String name = studentNameField.getText();
            String rollText = studentRollField.getText().replaceAll("\\D", "");
            if (rollText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid roll number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int roll = Integer.parseInt(rollText);
            double amount = givenAmount;
            double due = remainingAmount;
            java.sql.Date paymentDate = new java.sql.Date(System.currentTimeMillis());

            Connect connect = new Connect();
            String sql = "INSERT INTO payment (name, roll, amount, due, payment_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connect.c.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, roll);
            pstmt.setDouble(3, amount);
            pstmt.setDouble(4, due);
            pstmt.setDate(5, paymentDate);
            pstmt.executeUpdate();
            pstmt.close();

            JOptionPane.showMessageDialog(this, "Payment successful and saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving payment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printReceipt() {
        try {
            String studentName = studentNameField.getText();
            String studentRoll = studentRollField.getText();
            String studentClass = studentClassField.getText();
            double totalDue = Double.parseDouble(totalDueField.getText());
            double givenAmount = Double.parseDouble(givenAmountField.getText());
            double remainingAmount = Double.parseDouble(remainingAmountField.getText());

            Document document = new Document();
            String filename = studentName.replaceAll(" ", "_") + "_" + studentRoll + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            
            System.out.println(studentName);

            document.add(new Paragraph("Fee Payment Receipt"));
            document.add(new Paragraph("----------------------------------"));
            document.add(new Paragraph("Student Name: " + studentName));
            document.add(new Paragraph("Roll Number: " + studentRoll));
            document.add(new Paragraph("Class: " + studentClass));
            document.add(new Paragraph("Total Due Amount: " + String.format("%.2f", totalDue)));
            document.add(new Paragraph("Given Amount: " + String.format("%.2f", givenAmount)));
            document.add(new Paragraph("Remaining Amount: " + String.format("%.2f", remainingAmount)));
            document.add(new Paragraph("----------------------------------"));
            document.add(new Paragraph("Thank you for your payment!"));

            document.close();

            JOptionPane.showMessageDialog(this, "Receipt printed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            statusLabel.setText("Receipt printed successfully!");
            statusLabel.setVisible(true);

            File file = new File(filename);
            if (file.exists()) {
                Desktop.getDesktop().open(file);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error printing receipt: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeePayment("Abs Futy7", "101", "10A"));
    }
}