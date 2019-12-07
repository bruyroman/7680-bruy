package ru.cft.focusstart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UserMessage extends Communication {

    public User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy")
    public LocalDateTime dateTime;
    public String message;

    private UserMessage() {}

    public UserMessage(User user, LocalDateTime dateTime, String message) {
        this.user = user;
        this.dateTime = dateTime;
        this.message = message;
    }
}
