package server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private Set<ClientHandler> clients;
    private boolean isRunning;
    
    public ChatServer() {
        clients = Collections.synchronizedSet(new HashSet<>());
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            System.out.println("🚀 Chat Server started on port " + PORT);
            System.out.println("📍 Server IP: " + InetAddress.getLocalHost().getHostAddress());
            System.out.println("⏳ Waiting for clients...");
            
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔗 New client connected: " + clientSocket.getInetAddress());
                
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }
    
    public void broadcastMessage(Message message, ClientHandler sender) {
        synchronized (clients) {
            System.out.println("📢 Broadcasting message from " + message.getSender() + " to " + clients.size() + " clients");
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }
    
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("👋 Client disconnected. Total clients: " + clients.size());
    }
    
    public int getClientCount() {
        return clients.size();
    }
    
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.out.println("🛑 Server stopped");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new ChatServer().start();
    }
}