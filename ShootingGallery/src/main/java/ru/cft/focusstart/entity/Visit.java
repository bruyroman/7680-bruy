package ru.cft.focusstart.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Visit {

    private Long id;
    private Person client;
    private Instructor instructor;
    private Weapon weapon;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;
}
