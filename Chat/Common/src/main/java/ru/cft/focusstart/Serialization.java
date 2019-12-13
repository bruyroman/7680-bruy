package ru.cft.focusstart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.cft.focusstart.dto.Message;

import java.io.IOException;

public final class Serialization {

    private Serialization() {}

    public static String toJson(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(message);
    }

    public static Message fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.readValue(json, Message.class);
    }
}
