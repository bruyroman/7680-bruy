package ru.cft.focusstart;

import ru.cft.focusstart.dto.Communication;
import ru.cft.focusstart.dto.ServerEvent;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class Connection {
    private String address;
    private Integer port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Connection(String address, Integer port) {
        this.address = address;
        this.port = port;
    }

    public void Open(User user) throws ConnectException {
        try {
            socket = new Socket(address, port);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            writer.println(Serialization.toJson(user));
            writer.flush();

            reader.ready();
            String json = reader.readLine();

            Communication communication = Serialization.fromJson(json);
            if (communication.getClass().getName() == ServerMessage.class.getName()) {

                ServerMessage serverMessage = (ServerMessage) communication;
                if (serverMessage.event == ServerEvent.ERROR) {
                    throw new ConnectException(serverMessage.getMessage());
                }

            } else {
                throw new ConnectException("Некорректный ответ от сервера!" + System.lineSeparator() + json);
            }

        } catch (IOException e) {
            throw new ConnectException("Сервер недоступен!" + System.lineSeparator() + e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    public void startMessageListener(Client client) {
        Thread messageListenerThread = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Communication communication = Serialization.fromJson(reader.readLine());
                    client.acceptMessage(communication);
                } catch (IOException e) {
                    System.out.println("Ошибка при приёме сообщения!" + System.lineSeparator() + e.getMessage());
                }
            }
        });
        messageListenerThread.start();
    }

    public void sendCommunication(Communication communication) {
        try {
            writer.println(Serialization.toJson(communication));
            writer.flush();
        } catch (IOException e) {
            System.out.println("Ошибка при отправке сообщения!" + System.lineSeparator() + e.getMessage());
        }
    }
}
