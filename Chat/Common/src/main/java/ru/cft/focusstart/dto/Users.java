package ru.cft.focusstart.dto;

import java.util.ArrayList;
import java.util.List;

public class Users extends Communication {

    private List<User> users;

    public Users() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getUsers() {
        return users;
    }
}
