package ru.cft.focusstart.repository;

public class DataAccessException extends RuntimeException {

    public DataAccessException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
