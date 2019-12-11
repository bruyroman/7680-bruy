package ru.cft.focusstart;

import ru.cft.focusstart.dto.Communication;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.User;
import ru.cft.focusstart.dto.UserMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Server {
    private ServerSocket serverSocket;
    private AtomicReference<List<Client>> clients;
    private Integer port;
    private Thread messageListener;
    private Thread connectionListener;

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Server() {
        clients = new AtomicReference<>();
        clients.set(new ArrayList<>());
    }

    public void start() throws IOException {
        try {
            port = getPortFromResources();
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IOException("Не удалось открыть соединение!", e);
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
        Properties properties = new Properties();

        try (InputStream propertiesStream = Server.class.getResourceAsStream("/server.properties")) {
            if (propertiesStream != null) {
                properties.load(propertiesStream);
            }
            return Integer.valueOf(properties.getProperty("server.port"));
        } catch (IOException e) {
            throw new IOException("Не удалось получить порт из ресурсов!", e);
        }
    }

    public void close() {
        try {
            connectionListener.interrupt();
            serverSocket.close();
            messageListener.interrupt();
            ServerMessage message = new ServerMessage("Сервер завершил работу");
            message.setEvent(ServerMessage.Events.CLOSE);
            String json = Serialization.toJson(message);
            for (Client clientItem : clients.get()) {
                clientItem.sendMessage(json);
                clientItem.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
                    System.out.println("Произошла неудачная попытка подключения к серверу!" + System.lineSeparator() + e.getMessage());
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
                    if (clientItem.ready()) {
                        client = clientItem;
                        break;
                    }
                }

                if (client != null) {
                    processMessage(client);
                }

            } catch (IOException e) {
                System.out.println("Ошибка при подключении клиента!" + System.lineSeparator() + e.getMessage());
                System.out.println("Ошибка передачи сообщения!" + System.lineSeparator() + e.getMessage());
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }
    }

    private void processMessage(Client client) throws IOException {
        String message = client.readLine();
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

            UserMessage userMessage = (UserMessage) communication;
            for (Client clientItem : clients.get()) {
                if (!userMessage.userName.equals(clientItem.getUserName()) && client.getActivity()) {
                    clientItem.sendMessage(message);
                }
            }
        } else {
            client.sendMessage(Serialization.toJson(new ServerMessage("Сервер ждёт данные о пользователе (объект User)!").setEvent(ServerMessage.Events.ERROR)));
            System.out.println("Сервер ожидал UserMessage, а пришел - " + communication.getClass().getName());
        }
    }

    private void addClient(Client client) throws IOException {
        List<String> userNames = getUserNames();

        if (userNames.contains(client.getUserName())) {
            client.sendMessage(Serialization.toJson(new ServerMessage("В чате уже существует пользователь с таким именем!").setEvent(ServerMessage.Events.ERROR)));
        } else {
            client.setActivity(true);
            userNames.add(client.getUserName());
            client.sendMessage(Serialization.toJson(new ServerMessage("GOOD").setEvent(ServerMessage.Events.SUCCESS)));

            sendAllClientsMessage(new ServerMessage("В чат добавлен новый собеседник с именем " + client.getUserName())
                    .setEvent(ServerMessage.Events.UPDATE_USERS)
                    .setUsers(userNames));
        }
    }

    private void removeClient(Client client) throws IOException {
        clients.get().remove(client);
        client.close();
        ServerMessage serverMessage = new ServerMessage("Собеседник с именем  " + client.getUserName() + " вышел из чата").setEvent(ServerMessage.Events.UPDATE_USERS);
        serverMessage.setUsers(getUserNames());
        sendAllClientsMessage(serverMessage);
    }

    private void sendAllClientsMessage(ServerMessage serverMessage) throws IOException {
        String json = Serialization.toJson(serverMessage);
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


//TODO: ввести минутное пингование

}
