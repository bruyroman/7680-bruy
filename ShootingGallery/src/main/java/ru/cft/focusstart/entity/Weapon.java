package ru.cft.focusstart.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "\"WEAPON\"")
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name = "\"INSTRUCTOR_ID\"")
    private Instructor instructor;

    @Column(name = "\"TYPE\"", nullable = false, length = 100)
    private String type;

    @Column(name = "\"MODEL\"", nullable = false, length = 100)
    private String model;

    @Column(name = "\"SERIES\"", length = 100)
    private String series;

    @Column(name = "\"NUMBER\"", nullable = false)
    private Integer number;
}