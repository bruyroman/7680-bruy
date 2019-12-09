package ru.cft.focusstart;

import ru.cft.focusstart.View.ChatView;
import ru.cft.focusstart.View.ChatWindow;
import ru.cft.focusstart.View.ConnectionView;
import ru.cft.focusstart.View.ConnectionWindow;
import ru.cft.focusstart.dto.*;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class Client {

    private ConnectionView connectionView;
    private Connection connection;
    private String userName;
    private AtomicReference<ChatView> chatView;
    private Users users = new Users();

    private User user;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        connectionView = new ConnectionWindow(this);
        connectionView.setDefaultAddress("localhost:1010");
        connectionView.showView();
    }

    public void connect(String serverAddress, String userName) {
        user = new User(userName);
        this.userName = userName;

        String[] address = serverAddress.split(":");
        if (address.length != 2) {
            System.out.println("Неверно введён адрес сервера!");
            connectionView.showView();
            return;
        }

        try {
            connection = new Connection(address[0], Integer.valueOf(address[1]));
            connection.Open(user);
        } catch (NumberFormatException e) {
            System.out.println("Порт должен быть числом!" + System.lineSeparator() + e.getMessage());
            connectionView.showView();
            return;
        } catch (ConnectException e) {
            System.out.println("Ошибка при подключении!" + System.lineSeparator() + e.getMessage());
            connectionView.showView();
            return;
        }

        chatView = new AtomicReference<>();
        chatView.set(new ChatWindow(this));

        connection.startMessageListener(this);
    }

    public String getMyUserName() {
        return userName;
    }

    public String[] getConnectedUsers() {
        if (users.getUsers().size() > 0) {
            return users.getUsersName().toArray(new String[0]);
        } else {
            return new String[]{};
        }
    }

    public void sendMessage(String message) {
        chatView.get().addMessage(userName, LocalDateTime.now(), message);
        connection.sendCommunication(new UserMessage(user, LocalDateTime.now(), message));
    }

    public void acceptMessage(Communication communication) {
        if (communication.getClass().getName() == UserMessage.class.getName()) {
            UserMessage userMessage = (UserMessage) communication;
            chatView.get().addMessage(userMessage.user.getName(), userMessage.dateTime, userMessage.message);

        } else if (communication.getClass().getName() == ServerMessage.class.getName()) {
            ServerMessage serverMessage = (ServerMessage) communication;
            chatView.get().addMessage(serverMessage.getMessage());

        } else if (communication.getClass().getName() == Users.class.getName()) {
            users.setUsers(((Users) communication).getUsers());
            chatView.get().updateUsers();

        } else {
            System.out.println("Пришло неизвестное сообщение!" + System.lineSeparator() + communication.getClass().getName());
        }
    }
}


