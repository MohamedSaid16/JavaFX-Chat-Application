package main;

import client.ClientGUI;
import server.ServerGUI;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
            // Run as server
            ServerGUI.main(args);
        } else {
            // Run as client (default)
            ClientGUI.main(args);
        }
    }
}