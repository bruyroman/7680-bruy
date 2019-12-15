package ru.cft.focusstart.View;

import ru.cft.focusstart.Client;
import ru.cft.focusstart.dto.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.format.DateTimeFormatter;

public class ChatWindow extends JFrame implements ChatView {

    private Client client;
    private JPanel jPanelCenter;
    private JTextArea jtaMessages;
    private JScrollPane jspMessages;
    private JPanel jPanelRight;
    private JTextArea jtaUsers;
    private JPanel jPanelEnd;
    private JTextArea jtaUserMessage;
    private JScrollPane jspUserMessage;
    private JButton jbSendMessage;
    private DateTimeFormatter dateTimeFormatter;

    public ChatWindow() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yy");

        //Настройки окна
        setTitle("Чат");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout(10, 10));

        //Центральная панель
        jPanelCenter = new JPanel();
        jPanelCenter.setLayout(new BorderLayout());
        add(jPanelCenter, BorderLayout.CENTER);

        //Поле вывода сообщений
        jtaMessages = new JTextArea();
        jtaMessages.setLineWrap(true);
        jtaMessages.setEditable(false);
        jspMessages = new JScrollPane(jtaMessages,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPanelCenter.add(jspMessages, BorderLayout.CENTER);

        //Правая панель
        jPanelRight = new JPanel();
        jPanelRight.setLayout(new BorderLayout());
        jPanelRight.setPreferredSize(new Dimension(150, 0));
        add(jPanelRight, BorderLayout.LINE_END);

        //Поле вывода участников чата
        jtaUsers = new JTextArea();
        jtaUsers.setLineWrap(true);
        jtaUsers.setEditable(false);
        JScrollPane jspUsers = new JScrollPane(jtaUsers,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPanelRight.add(jspUsers, BorderLayout.CENTER);

        //Нижняя панель
        jPanelEnd = new JPanel();
        jPanelEnd.setPreferredSize(new Dimension(0, 70));
        jPanelEnd.setLayout(new BorderLayout());
        add(jPanelEnd, BorderLayout.PAGE_END);

        //Поле ввода сообщений
        jtaUserMessage = new JTextArea();
        jtaUserMessage.setLineWrap(true);
        jtaUserMessage.addKeyListener(new KeyListenerUserMessage());
        jspUserMessage = new JScrollPane(jtaUserMessage,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPanelEnd.add(jspUserMessage, BorderLayout.CENTER);

        //Кнопак отправить сообщение
        jbSendMessage = new JButton();
        jbSendMessage.setText("Отправить");
        jbSendMessage.setPreferredSize(new Dimension(150, 70));
        jbSendMessage.addActionListener(this::sendMessage);
        jPanelEnd.add(jbSendMessage, BorderLayout.LINE_END);

        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void onConnect() {
        setVisible(true);
        usersUpdate();
    }

    public void onMessageReceived(UserMessage userMessage) {
        addMessage(userMessage.getUserName() + " (" + dateTimeFormatter.format(userMessage.getDateTime()) + "):" + System.lineSeparator() + userMessage.getMessage());
    }

    public void onUsersUpdate(String message) {
        addMessage(message);
        usersUpdate();
    }

    public void onServerDisconnect(String message) {
        addMessage(message);
        jbSendMessage.setEnabled(false);
        jtaUserMessage.setEnabled(false);
        jtaUserMessage.setText("");
    }

    public void onException(String message) {
        new InfoWindow().showDialog(message);
    }

    private void addMessage(String message) {
        jtaMessages.append(message + System.lineSeparator() + System.lineSeparator());
    }

    private void usersUpdate() {
        jtaUsers.setText("Ваше имя: " + client.getUserName() + System.lineSeparator() + System.lineSeparator() +
                "В чате присутствуют:" + System.lineSeparator() +
                String.join(System.lineSeparator(), client.getConnectedUsers()));
    }

    private void sendMessage(ActionEvent e) {
        sendMessage();
    }

    private void sendMessage() {
        if (jtaUserMessage.getText().trim().length() != 0) {
            client.sendMessage(jtaUserMessage.getText().trim());
            jtaUserMessage.setText("");
            jtaUserMessage.setCaretPosition(0);
        }
        jtaUserMessage.grabFocus();
    }

    private class KeyListenerUserMessage implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 10) {
                e.consume();
                sendMessage();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}
    }

}
