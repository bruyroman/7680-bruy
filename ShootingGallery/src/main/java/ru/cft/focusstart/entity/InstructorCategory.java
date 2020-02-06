package ru.cft.focusstart.entity;

import lombok.Getter;

public enum InstructorCategory {
    ONE("ONE"),
    TWO("TWO"),
    THREE("THREE"),
    HIGHEST("HIGHEST");

    @Getter
    private String name;

    InstructorCategory(String name) {
        this.name = name;
    }
}
