/*
 * Assignment: Online Chat Application
 * File: ChatClient.java
 * Author: Brian Wakhale
 *
 * Description:
 * This class connects a client to the chat server. It allows the
 * user to send messages while simultaneously receiving messages
 * from other connected users.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    // Server details
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("       ONLINE CHAT APPLICATION");
        System.out.println("========================================");

        try (

                // Connect to the chat server
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

                // Read messages from the server
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // Send messages to the server
                PrintWriter writer = new PrintWriter(
                        socket.getOutputStream(), true);

                // Read user input
                Scanner scanner = new Scanner(System.in);

        ) {

            System.out.println("Connected to the chat server.");
            System.out.println();

            /*
             * Thread responsible for receiving messages
             * from the server.
             */
            Thread receiveThread = new Thread(() -> {

                String message;

                try {

                    while ((message = reader.readLine()) != null) {

                        System.out.println(message);

                    }

                } catch (IOException exception) {

                    System.out.println("Disconnected from server.");

                }

            });

            receiveThread.start();

            /*
             * Main thread continuously reads keyboard input
             * and sends it to the server.
             */
            while (true) {

                String message = scanner.nextLine();

                writer.println(message);

                // Exit the application
                if (message.equalsIgnoreCase("EXIT")) {

                    System.out.println("Leaving chat...");

                    break;

                }

            }

        } catch (IOException exception) {

            System.out.println("Unable to connect to the server.");
            System.out.println(exception.getMessage());

        }

    }
}