package ru.cft.focusstart.View;

import java.time.LocalDateTime;

public interface ChatView {

    void showView();

    void hideView();

    void addMessage(String userName, LocalDateTime dateTime, String message);

    void addMessage(String message);

    void updateUsers();

    void stopChat();
}
