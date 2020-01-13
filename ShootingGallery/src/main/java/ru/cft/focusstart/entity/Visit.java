package ru.cft.focusstart.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Visit {

    private Long id;
    private Person client;
    private Instructor instructor;
    private Weapon weapon;
    private LocalDateTime datetimeStart;
    private LocalDateTime datetimeEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getClient() {
        return client;
    }

    public void setClient(Person client) {
        this.client = client;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public LocalDateTime getDatetimeStart() {
        return datetimeStart;
    }

    public void setDatetimeStart(LocalDateTime datetimeStart) {
        this.datetimeStart = datetimeStart;
    }

    public LocalDateTime getDatetimeEnd() {
        return datetimeEnd;
    }

    public void setDatetimeEnd(LocalDateTime datetimeEnd) {
        this.datetimeEnd = datetimeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(id, visit.id) &&
                Objects.equals(client, visit.client) &&
                Objects.equals(instructor, visit.instructor) &&
                Objects.equals(weapon, visit.weapon) &&
                Objects.equals(datetimeStart, visit.datetimeStart) &&
                Objects.equals(datetimeEnd, visit.datetimeEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, instructor, weapon, datetimeStart, datetimeEnd);
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", client=" + client +
                ", instructor=" + instructor +
                ", weapon=" + weapon +
                ", datetimeStart='" + datetimeStart + '\'' +
                ", datetimeEnd='" + datetimeEnd + '\'' +
                '}';
    }
}
