package main.java2final;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String userType;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                clientHandlers.add(this);

                // First message from the client should be the user type (user/admin)
                userType = in.readLine();
                System.out.println("User type: " + userType);

                // If the user is a customer, the next message should be the customer ID
                if (userType.equals("User")) {
                    String customerId = in.readLine();
                    System.out.println("Customer ID: " + customerId);

                    // The next message should be the customer username
                    String customerUsername = in.readLine();
                    System.out.println("Customer username: " + customerUsername);

                    // Send the customer ID to all other clients
                    broadcast(customerId);

                    // Send the customer username to all other clients
                    broadcast(customerUsername);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    String taggedMessage = userType + ": " + message;
                    System.out.println(message);
                    broadcast(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientHandlers.remove(this);
            }
        }

        private void broadcast(String message) {
            for (ClientHandler handler : clientHandlers) {
                handler.out.println(message);
            }
        }
    }
}
