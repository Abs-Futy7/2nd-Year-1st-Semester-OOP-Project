package school.management.system;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;
import java.util.Base64;
import javax.swing.ImageIcon;

class ChatClientCustomButton extends JButton {
    private Color defaultColor;
    private Color hoverColor;

    public ChatClientCustomButton(String text, Color defaultColor, Color hoverColor) {
        super(text);
        this.defaultColor = defaultColor;
        this.hoverColor = hoverColor;
        
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Raleway", Font.BOLD, 14));
        setBackground(defaultColor);
        setOpaque(true);

         

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
               setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
               setBackground(defaultColor);
            }
        });
    }
}

public class ChatClient {
    private static final String SERVER_ADDRESS = "localhost";
  //  private static final String SERVER_ADDRESS = "127.0.0.1";
  //  private static final String SERVER_ADDRESS = "192.168.0.102";
    
    private static final int SERVER_PORT = 12345;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame frame;
    private JTextField textField;
    private JPanel chatPanel;
    private ChatClientCustomButton sendButton;
    private JToggleButton darkModeToggle;
    private boolean isDarkMode = true;
    private String username;
    private ChatClientCustomButton fileButton;

    public ChatClient() {
        username = JOptionPane.showInputDialog(frame, "Enter your username:", "Chat Client", JOptionPane.PLAIN_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username cannot be empty. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
          //  System.exit(0);
        }
        createAndShowGUI();
    }

    private void createAndShowGUI() {
    frame = new JFrame("Client Name - " + username);
    frame.setLayout(new BorderLayout());

    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new BorderLayout());
    headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    headerPanel.setBackground(new Color(0, 120, 215));

    JLabel titleLabel = new JLabel("Chat Room", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    titleLabel.setForeground(Color.WHITE);
    headerPanel.add(titleLabel, BorderLayout.CENTER);

    darkModeToggle = new JToggleButton("Dark Mode");
    darkModeToggle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    darkModeToggle.setBackground(new Color(0, 120, 215));
    darkModeToggle.setForeground(Color.WHITE);
    darkModeToggle.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    darkModeToggle.setFocusPainted(false);
    darkModeToggle.addActionListener(e -> toggleDarkMode());
    headerPanel.add(darkModeToggle, BorderLayout.EAST);

    frame.add(headerPanel, BorderLayout.NORTH);

    chatPanel = new JPanel();
    chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
    chatPanel.setBackground(new Color(240, 240, 240));
    JScrollPane scrollPane = new JScrollPane(chatPanel);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    frame.add(scrollPane, BorderLayout.CENTER);

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BorderLayout());
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    inputPanel.setBackground(new Color(255, 255, 255));

    // Load the file icon image
    ImageIcon fileIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/file.png")); // Replace with the correct path
    if (fileIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
        System.out.println("Error: File icon not found!");
    } else {
        System.out.println("File icon loaded successfully.");
    }
    fileButton = new ChatClientCustomButton("", new Color(76, 175, 80), new Color(56, 142, 60));
    fileButton.setIcon(fileIcon);
    fileButton.setToolTipText("Click to send a file");
    fileButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    fileButton.setBackground(new Color(0, 120, 215));
    fileButton.setForeground(Color.WHITE);
    fileButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    fileButton.addActionListener(e -> sendFile());
    inputPanel.add(fileButton, BorderLayout.WEST);

    textField = new JTextField();
    textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    textField.setBackground(new Color(255, 255, 255));
    textField.setForeground(new Color(50, 50, 50));
    textField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    inputPanel.add(textField, BorderLayout.CENTER);

    // Load the send icon image
    ImageIcon sendIcon = new ImageIcon(getClass().getClassLoader().getResource("assets/send.png")); // Replace with the correct path
     if (sendIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
        System.out.println("Error: Send icon not found!");
    } else {
        System.out.println("Send icon loaded successfully.");
    }
    sendButton = new ChatClientCustomButton("", new Color(33, 150, 243), new Color(25, 118, 210));
    sendButton.setIcon(sendIcon);
    sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    sendButton.setBackground(new Color(0, 120, 215));
    sendButton.setForeground(Color.WHITE);
    sendButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    sendButton.setFocusPainted(false);
    sendButton.addActionListener(e -> sendMessage());
    inputPanel.add(sendButton, BorderLayout.EAST);

    frame.add(inputPanel, BorderLayout.SOUTH);

    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(500, 400);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    connectToServer();
}

    private void sendMessage() {
        String message = textField.getText();
        if (!message.isEmpty()) {
            appendMessage("You: " + message, true);
            out.println(message);
            textField.setText("");
        }
    }
    
    // Add this method to the ChatClient class
    private void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (file.length() > 10 * 1024 * 1024) {
                JOptionPane.showMessageDialog(frame, "File size exceeds 10MB limit");
                return;
            }

            try {
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                String base64Data = Base64.getEncoder().encodeToString(fileBytes);
                
                String recipient = JOptionPane.showInputDialog(
                    frame, "Send to (username or 'broadcast'):", "File Recipient", JOptionPane.PLAIN_MESSAGE);
                
                if (recipient != null && !recipient.isEmpty()) {
                    String command = "[FILE]@" + recipient.trim() + "|" + file.getName() + "|" + base64Data;
                    out.println(command);
                    appendMessage("You sent file: " + file.getName(), true);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error reading file");
            }
        }
    }

    private void appendMessage(String message, boolean isOwnMessage) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String timestamp = dateFormat.format(new Date());

        MessageBubble messageBubble = new MessageBubble(message, timestamp, isOwnMessage, isDarkMode);
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(isDarkMode ? new Color(40, 40, 40) : new Color(240, 240, 240));
        wrapperPanel.add(messageBubble, isOwnMessage ? BorderLayout.EAST : BorderLayout.WEST);

        chatPanel.add(wrapperPanel);
        chatPanel.revalidate();
        chatPanel.repaint();

        JScrollBar verticalScrollBar = ((JScrollPane) chatPanel.getParent().getParent()).getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }

    private void toggleDarkMode() {
        isDarkMode = darkModeToggle.isSelected();
        Color bgColor = isDarkMode ? new Color(40, 40, 40) : new Color(240, 240, 240);
        Color fgColor = isDarkMode ? Color.WHITE : Color.BLACK;

        chatPanel.setBackground(bgColor);
        frame.getContentPane().setBackground(bgColor);
        textField.setBackground(isDarkMode ? new Color(80, 80, 80) : Color.WHITE);
        textField.setForeground(fgColor);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(isDarkMode ? new Color(100, 100, 100) : new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        sendButton.setBackground(isDarkMode ? new Color(0, 80, 160) : new Color(0, 120, 215));

        for (Component comp : chatPanel.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component bubble : ((JPanel) comp).getComponents()) {
                    if (bubble instanceof MessageBubble) {
                        bubble.repaint();
                    }
                }
            }
        }
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(username);
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            appendMessage("Unable to connect to server.", false);
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("[FILE]")) {
                    handleIncomingFile(message);
                } else if (message.startsWith("[PRIVATE]")) {
                    System.out.println("check private");
                    appendMessage(message, false);
                }else if(message.contains("Enter your username")){  
                }
                else {
                    appendMessage(message, false);
                }
            }
        } catch (IOException e) {
            appendMessage("Connection lost.", false);
        } finally {
            cleanup();
        }
    }

    private void handleIncomingFile(String fileMessage) {
        String[] parts = fileMessage.substring(6).split("\\|", 4);
        String context = parts[0].substring(1);
        
        System.out.println(context);
        
        if (context.equalsIgnoreCase("broadcast") && parts.length == 4) {
            String sender = parts[1];
            String fileName = parts[2];
            String fileData = parts[3];
            showFileMessage(sender, fileName, fileData, true);
        } else if (parts.length == 3) {
            String sender = context;
            String fileName = parts[1];
            String fileData = parts[2];
            showFileMessage(sender, fileName, fileData, false);
        }
    }
    
    private void showFileMessage(String sender, String fileName, String fileData, boolean isBroadcast) {
        String prefix = isBroadcast ? "[Broadcast] " : "[Private] ";
        JPanel filePanel = new JPanel();
        filePanel.setBackground(isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200));
        JLabel fileLabel = new JLabel(prefix + "📎 " + fileName + " (From: " + sender + ")");
        fileLabel.setForeground(isDarkMode ? Color.WHITE : Color.BLACK);
        fileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        fileLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JFileChooser saver = new JFileChooser();
                saver.setSelectedFile(new File(fileName));
                int result = saver.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        byte[] decoded = Base64.getDecoder().decode(fileData);
                        Files.write(saver.getSelectedFile().toPath(), decoded);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error saving file");
                    }
                }
            }
        });

        filePanel.add(fileLabel);
        chatPanel.add(filePanel);
        chatPanel.revalidate();
        chatPanel.repaint();
    }
    private void cleanup() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing client socket: " + e.getMessage());
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }

    private static class MessageBubble extends JPanel {
        private final String message;
        private final String timestamp;
        private final boolean isOwnMessage;
        private final boolean isDarkMode;

        public MessageBubble(String message, String timestamp, boolean isOwnMessage, boolean isDarkMode) {
            this.message = message;
            this.timestamp = timestamp;
            this.isOwnMessage = isOwnMessage;
            this.isDarkMode = isDarkMode;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bubbleColor = isOwnMessage ? new Color(0, 120, 215) : (isDarkMode ? new Color(60, 60, 60) : new Color(200, 200, 200));
            g2d.setColor(bubbleColor);

            int arc = 20;
            int padding = 5;
            int bubbleX = padding;
            int bubbleY = padding;
            int bubbleWidth = getWidth() - 2 * padding;
            int bubbleHeight = getHeight() - 2 * padding;
            g2d.fillRoundRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight, arc, arc);

            g2d.setColor(isOwnMessage ? Color.WHITE : (isDarkMode ? Color.WHITE : Color.BLACK));
            FontMetrics metrics = g2d.getFontMetrics();
            int textX = padding + 5;
            int textY = metrics.getAscent() + padding;
            
             
            g2d.drawString(message, textX, textY);
             

            g2d.setColor(isOwnMessage ? new Color(200, 200, 200) : (isDarkMode ? new Color(150, 150, 150) : new Color(100, 100, 100)));
            g2d.setFont(getFont().deriveFont(10f));
            int timestampX = getWidth() - g2d.getFontMetrics().stringWidth(timestamp) - padding - 5;
            int timestampY = getHeight() - padding;
            
            
            g2d.drawString(timestamp, timestampX, timestampY);
             

            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics metrics = getFontMetrics(getFont());
            int width = Math.min(300, metrics.stringWidth(message) + 40);
            int height = metrics.getHeight() + 20;
            return new Dimension(width, height);
        }
    }
}