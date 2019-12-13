package ru.cft.focusstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;
    private LocalDateTime lastActivity;
    private boolean addedToChat;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        addedToChat = false;
        resetInactivity();
    }

    private void resetInactivity() {
        lastActivity = LocalDateTime.now();
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public boolean haveMessage() throws IOException {
        return reader.ready();
    }

    public String getMessage() throws IOException {
        String message = reader.readLine();
        resetInactivity();
        return message;
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void setAddedToChat(boolean value) {
        this.addedToChat = value;
    }

    public boolean isAddedToChat() {
        return addedToChat;
    }

    public long getInactiveTimeInMilliseconds() {
        return Duration.between(lastActivity, LocalDateTime.now()).getSeconds() * 1000;
    }

}
