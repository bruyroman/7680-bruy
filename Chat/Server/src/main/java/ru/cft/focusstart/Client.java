package ru.cft.focusstart;

import ru.cft.focusstart.dto.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private User user;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public void close() throws IOException {
        socket.close();
    }

}
