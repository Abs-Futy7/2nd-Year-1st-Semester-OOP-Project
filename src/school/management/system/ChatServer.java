 package school.management.system;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static final Map<String, PrintWriter> userWriters = Collections.synchronizedMap(new HashMap<>());

    public static void main(String[] args) {
        System.out.println("Chat server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Error in server: " + e.getMessage());
        }
    }

    public static class ClientHandler extends Thread {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Enter your username:");
                username = in.readLine();
                if (username == null || username.isEmpty()) {
                    socket.close();
                    return;
                }

                synchronized (userWriters) {
                    if (userWriters.containsKey(username)) {
                        out.println("Username already taken. Disconnecting...");
                        socket.close();
                        return;
                    }
                    userWriters.put(username, out);
                }

                System.out.println(username + " has joined the chat.");
                broadcast(username + " has joined the chat.", null);

                String message;
                while ((message = in.readLine()) != null) {
                     
                    handleClientMessage(message);
                    
                }
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                cleanup();
            }
        }

        private void handleClientMessage(String message) {
            message = message.trim();
            
            
            
            

            if (message.startsWith("[FILE]")) {
                handleFileTransfer(message);
                return;
            }
            
            if (message.contains("@")) {
                String[] parts = message.split(" ", 2);
                System.out.println(message);
                if (parts.length > 1) {
                    String targetUser = parts[0].substring(1);
                    String privateMessage = parts[1];
                    System.out.println(targetUser+" "+privateMessage);
                    sendPrivateMessage(username, targetUser, privateMessage);
                } else {
                    out.println("Invalid format");
                }
            } else {
                broadcast(username + ": " + message, username);
            }
        }

        private void handleFileTransfer(String fileMessage) {
            String[] parts = fileMessage.substring(6).split("\\|", 3);
            if (parts.length != 3) return;

            String targetType = parts[0].substring(1);
            String fileName = parts[1];
            String fileData = parts[2];
            
            System.out.println(targetType);

            synchronized (userWriters) {
                if (targetType.equals("broadcast")) {
                    for (Map.Entry<String, PrintWriter> entry : userWriters.entrySet()) {
                        if (!entry.getKey().equals(username)) {
                            entry.getValue().println("[FILE]@broadcast|" + username + "|" + fileName + "|" + fileData);
                        }
                    }
                } else {
                    PrintWriter targetWriter = userWriters.get(targetType);
                    if (targetWriter != null) {
                        targetWriter.println("[FILE]@" + username + "|" + fileName + "|" + fileData);
                    } else {
                        userWriters.get(username).println("User " + targetType + " not found");
                    }
                }
            }
        }

        private void sendPrivateMessage(String fromUser, String toUser, String message) {
            synchronized (userWriters) {
                PrintWriter targetWriter = userWriters.get(toUser);
                if (targetWriter != null) {
                    System.out.println("Private Message");
                    targetWriter.println("[Private]" + fromUser + ": " + message);
                } else {
                    userWriters.get(fromUser).println("User " + toUser + " not found");
                }
            }
        }

        private void broadcast(String message, String excludeUser) {
            synchronized (userWriters) {
                for (Map.Entry<String, PrintWriter> entry : userWriters.entrySet()) {
                    if (!entry.getKey().equals(excludeUser)) {
                        entry.getValue().println(message);
                    }
                }
            }
        }

        private void cleanup() {
            if (username != null) {
                System.out.println(username + " has left the chat.");
                broadcast(username + " has left the chat.", null);
                synchronized (userWriters) {
                    userWriters.remove(username);
                }
            }
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }
    }
}



