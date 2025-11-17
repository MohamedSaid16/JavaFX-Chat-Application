package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientGUI extends Application implements ChatClient.MessageListener {
    private ChatClient client;
    private String username;
    
    // UI Components
    private TextArea chatArea;
    private TextField messageField;
    private Button sendButton;
    private Button connectButton;
    private Button disconnectButton;
    private TextField serverField;
    private TextField portField;
    private TextField usernameField;
    private Label statusLabel;
    private VBox loginPanel;
    private VBox chatPanel;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Chat Application");
        
        // Create login panel
        loginPanel = createLoginPanel();
        
        // Create chat panel
        chatPanel = createChatPanel();
        chatPanel.setVisible(false);
        
        BorderPane root = new BorderPane();
        root.setCenter(loginPanel);
        
        Scene scene = new Scene(root, 700, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createLoginPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(30));
        panel.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10;");
        
        Label titleLabel = new Label("💬 JavaFX Chat");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label subtitleLabel = new Label("Connect to start chatting");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        serverField = new TextField();
        serverField.setPromptText("Server Address");
        serverField.setText("localhost");
        styleTextField(serverField);
        
        portField = new TextField();
        portField.setPromptText("Port");
        portField.setText("12345");
        styleTextField(portField);
        
        usernameField = new TextField();
        usernameField.setPromptText("Your Username");
        styleTextField(usernameField);
        
        connectButton = new Button("Connect to Chat");
        connectButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        connectButton.setPrefHeight(40);
        connectButton.setOnAction(e -> connectToServer());
        
        statusLabel = new Label("Ready to connect");
        statusLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");
        
        panel.getChildren().addAll(titleLabel, subtitleLabel, serverField, portField, usernameField, connectButton, statusLabel);
        return panel;
    }
    
    private VBox createChatPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        
        // Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 10; -fx-border-radius: 5;");
        Label chatTitle = new Label("Group Chat");
        chatTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        HBox.setHgrow(chatTitle, Priority.ALWAYS);
        
        disconnectButton = new Button("Disconnect");
        disconnectButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        disconnectButton.setOnAction(e -> disconnect());
        
        header.getChildren().addAll(chatTitle, disconnectButton);
        
        // Chat area
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(400);
        chatArea.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px;");
        
        // Input area
        HBox inputPanel = new HBox(10);
        messageField = new TextField();
        messageField.setPromptText("Type your message here...");
        messageField.setStyle("-fx-padding: 8; -fx-font-size: 14px;");
        messageField.setOnAction(e -> sendMessage());
        
        sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        sendButton.setPrefWidth(80);
        sendButton.setOnAction(e -> sendMessage());
        
        inputPanel.getChildren().addAll(messageField, sendButton);
        HBox.setHgrow(messageField, Priority.ALWAYS);
        
        panel.getChildren().addAll(header, chatArea, inputPanel);
        return panel;
    }
    
    private void styleTextField(TextField field) {
        field.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-background-radius: 5; -fx-border-radius: 5;");
    }
    
    private void connectToServer() {
        String server = serverField.getText().trim();
        String portText = portField.getText().trim();
        username = usernameField.getText().trim();
        
        if (server.isEmpty() || portText.isEmpty()) {
            showAlert("Error", "Please enter server address and port");
            return;
        }
        
        if (username.isEmpty()) {
            showAlert("Error", "Please enter a username");
            return;
        }
        
        if (username.length() > 20) {
            showAlert("Error", "Username must be less than 20 characters");
            return;
        }
        
        try {
            int port = Integer.parseInt(portText);
            client = new ChatClient(server, port);
            client.setMessageListener(this);
            
            updateStatus("Connecting to " + server + ":" + port + "...");
            connectButton.setDisable(true);
            
            if (client.connect(username)) {
                loginPanel.setVisible(false);
                chatPanel.setVisible(true);
                messageField.requestFocus();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid port number");
            connectButton.setDisable(false);
        }
    }
    
    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && client != null && client.isConnected()) {
            client.sendMessage(username, message);
            messageField.clear();
        }
    }
    
    private void disconnect() {
        if (client != null) {
            client.disconnect();
        }
        chatPanel.setVisible(false);
        loginPanel.setVisible(true);
        connectButton.setDisable(false);
        chatArea.clear();
        updateStatus("Disconnected from server");
    }
    
    private void appendToChat(String text) {
        Platform.runLater(() -> {
            chatArea.appendText(text + "\n");
            // Auto-scroll to bottom
            chatArea.setScrollTop(Double.MAX_VALUE);
        });
    }
    
    private void updateStatus(String status) {
        Platform.runLater(() -> {
            statusLabel.setText(status);
        });
    }
    
    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
    
    // MessageListener implementation
    @Override
    public void onMessageReceived(Message message) {
        appendToChat(message.toString());
    }
    
    @Override
    public void onConnectionStatusChanged(boolean connected, String message) {
        Platform.runLater(() -> {
            if (connected) {
                updateStatus("Connected: " + message);
                appendToChat("✅ " + message);
            } else {
                updateStatus("Disconnected: " + message);
                appendToChat("❌ " + message);
                if (!loginPanel.isVisible()) {
                    disconnect();
                }
            }
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}