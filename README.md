# JavaFX Chat Application

A real-time multi-client chat application built with Java and JavaFX, featuring a client-server architecture with a modern graphical user interface.

## Features
- Real-time messaging between multiple clients
- Multi-client server support
- Modern JavaFX GUI
- Automatic join/leave notifications
- Message timestamps
- Real-time connection status

## Project Structure
java-chat/
├── src/
│   ├── server/
│   │   ├── ChatServer.java
│   │   ├── ClientHandler.java
│   │   └── ServerGUI.java
│   ├── client/
│   │   ├── ChatClient.java
│   │   ├── ClientGUI.java
│   │   └── Message.java
│   └── main/
│       └── Main.java
├── resources/
│   └── styles.css
└── pom.xml

## Installation & Setup

### Method 1: Using Maven (Recommended)
Clone the repository:
git clone https://github.com/yourusername/java-chat.git
cd java-chat

Build the project:
mvn clean compile

### Method 2: Manual Setup
- Install JavaFX SDK
- Add JavaFX to your IDE module path
- Add VM options:
--module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml

## Running the Application

### Step 1: Start the Server
Using Maven:
mvn compile exec:java -Dexec.mainClass="server.ChatServer"

Using Java:
java -cp target/classes server.ChatServer

Expected output:
Chat Server started on port 12345

### Step 2: Start Client(s)
Using Maven:
mvn javafx:run

Using Java:
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp target/classes client.ClientGUI

### Step 3: Connect and Chat
- Default server: localhost:12345
- Enter a username
- Click "Connect"
- Start chatting

## Usage Guide

### For Server Administrators
- Default port: 12345
- Console shows connected clients
- All messages broadcast to everyone

### For Users
- Enter correct server IP + port
- Choose a unique username
- Press Enter or click Send
- Click Disconnect to leave

## Core Components

### Server Side
- ChatServer: main server class
- ClientHandler: manages each client

### Client Side
- ChatClient: networking logic
- ClientGUI: JavaFX UI
- Message: data model

## Configuration

### Change Server Port
In ChatServer.java:
private static final int PORT = 12345;

### Customize UI
Edit:
resources/styles.css

## Troubleshooting

### "JavaFX runtime components are missing"
- JavaFX SDK not installed
- Module path not configured

### Connection refused
- Server not running
- Firewall might be blocking port 12345

### Username already taken
- Choose another username

## Contributing
git checkout -b feature/NewFeature
git commit -m "Add NewFeature"
git push origin feature/NewFeature
Open a Pull Request.
