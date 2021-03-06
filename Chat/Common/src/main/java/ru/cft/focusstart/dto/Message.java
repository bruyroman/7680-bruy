package ru.cft.focusstart.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ServerMessage.class, name = "ServerMessage"),
        @JsonSubTypes.Type(value = UserMessage.class, name = "UserMessage")
})
public abstract class Message {}