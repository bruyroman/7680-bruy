package ru.cft.focusstart.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "\"INSTRUCTOR\"")
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @OneToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "\"PERSON_ID\"")
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"CATEGORY\"", nullable=false)
    private InstructorCategory category;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "instructor", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Visit> visits;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "instructor", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Weapon> weapons;
}