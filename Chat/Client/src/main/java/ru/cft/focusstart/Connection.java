package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.dto.Message;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.UserMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;

public class Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private static final long MILLISECOND_VALID_SERVER_INACTIVITY_INTERVAL = 15000;

    private volatile LocalDateTime lastActivityServer;
    private String address;
    private Integer port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Thread messageListener;
    private Thread serverActivityListener;
    private Client client;

    public Connection(String address, Integer port) {
        this.address = address;
        this.port = port;
        lastActivityServer = LocalDateTime.now();
    }

    public void connect(Client client) throws ConnectException {
        try {
            socket = new Socket(address, port);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new ConnectException("Сервер недоступен!" + System.lineSeparator() + e.getMessage());
        }

        this.client = client;
        joiningUser();
        lastActivityServer = LocalDateTime.now();

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        messageListener = new Thread(this::runMessageListener);
        messageListener.start();
        serverActivityListener = new Thread(this::runServerActivityListener);
        serverActivityListener.start();
    }

    private void joiningUser() throws ConnectException {
        try {
            writer.println(Serialization.toJson(new UserMessage(client.getUserName(), UserMessage.Events.JOINING)));
            writer.flush();

            Message message = Serialization.fromJson(reader.readLine());
            if (message instanceof ServerMessage) {
                ServerMessage serverMessage = (ServerMessage) message;
                switch (serverMessage.getEvent()) {
                    case JOINING_SUCCESS:
                        break;
                    case JOINING_ERROR:
                        throw new ConnectException(serverMessage.getMessage());
                    default:
                        throw new ConnectException("Некорректный ответ от сервера! (" + serverMessage.getEvent() + ")" + System.lineSeparator() + serverMessage.getMessage());
                }
            } else {
                throw new ConnectException("Некорректный ответ от сервера!" + System.lineSeparator() + message.getClass().getName());
            }
        } catch (IOException e) {
            throw new ConnectException(e.getMessage());
        }
    }

    public void close() {
        try {
            messageListener.interrupt();
            writer.println(Serialization.toJson(new UserMessage(client.getUserName(), UserMessage.Events.CLOSE)));
            writer.flush();
            socket.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void runMessageListener() {
        boolean interrupted = false;
        while (!interrupted) {
            try {
                client.acceptMessage(Serialization.fromJson(reader.readLine()));
                lastActivityServer = LocalDateTime.now();
            } catch (IOException e) {
                interrupted = Thread.currentThread().isInterrupted() || socket.isClosed();
                if (!interrupted) {
                    LOGGER.error("Ошибка при приёме сообщения!" + System.lineSeparator() + e.getMessage());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        interrupted = true;
                    }
                }
            }
        }
    }

    private void runServerActivityListener() {
        boolean interrupted = false;
        while (!interrupted) {
            if (Duration.between(lastActivityServer, LocalDateTime.now()).getSeconds() * 1000 > MILLISECOND_VALID_SERVER_INACTIVITY_INTERVAL) {
                interrupted = true;
                client.acceptMessage(new ServerMessage("Сервер недоступен", ServerMessage.Events.CLOSE));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                interrupted = true;
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            writer.println(Serialization.toJson(message));
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Ошибка при отправке сообщения!" + System.lineSeparator() + e.getMessage());
        }
    }
}
