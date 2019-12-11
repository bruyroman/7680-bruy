package ru.cft.focusstart;

import ru.cft.focusstart.dto.Communication;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class Connection {
    private String address;
    private Integer port;
    private Socket socket;
    private BufferedReader reader;
    private AtomicReference<PrintWriter> writer;
    private Thread messageListener;

    public Connection(String address, Integer port) {
        this.address = address;
        this.port = port;
    }

    public void connect(Client client) throws ConnectException {
        try {
            socket = new Socket(address, port);
            writer = new AtomicReference<>();
            writer.set(new PrintWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new ConnectException("Сервер недоступен!" + System.lineSeparator() + e.getMessage());
        }

        try {
            User user = new User(client.getUserName());
            user.setEvent(User.Events.JOINING);
            writer.get().println(Serialization.toJson(user));
            writer.get().flush();

            reader.ready();
            String json = reader.readLine();
            Communication communication = Serialization.fromJson(json);

            if (communication.getClass().getName() == ServerMessage.class.getName()) {
                ServerMessage serverMessage = (ServerMessage) communication;

                switch (serverMessage.getEvent()) {
                    case SUCCESS:
                        break;
                    case ERROR:
                        throw new ConnectException("Ошибка! " + serverMessage.getMessage());
                    default:
                        throw new ConnectException("Некорректный ответ от сервера! (" + serverMessage.getEvent() + ")" + System.lineSeparator() + serverMessage.getMessage());
                }
            } else {
                throw new ConnectException("Некорректный ответ от сервера!" + System.lineSeparator() + json);
            }

        } catch (IOException e) {
            throw new ConnectException("Сервер недоступен!" + System.lineSeparator() + e.getMessage());
        }
        messageListener = getMessageListener(client);
        messageListener.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                messageListener.interrupt();
                User user = new User(client.getUserName());
                user.setEvent(User.Events.CLOSE);
                writer.get().println(Serialization.toJson(user));
                writer.get().flush();
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }));
    }

    private Thread getMessageListener(Client client) {
       return new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Communication communication = Serialization.fromJson(reader.readLine());
                    client.acceptMessage(communication);
                } catch (IOException e) {
                    System.out.println("Ошибка при приёме сообщения!" + System.lineSeparator() + e.getMessage());
                }
            }
        });
    }


    public void sendCommunication(Communication communication) {
        try {
            writer.get().println(Serialization.toJson(communication));
            writer.get().flush();
        } catch (IOException e) {
            System.out.println("Ошибка при отправке сообщения!" + System.lineSeparator() + e.getMessage());
        }
    }
}
