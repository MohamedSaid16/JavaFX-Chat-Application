package client;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    private MessageType type;
    
    public enum MessageType {
        TEXT, JOIN, LEAVE, ERROR, SYSTEM
    }
    
    public Message(String sender, String content, MessageType type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public MessageType getType() { return type; }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    @Override
    public String toString() {
        switch (type) {
            case JOIN:
                return "🎉 " + content;
            case LEAVE:
                return "👋 " + content;
            case SYSTEM:
                return "⚡ " + content;
            case ERROR:
                return "❌ " + content;
            default:
                return String.format("[%s] %s: %s", getFormattedTimestamp(), sender, content);
        }
    }
}