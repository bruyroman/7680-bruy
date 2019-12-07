package ru.cft.focusstart.dto;

public class ServerMessage extends Communication {

    private String message;
    public ServerEvent event = ServerEvent.INFO;

    private ServerMessage() {}

    public ServerMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
