/*
 * Assignment: Online Chat Application
 * File: ClientHandler.java
 * Author: Brian Wakhale
 *
 * Description:
 * This class manages communication with a single connected client.
 * Each client runs in its own thread, allowing multiple users to
 * chat simultaneously.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    // Socket used to communicate with the client
    private Socket clientSocket;

    // Reads messages from the client
    private BufferedReader reader;

    // Sends messages to the client
    private PrintWriter writer;

    // Unique ID assigned by the server
    private String userId;

    /**
     * Constructor for a connected client.
     *
     * @param socket The client's socket.
     * @param userId Unique user ID assigned by the server.
     */
    public ClientHandler(Socket socket, String userId) {

        this.clientSocket = socket;
        this.userId = userId;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            writer = new PrintWriter(
                    clientSocket.getOutputStream(), true);

            // Send the assigned user ID to the client
            writer.println("Connected successfully!");
            writer.println("Your ID is: " + userId);
            writer.println("-----------------------------------");
            writer.println("Type your message and press Enter.");
            writer.println("Type EXIT to leave the chat.");
            writer.println("-----------------------------------");

        } catch (IOException exception) {
            System.out.println("Error setting up client: "
                    + exception.getMessage());
        }
    }

    /**
     * Runs continuously while the client is connected.
     * Reads incoming messages and sends them to the server
     * for broadcasting.
     */
    @Override
    public void run() {

        String message;

        try {

            while ((message = reader.readLine()) != null) {

                // Allow the user to disconnect gracefully
                if (message.equalsIgnoreCase("EXIT")) {
                    break;
                }

                // Ignore blank messages
                if (message.trim().isEmpty()) {
                    continue;
                }

                // Broadcast the message to all other clients
                ChatServer.broadcastMessage(
                        userId + ": " + message,
                        this
                );
            }

        } catch (IOException exception) {

            System.out.println(userId + " connection lost.");

        } finally {

            closeConnection();

            ChatServer.removeClient(this);
        }
    }

    /**
     * Sends a message to this client.
     *
     * @param message Message received from another client.
     */
    public void sendMessage(String message) {

        writer.println(message);

    }

    /**
     * Returns the client's unique user ID.
     *
     * @return user ID.
     */
    public String getUserId() {

        return userId;

    }

    /**
     * Closes all resources associated with the client.
     */
    private void closeConnection() {

        try {

            if (reader != null) {
                reader.close();
            }

            if (writer != null) {
                writer.close();
            }

            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }

        } catch (IOException exception) {

            System.out.println("Error closing connection: "
                    + exception.getMessage());

        }
    }
}