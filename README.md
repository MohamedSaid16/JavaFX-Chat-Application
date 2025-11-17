# 🎓 JavaFX Chat Application

A real-time multi-client **Chat Application** built with **Java** and **JavaFX**, featuring a **client-server architecture** and a modern graphical user interface.

---

## 🚀 Features

- Real-time messaging between multiple clients  
- Multi-client server support  
- Modern JavaFX GUI  
- Automatic join/leave notifications  
- Message timestamps  
- Real-time connection status  

---

## 🛠️ Technologies Used

- **Backend:** Java  
- **GUI:** JavaFX  
- **Build Tool:** Maven  
- **Architecture:** Client–Server Model  

---

## 📦 Project Structure

```
JavaFX-Chat-Application/
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
```

---

## 📋 Installation & Setup

### Method 1: Using Maven (Recommended)

Clone the repository:

```sh
git clone https://github.com/MohamedSaid16/JavaFX-Chat-Application.git  
cd JavaFX-Chat-Application
```

Build the project:

```sh
mvn clean compile
```

### Method 2: Manual Setup

- Install JavaFX SDK  
- Add JavaFX to your IDE module path  
- Add VM options:
  ```
  --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
  ```

---

## ▶️ Running the Application

### Step 1: Start the Server

Using Maven:

```sh
mvn compile exec:java -Dexec.mainClass="server.ChatServer"
```

Using Java:

```sh
java -cp target/classes server.ChatServer
```

Expected output:  
Chat Server started on port 12345

### Step 2: Start Client(s)

Using Maven:

```sh
mvn javafx:run
```

Using Java:

```sh
java --module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp target/classes client.ClientGUI
```

### Step 3: Connect and Chat

- Default server: localhost:12345  
- Enter a username  
- Click "Connect"  
- Start chatting!  

---

## 🧩 Core Components

### Server Side

- `ChatServer`: main server class  
- `ClientHandler`: manages each client  

### Client Side

- `ChatClient`: networking logic  
- `ClientGUI`: JavaFX UI  
- `Message`: data model  

---

## ⚙️ Configuration

### Change Server Port

In `ChatServer.java`:
```java
private static final int PORT = 12345;
```

### Customize UI

Edit:  
`resources/styles.css`

---

## 🔧 Troubleshooting

- JavaFX runtime components missing – Check SDK and module path  
- Connection refused – Make sure server is running and firewall allows port 12345  
- Username already taken – Choose another username  

---

## 🤝 Contributing

```sh
git checkout -b feature/NewFeature  
git commit -m "Add NewFeature"  
git push origin feature/NewFeature  
```

Open a Pull Request.
