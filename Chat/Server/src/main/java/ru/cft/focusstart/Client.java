package ru.cft.focusstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String userName;
    private boolean activity;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
        activity = false;
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
        return reader.readLine();
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public boolean getActivity() {
        return activity;
    }

}
