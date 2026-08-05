/*
 * Assignment: Online Chat Application
 * File: ChatServer.java
 * Author: Brian Wakhale
 *
 * Description:
 * This class starts the chat server, accepts connections from multiple
 * clients, assigns each client a unique user ID, and broadcasts messages
 * to all connected clients.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

    // Port number the server will listen on
    private static final int PORT = 5000;

    // Stores all connected clients
    private static final List<ClientHandler> connectedClients = new ArrayList<>();

    // Counter used to generate unique user IDs
    private static int userCounter = 1;

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println("      ONLINE CHAT SERVER");
        System.out.println("====================================");
        System.out.println("Server is starting...");
        System.out.println("Listening on port " + PORT);
        System.out.println();

        // Try-with-resources automatically closes the ServerSocket
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            // Keep accepting clients until the server is stopped
            while (true) {

                // Wait for a client to connect
                Socket clientSocket = serverSocket.accept();

                // Generate a unique ID for the new client
                String userId = "User" + userCounter++;

                System.out.println(userId + " has connected.");

                // Create a handler for the client
                ClientHandler clientHandler =
                        new ClientHandler(clientSocket, userId);

                // Add the client to the list of connected users
                synchronized (connectedClients) {
                    connectedClients.add(clientHandler);
                }

                // Inform all connected users
                broadcastMessage(
                        "SERVER: " + userId + " joined the chat.",
                        clientHandler
                );

                // Start handling the client in a separate thread
                clientHandler.start();
            }

        } catch (IOException exception) {
            System.out.println("Server Error: " + exception.getMessage());
        }
    }

    /**
     * Sends a message to every connected client except the sender.
     *
     * @param message The message to broadcast.
     * @param sender The client who sent the message.
     */
    public static void broadcastMessage(String message,
                                        ClientHandler sender) {

        synchronized (connectedClients) {

            for (ClientHandler client : connectedClients) {

                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }

        // Display the message on the server console
        System.out.println(message);
    }

    /**
     * Removes a disconnected client from the active client list.
     *
     * @param clientHandler The disconnected client.
     */
    public static void removeClient(ClientHandler clientHandler) {

        synchronized (connectedClients) {
            connectedClients.remove(clientHandler);
        }

        System.out.println(clientHandler.getUserId()
                + " disconnected.");

        broadcastMessage(
                "SERVER: " + clientHandler.getUserId()
                        + " left the chat.",
                clientHandler
        );
    }
}