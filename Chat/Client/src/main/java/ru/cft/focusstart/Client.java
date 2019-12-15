package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cft.focusstart.View.*;
import ru.cft.focusstart.dto.Message;
import ru.cft.focusstart.dto.ServerMessage;
import ru.cft.focusstart.dto.UserMessage;

import java.net.ConnectException;
import java.util.List;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private ConnectionView connectionView;
    private Connection connection;
    private ChatView chatView;
    private InfoView infoView;
    private List<String> userNames;
    private String userName;

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
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
        chatView = new ChatWindow(this);
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
        UserMessage userMessage = new UserMessage(userName, message, UserMessage.Events.CHAT_MESSAGE);
        try {
            connection.sendMessage(userMessage);
        } catch (Exception e) {
            infoView.showDialog(e.getMessage());
        }
    }

    public synchronized void acceptMessage(Message message) {
        if (message instanceof UserMessage) {
            UserMessage userMessage = (UserMessage) message;
            chatView.addMessage(userMessage.getUserName(), userMessage.getDateTime(), userMessage.getMessage());

        } else if (message instanceof ServerMessage) {
            ServerMessage serverMessage = (ServerMessage) message;
            switch (serverMessage.getEvent()) {
                case UPDATE_USERS:
                    userNames = serverMessage.getUserNames();
                    userNames.remove(userName);
                    if (chatView != null) {
                        chatView.addMessage(serverMessage.getMessage());
                        chatView.updateUsers();
                    }
                    break;
                case CLOSE:
                    connection.close();
                    if (chatView != null) {
                        chatView.addMessage(serverMessage.getMessage());
                        chatView.stopChat();
                    }
                    break;
                case PRESENCE_SURVEY:
                    connection.sendMessage(new UserMessage(userName, UserMessage.Events.ACTIVITY_CONFIRMATION));
                    break;
                default:
                    LOGGER.info("Сервер прислал неожиданное событие! (" + serverMessage.getEvent() + ")");
            }
        } else {
            LOGGER.info("Пришло неизвестное сообщение!" + System.lineSeparator() + message.getClass().getName());
        }
    }

}


