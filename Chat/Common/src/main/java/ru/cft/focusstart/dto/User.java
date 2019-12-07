package ru.cft.focusstart.dto;

public class User extends Communication {

    private String name;

    private User() {}

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
