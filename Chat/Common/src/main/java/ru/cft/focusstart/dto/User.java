package ru.cft.focusstart.dto;

public class User extends Communication {

    private String name;
    private Events event;

    private User() {}

    public User(String name) {
        this.name = name;
        event = Events.NONE;
    }

    public String getName() {
        return name;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
       this.event = event;
    }

    public enum Events {
        NONE,
        JOINING,
        SUCCESS,
        CLOSE
    }
}
