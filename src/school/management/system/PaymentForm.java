 
package school.management.system;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;

class SetButton extends JButton {
    
    public SetButton(String text) {
        super(text);
        setCustomDesign();
    }

    private void setCustomDesign() {
        // Set button styling
        setBackground(new Color(59, 89, 182));  // Dark blue color
        setForeground(Color.WHITE);
        setFont(new Font("Raleway", Font.BOLD, 14));
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effects
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(78, 113, 214));  // Lighter blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(59, 89, 182));  // Original color
            }
        });
    }
}

public class PaymentForm extends JFrame implements ActionListener {
    private JTextField examFeeField, labFeeField, sessionFeeField, otherFeeField;
    private SetButton  submitButton;
    private Connect connect;

    public PaymentForm() {
        connect = new Connect();
        createUI();
    }

    private void createUI() {
        setTitle("Payment Set Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create components
        examFeeField = new JTextField();
        labFeeField = new JTextField();
        sessionFeeField = new JTextField();
        otherFeeField = new JTextField();
        submitButton = new SetButton ("Submit");

        // Add components to panel
        panel.add(new JLabel("Exam Fee:"));
        panel.add(examFeeField);
        panel.add(new JLabel("Lab Fee:"));
        panel.add(labFeeField);
        panel.add(new JLabel("Session Fee:"));
        panel.add(sessionFeeField);
        panel.add(new JLabel("Other Fee:"));
        panel.add(otherFeeField);
        panel.add(new JLabel()); // Empty cell for spacing
        panel.add(submitButton);

        // Style button
        submitButton.setBackground(new Color(59, 89, 182));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(this);

        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            BigDecimal examFee = parseField(examFeeField.getText());
            BigDecimal labFee = parseField(labFeeField.getText());
            BigDecimal sessionFee = parseField(sessionFeeField.getText());
            BigDecimal otherFee = parseField(otherFeeField.getText());
             

            String sql = "UPDATE paymentfields SET " +
                         "exam_fee = ?, " +
                         "lab_fee = ?, " +
                         "session_fee = ?, " +
                         "other_fee = ? " +
                         "WHERE id = 1";

            try (PreparedStatement pstmt = connect.c.prepareStatement(sql)) {
                pstmt.setBigDecimal(1, examFee);
                pstmt.setBigDecimal(2, labFee);
                pstmt.setBigDecimal(3, sessionFee);
                pstmt.setBigDecimal(4, otherFee);
                
             

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "Payment set successfully!");
                    clearFields();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private BigDecimal parseField(String text) {
        if (text == null || text.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format");
        }
    }

    private void clearFields() {
        examFeeField.setText("");
        labFeeField.setText("");
        sessionFeeField.setText("");
        otherFeeField.setText("");
        
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentForm());
    }
}