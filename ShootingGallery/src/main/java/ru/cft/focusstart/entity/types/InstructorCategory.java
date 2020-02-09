package ru.cft.focusstart.entity.types;

import lombok.Getter;

public enum InstructorCategory {
    ONE("ONE"),
    TWO("TWO"),
    THREE("THREE"),
    HIGHEST("HIGHEST");

    @Getter
    private final String name;

    InstructorCategory(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
