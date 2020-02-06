package ru.cft.focusstart.entity;

import lombok.Data;

@Data
public class Weapon {

    private Long id;
    private Instructor instructor;
    private String type;
    private String model;
    private String series;
    private Integer number;
}
