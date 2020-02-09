package ru.cft.focusstart.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "\"PERSON\"")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"SURNAME\"", nullable = false, length = 100)
    private String surname;

    @Column(name = "\"NAME\"", nullable = false, length = 100)
    private String name;

    @Column(name = "\"PATRONYMIC\"", length = 100)
    private String patronymic;

    @Column(name = "\"BIRTHDATE\"", nullable = false, length = 100)
    private LocalDate birthdate;
}