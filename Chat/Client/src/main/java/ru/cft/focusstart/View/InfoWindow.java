package ru.cft.focusstart.View;

import ru.cft.focusstart.CharsetConverter;

import javax.swing.*;

public class InfoWindow extends JFrame implements InfoView {

    public InfoWindow() {
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(new InfoWindow(), CharsetConverter.cp1251ToUtf8(message), CharsetConverter.cp1251ToUtf8("Внимание!"), JOptionPane.INFORMATION_MESSAGE);
    }

}
