package ru.cft.focusstart;

import ru.cft.focusstart.View.ChatView;
import ru.cft.focusstart.View.ChatWindow;
import ru.cft.focusstart.View.ConnectionView;
import ru.cft.focusstart.View.ConnectionWindow;
import ru.cft.focusstart.dto.*;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Client {

    private ConnectionView connectionView;
    private Connection connection;
    private AtomicReference<ChatView> chatView;
    private List<String> userNames;
    private String userName;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        chatView = new AtomicReference<>();
        connectionView = new ConnectionWindow(this);
        connectionView.setDefaultAddress("localhost:1010");
        connectionView.showView();
    }

    public void connect(String serverAddress, String userName) {
        String[] address = serverAddress.split(":");
        if (address.length != 2) {
            System.out.println("Неверно введён адрес сервера!");
            connectionView.showView();
            return;
        }

        this.userName = userName;

        try {
            connection = new Connection(address[0], Integer.valueOf(address[1]));
            connection.connect(this);
        } catch (NumberFormatException e) {
            System.out.println("Порт должен быть числом!" + System.lineSeparator() + e.getMessage());
            connectionView.showView();
            return;
        } catch (ConnectException e) {
            System.out.println(e.getMessage());
            connectionView.showView();
            return;
        }

        chatView.set(new ChatWindow(this));
    }

    public String getUserName() {
        return userName;
    }

    public String[] getConnectedUsers() {
        if (userNames != null && userNames.size() > 0) {
            return userNames.toArray(new String[0]);
        } else {
            return new String[]{};
        }
    }

    public void sendMessage(String message) {
        chatView.get().addMessage(userName, LocalDateTime.now(), message);
        try {
            connection.sendCommunication(new UserMessage(userName, LocalDateTime.now(), message));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void acceptMessage(Communication communication) {
        if (communication.getClass().getName() == UserMessage.class.getName()) {
            UserMessage userMessage = (UserMessage) communication;
            if (!userMessage.getUserName().equals(userName)) {
                chatView.get().addMessage(userMessage.getUserName(), userMessage.getDateTime(), userMessage.getMessage());
            }

        } else if (communication.getClass().getName() == ServerMessage.class.getName()) {
            ServerMessage serverMessage = (ServerMessage) communication;
            switch (serverMessage.getEvent()) {
                case UPDATE_USERS:
                    userNames = serverMessage.getUserNames();
                    userNames.remove(userName);
                    if (chatView.get() != null) {
                        chatView.get().addMessage(serverMessage.getMessage());
                        chatView.get().updateUsers();
                    }
                    break;
                case CLOSE:
                    connection.close();
                    userNames = new ArrayList<>();
                    if (chatView.get() != null) {
                        chatView.get().addMessage(serverMessage.getMessage());
                        chatView.get().updateUsers();
                    }
                    break;
                default:
                    System.out.println("Сервер прислал неожиданное событие! (" + serverMessage.getEvent() + ")");
            }
        } else {
            System.out.println("Пришло неизвестное сообщение!" + System.lineSeparator() + communication.getClass().getName());
        }
    }

}


