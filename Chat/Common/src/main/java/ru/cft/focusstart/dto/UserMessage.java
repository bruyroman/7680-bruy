package ru.cft.focusstart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserMessage extends Communication {

    public String userName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    public LocalDateTime dateTime;
    public String message;

    private UserMessage() {}

    public UserMessage(String userName, LocalDateTime dateTime, String message) {
        this.userName = userName;
        this.dateTime = dateTime;
        this.message = message;
    }
}
