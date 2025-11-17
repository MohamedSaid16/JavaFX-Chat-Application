package server;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ServerGUI extends Application {
    private ChatServer server;
    private Thread serverThread;
    private TextArea logArea;
    private Button startButton;
    private Button stopButton;
    private Label statusLabel;
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Chat Server Administration");
        
        // Create UI components
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setPrefHeight(400);
        
        startButton = new Button("Start Server");
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setOnAction(e -> startServer());
        
        stopButton = new Button("Stop Server");
        stopButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        stopButton.setOnAction(e -> stopServer());
        stopButton.setDisable(true);
        
        statusLabel = new Label("Status: Stopped");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Layout
        HBox buttonBox = new HBox(10, startButton, stopButton, statusLabel);
        buttonBox.setPadding(new Insets(10));
        
        VBox root = new VBox(10, buttonBox, logArea);
        root.setPadding(new Insets(10));
        
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> stopServer());
        primaryStage.show();
    }
    
    private void startServer() {
        server = new ChatServer();
        serverThread = new Thread(() -> server.start());
        serverThread.setDaemon(true);
        serverThread.start();
        
        startButton.setDisable(true);
        stopButton.setDisable(false);
        updateStatus("Status: Running on port 12345");
        log("🚀 Server started successfully");
    }
    
    private void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
        
        startButton.setDisable(false);
        stopButton.setDisable(true);
        updateStatus("Status: Stopped");
        log("🛑 Server stopped");
    }
    
    private void updateStatus(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }
    
    private void log(String message) {
        Platform.runLater(() -> {
            logArea.appendText(message + "\n");
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}