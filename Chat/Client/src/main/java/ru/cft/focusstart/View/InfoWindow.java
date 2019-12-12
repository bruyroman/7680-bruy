package ru.cft.focusstart.View;

import javax.swing.*;

public class InfoWindow extends JFrame implements InfoView {

    public InfoWindow() {
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(new InfoWindow(), message, "Внимание!", JOptionPane.INFORMATION_MESSAGE);
    }

}
