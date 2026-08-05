# Online Chat Application

# Project Overview

This project is a simple online chat application developed in Java using socket programming. The application follows the client-server architecture, allowing multiple clients to connect to a central server and communicate in real time.

Each connected client is assigned a unique user ID by the server. Messages sent by one client are broadcast to all other connected clients currently connected to the server. The application demonstrates the use of Java networking, multithreading, and input/output streams.

---

# Objectives

The objectives of this project are to:

- Develop a multi-client chat server using Java sockets.
- Implement a client application capable of sending and receiving messages.
- Demonstrate client-server communication.
- Apply multithreading to handle multiple users simultaneously.
- Design a simple text-based user interface.

---

# Features

The application includes the following features:

- Multi-client support.
- Server assigns unique user IDs.
- Real-time message broadcasting.
- Simultaneous message sending and receiving.
- Graceful client disconnection using the EXIT command.
- Text-based command-line interface.
- Thread-based client handling.

---

# Project Structure

```
OnlineChatApplication
│
├── src
│   ├── ChatServer.java
│   ├── ClientHandler.java
│   └── ChatClient.java
│
├── screenshots
│
├── README.md
├── References.md
└── .gitignore
```

---

# Technologies Used

- Java
- Java Socket Programming
- ServerSocket
- Socket
- BufferedReader
- PrintWriter
- Multithreading
- Visual Studio Code

---

# How the Application Works

### Chat Server

The server starts by creating a `ServerSocket` that listens on port **5000**. Whenever a client connects, the server assigns a unique user ID (e.g., User1, User2) and creates a separate thread to manage communication with that client.

### Client Handler

Each client is managed independently by the `ClientHandler` class. It continuously listens for incoming messages from the client and forwards them to the server, which broadcasts them to the remaining connected users.

### Chat Client

The client connects to the server using a socket. Two concurrent operations occur:

- One thread receives messages from the server.
- The main thread accepts keyboard input and sends messages to the server.

This enables real-time communication without interrupting either operation.

---

# How to Compile

Open the project folder in Visual Studio Code and run:

```bash
javac src/*.java
```

---

# How to Run

### Step 1: Start the Server

```bash
java -cp src ChatServer
```

---

### Step 2: Start Client 1

Open a new terminal and run:

```bash
java -cp src ChatClient
```

---

### Step 3: Start Client 2

Open another terminal and run:

```bash
java -cp src ChatClient
```

Additional clients may also connect using the same command.

---

# Sample Conversation

```
User1:
Hello everyone!

User2:
Hi User1!

User3:
Welcome to the chat!
```

---

# Screenshots

The following screenshots are included with the submission:

- Chat server running
- Client 1 connected
- Client 2 connected
- Chat conversation between users

---

# Challenges Encountered

Some of the challenges encountered during development included:

- Managing multiple client connections simultaneously.
- Ensuring thread-safe broadcasting of messages.
- Handling client disconnections without affecting other users.
- Maintaining a clear separation of responsibilities between the server, client handler, and client classes.

These challenges were addressed using Java's multithreading capabilities and synchronized access to shared resources.

---

# Conclusion

The Online Chat Application successfully demonstrates socket programming, client-server communication, multithreading, and real-time message broadcasting in Java. The project meets the assignment requirements by supporting multiple clients, assigning unique user IDs, and providing a simple text-based interface for communication.

---