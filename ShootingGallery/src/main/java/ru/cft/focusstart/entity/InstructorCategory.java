package ru.cft.focusstart.entity;

public enum InstructorCategory {
    ONE("Первая"),
    TWO("Вторая"),
    THREE("Третья"),
    HIGHEST("Высшая");

    private String name;

    InstructorCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
