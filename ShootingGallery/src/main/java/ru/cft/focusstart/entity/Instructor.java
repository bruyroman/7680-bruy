package ru.cft.focusstart.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
public class Instructor {

    private Long id;
    private Person person;
    private InstructorCategory category;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Visit> visits;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Weapon> weapons;
}
