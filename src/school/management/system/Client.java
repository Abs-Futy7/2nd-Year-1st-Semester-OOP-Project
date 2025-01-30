package school.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {

    private JComboBox<String> questionDropdown;
    private JPanel chatPanel;
    private JButton sendButton;
    private JButton refreshButton;
    private PrintWriter out;

    public Client() {
        setTitle("Frequently Asked Questions");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set overall background color
        getContentPane().setBackground(new Color(40, 40, 40)); // Dark gray background

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 78, 254)); // Blue header
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel titleLabel = new JLabel("FAQ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Raleway", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Chat Panel
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(new Color(30, 30, 30)); // Darker gray for chat area
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        //scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(40, 40, 40));
        //inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dropdown for predefined questions
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        questionDropdown = new JComboBox<>(new String[]{
            "Hello",
            "What is this app about?",
            "Who can use this app?",
            "What are the school timings?",
            "How do students and teachers register?",
            "What should be done if someone forget his password?",
            "Can users update their profile after registration?"
        });

        questionDropdown.setFont(new Font("Raleway", Font.PLAIN, 16));
        questionDropdown.setBackground(new Color(60, 60, 60));
        questionDropdown.setForeground(Color.WHITE);
        questionDropdown.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        inputPanel.add(questionDropdown, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(40, 40, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;

        sendButton = createStyledButton("Send", new Color(44, 78, 254), new Color(35, 60, 200));
        refreshButton = createStyledButton("Refresh", new Color(60, 141, 199), new Color(40, 100, 150));

        buttonPanel.add(sendButton);
        buttonPanel.add(refreshButton);
        inputPanel.add(buttonPanel, gbc);

        add(inputPanel, BorderLayout.SOUTH);

        // Add Action Listeners
        sendButton.addActionListener(e -> sendMessage());
        refreshButton.addActionListener(e -> resetInterface());

        // Connect to server
        connectToServer();
    }

    private JButton createStyledButton(String text, Color backgroundColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Raleway", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        return button;
    }

    private void sendMessage() {
        String selectedQuestion = (String) questionDropdown.getSelectedItem();
        if (selectedQuestion != null && !selectedQuestion.trim().isEmpty()) {
            addMessage("You", selectedQuestion, true);
            out.println(selectedQuestion);
            questionDropdown.setSelectedIndex(0); // Reset dropdown
        }
    }

    private void resetInterface() {
        questionDropdown.setSelectedIndex(0);
        chatPanel.removeAll(); // Clear all messages
        chatPanel.revalidate();
        chatPanel.repaint();
    }

    private void addMessage(String sender, String message, boolean isClient) {
        MessagePanel messagePanel = new MessagePanel(sender, message, isClient);

        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(new Color(30, 30, 30));
        wrapperPanel.setOpaque(false);

        if (isClient) {
            wrapperPanel.add(messagePanel, BorderLayout.EAST);
        } else {
            wrapperPanel.add(messagePanel, BorderLayout.WEST);
        }

        chatPanel.add(wrapperPanel);
        chatPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        chatPanel.revalidate();
        chatPanel.repaint();

        JScrollBar vertical = ((JScrollPane) chatPanel.getParent().getParent()).getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 7777);
            out = new PrintWriter(socket.getOutputStream(), true);
            new Thread(new ServerListener(socket)).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to the server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class MessagePanel extends JPanel {

        private final String message;
        private final boolean isClient;

        public MessagePanel(String sender, String message, boolean isClient) {
            this.message = sender + ": " + message;
            this.isClient = isClient;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bubbleColor = isClient ? new Color(44, 78, 254) : new Color(60, 60, 60);
            g2d.setColor(bubbleColor);

            FontMetrics metrics = g2d.getFontMetrics();
            int messageWidth = metrics.stringWidth(message) + 20;
            int messageHeight = metrics.getHeight() + 10;

            g2d.fillRoundRect(0, 0, messageWidth, messageHeight, 20, 20);

            g2d.setColor(Color.WHITE);
            g2d.drawString(message, 10, messageHeight / 2 + metrics.getAscent() / 2 - 5);

            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics metrics = getFontMetrics(getFont());
            int messageWidth = metrics.stringWidth(message) + 30;
            int messageHeight = metrics.getHeight() + 20;
            return new Dimension(messageWidth, messageHeight);
        }
    }

    private class ServerListener implements Runnable {

        private final BufferedReader in;

        public ServerListener(Socket socket) throws IOException {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        public void run() {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    addMessage("Server", response, false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void startClient() {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.setVisible(true);
        });
    }
}