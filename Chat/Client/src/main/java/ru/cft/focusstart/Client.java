package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.View.*;
import ru.cft.focusstart.dto.Communication;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.UserMessage;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private ConnectionView connectionView;
    private Connection connection;
    private AtomicReference<ChatView> chatView;
    private InfoView infoView;
    private List<String> userNames;
    private String userName;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        chatView = new AtomicReference<>();
        infoView = new InfoWindow();
        connectionView = new ConnectionWindow(this);
        connectionView.setDefaultAddress("localhost:1010");
        connectionView.showView();
    }

    public void connect(String serverAddress, String userName) {
        String[] address = serverAddress.split(":");
        if (address.length != 2) {
            infoView.showDialog("Неверно введён адрес сервера!");
            connectionView.showView();
            return;
        }

        this.userName = userName;

        try {
            connection = new Connection(address[0], Integer.valueOf(address[1]));
            connection.connect(this);
        } catch (NumberFormatException e) {
            infoView.showDialog("Порт должен быть числом!" + System.lineSeparator() + e.getMessage());
            connectionView.showView();
            return;
        } catch (ConnectException e) {
            infoView.showDialog(e.getMessage());
            connectionView.showView();
            return;
        }

        connectionView = null;
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
            infoView.showDialog(e.getMessage());
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
                    if (chatView.get() != null) {
                        chatView.get().addMessage(serverMessage.getMessage());
                        chatView.get().stopChat();
                    }
                    break;
                default:
                    LOGGER.info("Сервер прислал неожиданное событие! (" + serverMessage.getEvent() + ")");
            }
        } else {
            LOGGER.info("Пришло неизвестное сообщение!" + System.lineSeparator() + communication.getClass().getName());
        }
    }

}


