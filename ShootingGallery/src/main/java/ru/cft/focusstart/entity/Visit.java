package ru.cft.focusstart.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "VISIT")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "CLIENT_ID")
    private Person client;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name = "INSTRUCTOR_ID")
    private Instructor instructor;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name = "WEAPON_ID")
    private Weapon weapon;

    @Column(name = "DATETIME_START", nullable=false)
    private LocalDateTime datetimeStart;

    @Column(name = "DATETIME_END")
    private LocalDateTime datetimeEnd;
}