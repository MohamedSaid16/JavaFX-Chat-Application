package server;

import java.io.*;
import java.net.*;
import client.Message;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String username;
    
    public ClientHandler(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            // Receive username
            Message joinMessage = (Message) input.readObject();
            this.username = joinMessage.getSender();
            
            System.out.println("🎉 " + username + " joined the chat");
            
            // Broadcast join message
            Message broadcastJoin = new Message("System", 
                username + " joined the chat", Message.MessageType.JOIN);
            server.broadcastMessage(broadcastJoin, this);
            
            // Receive messages
            while (socket.isConnected() && !socket.isClosed()) {
                Message message = (Message) input.readObject();
                if (message.getType() == Message.MessageType.TEXT) {
                    System.out.println("💬 " + username + ": " + message.getContent());
                    server.broadcastMessage(message, this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️  Client " + username + " disconnected unexpectedly");
        } finally {
            disconnect();
        }
    }
    
    public void sendMessage(Message message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("❌ Failed to send message to " + username);
            disconnect();
        }
    }
    
    private void disconnect() {
        try {
            if (username != null) {
                Message leaveMessage = new Message("System", 
                    username + " left the chat", Message.MessageType.LEAVE);
                server.broadcastMessage(leaveMessage, this);
                System.out.println("👋 " + username + " disconnected");
            }
            
            server.removeClient(this);
            
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getUsername() {
        return username;
    }
}