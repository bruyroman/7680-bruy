package ru.cft.focusstart.dto;

import java.util.List;

public class ServerMessage extends Message {

    private String message;
    private List<String> userNames;
    private Events event;

    private ServerMessage() {}

    public ServerMessage(String message, Events event) {
        this.event = event;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public ServerMessage setUserNames(List<String> usersName) {
        this.userNames = usersName;
        return this;
    }

    public Events getEvent() {
        return event;
    }

    public enum Events {
        JOINING_ERROR,
        JOINING_SUCCESS,
        ERROR,
        UPDATE_USERS,
        PRESENCE_SURVEY,
        CLOSE
    }
}
