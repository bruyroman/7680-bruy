package ru.cft.focusstart.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Person {

    private Long id;
    private String surname;
    private String name;
    private String patronymic;
    private LocalDate birthdate;
}
