package ru.cft.focusstart;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Client {

    private String userName;
    private ChatWindow chatWindow;
    private ArrayList<String> users = new ArrayList<String>();

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        new ConnectionWindow(this);
    }

    public void connect(String serverAddress, String userName) {
        //TODO:Здесь инициируем подключение (выводим ошибки (север недоступен, имя занято))
        this.userName = userName;
        chatWindow = new ChatWindow(this);
    }

    public String getMyUserName() {
        return userName;
    }

    public String[] getConnectedUsers() {
        if (users.size() > 0) {
            return users.toArray(new String[0]);
        } else {
            return new String[]{};
        }
    }

    public void sendMessage(String message) {
        //TODO:Здесь создаём экземпляр класса "Сообщение" и отправляем его на сервер
        chatWindow.addMessage(userName, LocalDateTime.now(), message);
    }

    //TODO:Сделать метод принятия сообщения от сервера

}


