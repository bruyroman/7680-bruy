package ru.cft.focusstart.View;

import ru.cft.focusstart.dto.UserMessage;

public interface ChatView {

    void onConnect();

    void onMessageReceived(UserMessage userMessage);

    void onUsersUpdate(String message);

    void onServerDisconnect(String message);

    void onException(String message);

}
