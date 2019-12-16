package ru.cft.focusstart.View;

import ru.cft.focusstart.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.ConnectException;

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

        jMenu = new JMenu("Файл");
        jMenuBar.add(jMenu);

        jExitItem = new JMenuItem("Выход");
        jExitItem.addActionListener(e -> System.exit(0));
        jMenu.add(jExitItem);

        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 2));
        add(jPanel);

        jlServerAddress = new JLabel("Введите адрес сервера ");
        jPanel.add(jlServerAddress);

        jtfServerAddress = new JTextField();
        jPanel.add(jtfServerAddress);

        jlUserName = new JLabel("Введите имя пользователя ");
        jPanel.add(jlUserName);

        jtfUserName = new JTextField();
        jPanel.add(jtfUserName);

        jbConnect = new JButton();
        jbConnect.setText("Подключиться");
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

    private void connect(ActionEvent e) {
        setVisible(false);
        try {
            client.connect(jtfServerAddress.getText(), jtfUserName.getText());
        } catch (ConnectException ex) {
            new InfoWindow().showDialog(ex.getMessage());
            setVisible(true);
        }
    }
}
