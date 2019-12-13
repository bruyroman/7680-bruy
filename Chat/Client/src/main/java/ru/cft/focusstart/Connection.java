package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.dto.Communication;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    private static long MILLISECOND_VALID_SERVER_INACTIVITY_INTERVAL = 15000;

    private AtomicReference<LocalDateTime> lastActivityServer;
    private String address;
    private Integer port;
    private Socket socket;
    private BufferedReader reader;
    private AtomicReference<PrintWriter> writer;
    private Thread messageListener;
    private Thread serverActivityListener;
    private Client client;

    public Connection(String address, Integer port) {
        this.address = address;
        this.port = port;
        lastActivityServer = new AtomicReference<>();
        lastActivityServer.set(LocalDateTime.now());
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

        this.client = client;
        joiningUser();
        lastActivityServer.set(LocalDateTime.now());

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        messageListener = new Thread(this::runMessageListener);
        messageListener.start();
        serverActivityListener = new Thread(this::runServerActivityListener);
        serverActivityListener.start();
    }

    private void joiningUser() throws ConnectException {
        try {
            writer.get().println(Serialization.toJson(new User(client.getUserName())
                    .setEvent(User.Events.JOINING)));
            writer.get().flush();

            Communication communication = Serialization.fromJson(reader.readLine());

            if (communication.getClass().getName() == ServerMessage.class.getName()) {
                ServerMessage serverMessage = (ServerMessage) communication;
                switch (serverMessage.getEvent()) {
                    case SUCCESS:
                        break;
                    case ERROR:
                        throw new ConnectException(serverMessage.getMessage());
                    default:
                        throw new ConnectException("Некорректный ответ от сервера! (" + serverMessage.getEvent() + ")" + System.lineSeparator() + serverMessage.getMessage());
                }
            } else {
                throw new ConnectException("Некорректный ответ от сервера!" + System.lineSeparator() + communication.getClass().getName());
            }
        } catch (IOException e) {
            throw new ConnectException(e.getMessage());
        }
    }

    public void close() {
        try {
            messageListener.interrupt();
            writer.get().println(Serialization.toJson(new User(client.getUserName())
                    .setEvent(User.Events.CLOSE)));
            writer.get().flush();
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
                lastActivityServer.set(LocalDateTime.now());
            }
            catch (IOException e) {
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
            if (Duration.between(lastActivityServer.get(), LocalDateTime.now()).getSeconds() * 1000 > MILLISECOND_VALID_SERVER_INACTIVITY_INTERVAL) {
                interrupted = true;
                client.acceptMessage(new ServerMessage("Сервер недоступен").setEvent(ServerMessage.Events.CLOSE));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                interrupted = true;
            }
        }
    }

    public void sendCommunication(Communication communication) {
        try {
            writer.get().println(Serialization.toJson(communication));
            writer.get().flush();
        } catch (IOException e) {
            LOGGER.error("Ошибка при отправке сообщения!" + System.lineSeparator() + e.getMessage());
        }
    }
}
