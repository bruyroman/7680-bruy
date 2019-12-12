package ru.cft.focusstart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserMessage extends Communication {

    private String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    private LocalDateTime dateTime;
    private String message;

    private UserMessage() {}

    public UserMessage(String userName, LocalDateTime dateTime, String message) {
        this.userName = userName;
        this.dateTime = dateTime;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMessage() {
        return message;
    }
}
