package school.management.system;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server {
    private static HashMap<String, String> faq;

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        faq = new HashMap<>();
        // Add frequent question-response pairs
        faq.put("hello", "Hi there! How can I assist you?");
        faq.put("what is this app about?", "This is a Modern School Management System");
        faq.put("who can use this app?", "Admin,Teacher,Students");
        faq.put("what are the school timings?","The school operates from 10:00 AM to 4:00 PM.");
        faq.put("how do students and teachers register?","Admins first add students and teachers to the system.After that they can register");
        faq.put("what should be done if someone forget his password?","You can recover it from forget password section in login page");
        faq.put("can users update their profile after registration?", "Yes in dashboorad there is a section update info");
        
 

        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            System.out.println("Server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String question;
                while ((question = in.readLine()) != null) {
                    question = question.toLowerCase();
                    String reply = faq.getOrDefault(question, "Sorry, I don't understand that.");
                    out.println(reply);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
