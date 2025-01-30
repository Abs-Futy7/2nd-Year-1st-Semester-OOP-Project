package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutUs extends JFrame implements ActionListener {

    JButton backButton;

    AboutUs() {
        // Frame setup
        setTitle("About Us - DU Scholars Academy");
        setSize(1200, 600);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Background image
        ImageIcon bg = new ImageIcon(ClassLoader.getSystemResource("assets/bg1.png"));
        Image i2 = bg.getImage().getScaledInstance(1200, 600, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1200, 600);
        add(image);

        // Logo
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/assets/DU_Scholars_Academy.png"));
        Image logoImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(resizedLogo);
        logoLabel.setBounds(230, 30, 120, 120);
        image.add(logoLabel);

        // Heading
        JLabel heading = new JLabel("About DU Scholars Academy", SwingConstants.CENTER);
        heading.setFont(new Font("Raleway", Font.BOLD, 40)); // Font style and size for the heading
        heading.setForeground(new Color(211, 230, 253)); // Light blue text color
        heading.setBounds(300, 50, 700, 60);
        image.add(heading);

        // Description
        JTextArea description = new JTextArea(
            "DU Scholars Academy is committed to providing quality education that empowers students to learn, grow, and succeed.\n\n"
            + "Our dedicated faculty and staff create a nurturing environment for students to excel academically and socially.\n\n"
            + "We aim to shape the future leaders of tomorrow with innovative teaching methods and a strong commitment to excellence."
        );
        description.setFont(new Font("Raleway", Font.PLAIN, 20));
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false); // Make it non-editable
        description.setOpaque(false); // Transparent background
        description.setForeground(Color.WHITE); // White text color

        // Center the description in the middle of the screen
        description.setBounds(250, 150, 700, 200);
        image.add(description);

        // Back Button
        backButton = new JButton("Back to Home");
        backButton.setBounds(500, 400, 200, 50);
        backButton.addActionListener(this);
        backButton.setFont(new Font("Raleway", Font.BOLD, 18));
        backButton.setBackground(new Color(44, 78, 254)); // Dark blue background for button
        backButton.setForeground(Color.WHITE); // White text for button
        backButton.setFocusPainted(false); // Remove focus border
        backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Rounded border
        image.add(backButton);

        // Make the frame visible
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            this.dispose(); // Close the AboutUs page
           // new MainPage(); // Go back to the MainPage
        }
    }

    public static void main(String[] args) {
        new AboutUs();
    }
}