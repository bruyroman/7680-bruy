package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.dto.Communication;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.User;
import ru.cft.focusstart.dto.UserMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private ServerSocket serverSocket;
    private AtomicReference<List<Client>> clients;
    private Integer port;
    private Thread messageListener;
    private Thread connectionListener;

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (SocketException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public Server() {
        clients = new AtomicReference<>();
        clients.set(new ArrayList<>());
    }

    public void start() throws SocketException {
        try {
            port = getPortFromResources();
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new SocketException("Не удалось открыть соединение!" + e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        messageListener = new Thread(this::runMessageListener);
        messageListener.start();
        connectionListener = new Thread(this::runConnectionListener);
        connectionListener.start();

        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("Введите EXIT для завершения работы сервера:");
            if (input.nextLine().toUpperCase().equals("EXIT")) {
                close();
                System.exit(0);
            }
        }
    }

    private static int getPortFromResources() throws IOException {
        try (InputStream propertiesStream = Server.class.getResourceAsStream("/server.properties")) {
            Properties properties = new Properties();
            if (propertiesStream != null) {
                properties.load(propertiesStream);
            }
            return Integer.valueOf(properties.getProperty("server.port"));
        } catch (NumberFormatException e) {
            throw new IOException("Порт должен быть числом!", e);
        } catch (IOException e) {
            throw new IOException("Не удалось получить порт из ресурсов!", e);
        }
    }

    public void close() {
        try {
            connectionListener.interrupt();
            serverSocket.close();
            messageListener.interrupt();

            String json = Serialization.toJson(new ServerMessage("Сервер завершил работу")
                    .setEvent(ServerMessage.Events.CLOSE));
            for (Client clientItem : clients.get()) {
                clientItem.sendMessage(json);
                clientItem.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void runConnectionListener() {
        boolean interrupted = false;
        while (!interrupted) {
            try {
                Socket clientSocket = serverSocket.accept();
                clients.get().add(new Client(clientSocket));
            } catch (IOException e) {
                interrupted = Thread.currentThread().isInterrupted();
                if (!interrupted) {
                    LOGGER.error("Произошла неудачная попытка подключения к серверу!" + System.lineSeparator() + e.getMessage());
                }
            }
        }
    }

    private void runMessageListener() {
        boolean interrupted = false;
        while (!interrupted) {
            try {
                Client client = null;
                for (Client clientItem : clients.get()) {
                    if (clientItem.haveMessage()) {
                        client = clientItem;
                        break;
                    }
                }

                if (client != null) {
                    processMessage(client);
                }
            } catch (IOException e) {
                LOGGER.error("Ошибка обработчика сообщений!" + System.lineSeparator() + e.getMessage());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }

    private void processMessage(Client client) throws IOException {
        String message = client.getMessage();
        Communication communication = Serialization.fromJson(message);

        if (communication.getClass().getName() == User.class.getName()) {
            User user = (User) communication;
            switch (user.getEvent()) {
                case JOINING:
                    client.setUserName(user.getName());
                    addClient(client);
                    break;
                case CLOSE:
                    removeClient(client);
                    break;
            }
        } else if (communication.getClass().getName() == UserMessage.class.getName() && client.getActivity()) {
            sendAllClientsMessage(communication);
        } else {
            client.sendMessage(Serialization.toJson(new ServerMessage("Сервер не ждал данные типа " + communication.getClass().getName() + " от данного клиента!")));
        }
    }

    private void addClient(Client client) throws IOException {
        if (client.getUserName().trim().length() == 0) {
            client.sendMessage(Serialization.toJson(new ServerMessage("Нельзя иметь пустое имя!").setEvent(ServerMessage.Events.ERROR)));
        } else if (getUserNames().contains(client.getUserName())) {
            client.sendMessage(Serialization.toJson(new ServerMessage("В чате уже существует пользователь с таким именем!").setEvent(ServerMessage.Events.ERROR)));
        } else {
            client.setActivity(true);
            client.sendMessage(Serialization.toJson(new ServerMessage("Подключение к серверу прошло успешно!").setEvent(ServerMessage.Events.SUCCESS)));

            sendAllClientsMessage(new ServerMessage("В чат добавлен новый собеседник с именем " + client.getUserName())
                    .setEvent(ServerMessage.Events.UPDATE_USERS)
                    .setUserNames(getUserNames()));
        }
    }

    private void removeClient(Client client) throws IOException {
        clients.get().remove(client);
        client.close();
        sendAllClientsMessage(new ServerMessage("Собеседник с именем  " + client.getUserName() + " вышел из чата")
                .setEvent(ServerMessage.Events.UPDATE_USERS)
                .setUserNames(getUserNames()));
    }

    private void sendAllClientsMessage(Communication communication) throws IOException {
        String json = Serialization.toJson(communication);
        for (Client clientItem : clients.get()) {
            if (clientItem.getActivity()) {
                clientItem.sendMessage(json);
            }
        }
    }

    private List<String> getUserNames() {
        List<String> userNames = new ArrayList<>();
        for (Client clientItem : clients.get()) {
            if (clientItem.getActivity()) {
                userNames.add(clientItem.getUserName());
            }
        }
        return userNames;
    }

}
