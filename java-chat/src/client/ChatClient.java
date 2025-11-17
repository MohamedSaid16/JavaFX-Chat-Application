package client;

import java.io.*;
import java.net.*;

public class ChatClient {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverAddress;
    private int serverPort;
    private MessageListener messageListener;
    private boolean connected = false;
    
    public interface MessageListener {
        void onMessageReceived(Message message);
        void onConnectionStatusChanged(boolean connected, String message);
    }
    
    public ChatClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
    
    public void setMessageListener(MessageListener listener) {
        this.messageListener = listener;
    }
    
    public boolean connect(String username) {
        try {
            socket = new Socket(serverAddress, serverPort);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            // Send join message
            Message joinMessage = new Message(username, "joined the chat", Message.MessageType.JOIN);
            output.writeObject(joinMessage);
            output.flush();
            
            connected = true;
            
            // Start message receiver
            startMessageReceiver();
            
            if (messageListener != null) {
                messageListener.onConnectionStatusChanged(true, "Connected to server successfully");
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            if (messageListener != null) {
                messageListener.onConnectionStatusChanged(false, "Connection failed: " + e.getMessage());
            }
            return false;
        }
    }
    
    private void startMessageReceiver() {
        Thread receiverThread = new Thread(() -> {
            try {
                while (connected && socket.isConnected() && !socket.isClosed()) {
                    Message message = (Message) input.readObject();
                    if (messageListener != null) {
                        messageListener.onMessageReceived(message);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("📡 Connection lost: " + e.getMessage());
            } finally {
                disconnect();
            }
        });
        receiverThread.setDaemon(true);
        receiverThread.start();
    }
    
    public void sendMessage(String sender, String content) {
        if (!connected) {
            if (messageListener != null) {
                messageListener.onConnectionStatusChanged(false, "Not connected to server");
            }
            return;
        }
        
        try {
            Message message = new Message(sender, content, Message.MessageType.TEXT);
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("❌ Failed to send message: " + e.getMessage());
            if (messageListener != null) {
                messageListener.onConnectionStatusChanged(false, "Failed to send message");
            }
            disconnect();
        }
    }
    
    public void disconnect() {
        connected = false;
        try {
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
            
            if (messageListener != null) {
                messageListener.onConnectionStatusChanged(false, "Disconnected from server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isConnected() {
        return connected && socket != null && socket.isConnected() && !socket.isClosed();
    }
}