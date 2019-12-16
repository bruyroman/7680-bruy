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

    private ChatView chatView;
    private Connection connection;
    private List<String> userNames;
    private String userName;

    public Client(ChatView chatView) {
        this.chatView = chatView;
    }

    public void connect(String serverAddress, String userName) throws ConnectException {
        String[] address = serverAddress.split(":");
        if (address.length != 2) {
            throw new ConnectException("Неверно введён адрес сервера!");
        }

        this.userName = userName;

        try {
            connection = new Connection(address[0], Integer.valueOf(address[1]));
            connection.connect(this);
        } catch (NumberFormatException e) {
            throw new ConnectException("Порт должен быть числом!" + System.lineSeparator() + e.getMessage());
        } catch (ConnectException e) {
            throw new ConnectException(e.getMessage());
        }
        chatView.onConnect();
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
            chatView.onException(e.getMessage());
        }
    }

    public synchronized void acceptMessage(Message message) {
        if (message instanceof UserMessage) {
            chatView.onMessageReceived((UserMessage) message);

        } else if (message instanceof ServerMessage) {
            ServerMessage serverMessage = (ServerMessage) message;
            switch (serverMessage.getEvent()) {
                case UPDATE_USERS:
                    userNames = serverMessage.getUserNames();
                    userNames.remove(userName);
                    chatView.onUsersUpdate(serverMessage.getMessage());
                    break;
                case CLOSE:
                    connection.close();
                    chatView.onServerDisconnect(serverMessage.getMessage());
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