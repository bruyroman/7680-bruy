package ru.cft.focusstart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.cft.focusstart.dto.Communication;

import java.io.IOException;

public final class Serialization {

    private Serialization() {}

    public static String toJson(Communication communication) throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.writeValueAsString(communication);
    }

    public static Communication fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return mapper.readValue(json, Communication.class);
    }
}
