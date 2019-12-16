package ru.cft.focusstart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserMessage extends Message {

    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private LocalDateTime dateTime;
    private Events event;
    private String message;

    private UserMessage() {}

    public UserMessage(String userName, Events event) {
        this.userName = userName;
        this.dateTime = LocalDateTime.now();
        this.event = event;
        this.message = "";
    }

    public UserMessage(String userName, String message, Events event) {
        this.userName = userName;
        this.dateTime = LocalDateTime.now();
        this.event = event;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Events getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }

    public enum Events {
        JOINING,
        CHAT_MESSAGE,
        ACTIVITY_CONFIRMATION,
        CLOSE
    }
}
