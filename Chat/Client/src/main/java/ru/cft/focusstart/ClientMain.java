package ru.cft.focusstart;

import ru.cft.focusstart.View.ChatWindow;
import ru.cft.focusstart.View.ConnectionView;
import ru.cft.focusstart.View.ConnectionWindow;

public class ClientMain {

    public static void main(String[] args) {
        ChatWindow chatWindow = new ChatWindow();
        Client client = new Client(chatWindow);
        chatWindow.setClient(client);

        ConnectionView connectionView = new ConnectionWindow(client);
        connectionView.setDefaultAddress("localhost:1010");
        connectionView.showView();
    }

}
