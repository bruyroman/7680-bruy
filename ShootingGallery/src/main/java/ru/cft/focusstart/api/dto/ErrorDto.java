package ru.cft.focusstart.api.dto;

import lombok.Value;

@Value
public class ErrorDto {

    private final int code;
    private final String message;

    public ErrorDto(ErrorCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }
}
