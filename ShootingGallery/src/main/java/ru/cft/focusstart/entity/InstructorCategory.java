package ru.cft.focusstart.entity;

public enum InstructorCategory {
    ONE("ONE"),
    TWO("TWO"),
    THREE("THREE"),
    HIGHEST("HIGHEST");

    private String name;

    InstructorCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
