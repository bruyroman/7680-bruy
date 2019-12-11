package ru.cft.focusstart.View;

import ru.cft.focusstart.CharsetConverter;
import ru.cft.focusstart.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConnectionWindow extends JFrame implements ConnectionView {

    private JMenuBar jMenuBar;
    private JMenu jMenu;
    private JMenuItem jExitItem;
    private JPanel jPanel;
    private JLabel jlServerAddress;
    private JTextField jtfServerAddress;
    private JLabel jlUserName;
    private JTextField jtfUserName;
    private JButton jbConnect;
    private Client client;

    public ConnectionWindow(Client client) {
        this.client = client;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        jMenuBar = new JMenuBar();
        setJMenuBar(jMenuBar);

        jMenu = new JMenu(CharsetConverter.cp1251ToUtf8("Файл"));
        jMenuBar.add(jMenu);

        jExitItem = new JMenuItem(CharsetConverter.cp1251ToUtf8("Выход"));
        jExitItem.addActionListener(e -> System.exit(0));
        jMenu.add(jExitItem);

        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 2));
        add(jPanel);

        jlServerAddress = new JLabel(CharsetConverter.cp1251ToUtf8("Введите адрес сервера "));
        jPanel.add(jlServerAddress);

        jtfServerAddress = new JTextField();
        jPanel.add(jtfServerAddress);

        jlUserName = new JLabel(CharsetConverter.cp1251ToUtf8("Введите имя пользователя "));
        jPanel.add(jlUserName);

        jtfUserName = new JTextField();
        jPanel.add(jtfUserName);

        jbConnect = new JButton();
        jbConnect.setText(CharsetConverter.cp1251ToUtf8("Подключиться"));
        jbConnect.addActionListener(this::connect);
        add(jbConnect);

        setLocationRelativeTo(null);
        setResizable(false);
        pack();
    }

    public void setDefaultAddress(String address) {
        jtfServerAddress.setText(address);
        jtfUserName.grabFocus();
    }

    public void showView() {
        setVisible(true);
    }

    public void hideView() {
        setVisible(false);
    }

    private void connect(ActionEvent e) {
        if (jtfServerAddress.getText().length() > 0 && jtfUserName.getText().length() > 0) {
            setVisible(false);
            client.connect(CharsetConverter.utf8ToCp1251(jtfServerAddress.getText()), CharsetConverter.utf8ToCp1251(jtfUserName.getText()));
        }
    }
}
