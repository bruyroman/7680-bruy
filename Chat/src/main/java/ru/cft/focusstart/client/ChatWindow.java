package ru.cft.focusstart.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatWindow extends JFrame {

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

    public ChatWindow(Client client) {
        this.client = client;

        //Настройки окна
        setTitle(CharsetConverter.cp1251ToUtf8("Чат"));
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
        jbSendMessage.setText(CharsetConverter.cp1251ToUtf8("Отправить"));
        jbSendMessage.setPreferredSize(new Dimension(150, 70));
        jbSendMessage.addActionListener(this::sendMessage);
        jPanelEnd.add(jbSendMessage, BorderLayout.LINE_END);

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        updateUsers();
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

    private void sendMessage(ActionEvent e) {
        sendMessage();
    }

    private void sendMessage() {
        if (jtaUserMessage.getText().trim().length() != 0) {
            addMessage(client.getMyUserName(), CharsetConverter.utf8ToCp1251(jtaUserMessage.getText()).trim());
            client.sendMessage(CharsetConverter.utf8ToCp1251(jtaUserMessage.getText()).trim());
            jtaUserMessage.setText("");
            jtaUserMessage.setCaretPosition(0);
        }
        jtaUserMessage.grabFocus();
    }

    public void addMessage(String userName, String message) {
        jtaMessages.append(CharsetConverter.cp1251ToUtf8(userName + ":" + System.lineSeparator() + message + System.lineSeparator() + System.lineSeparator()));
    }

    public void connectUser(String name) {
        jtaMessages.append(CharsetConverter.cp1251ToUtf8("В чат добавлен новый собеседник с именем " + name + System.lineSeparator() + System.lineSeparator()));
        updateUsers();
    }

    public void disconnectUser(String name) {
        jtaMessages.append(CharsetConverter.cp1251ToUtf8("Собеседник с именем  " + name + " вышел из чата" + System.lineSeparator() + System.lineSeparator()));
        updateUsers();
    }

    private void updateUsers() {
        jtaUsers.setText(CharsetConverter.cp1251ToUtf8("Ваше имя: " + client.getMyUserName() + System.lineSeparator() + System.lineSeparator() +
                "В чате присутствуют:" + System.lineSeparator() +
                String.join(System.lineSeparator(), client.getConnectedUsers())));
    }
}
