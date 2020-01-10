package ru.cft.focusstart.entity;

import java.util.List;
import java.util.Objects;

public class Instructor {

    private Long id;
    private Person person;
    private InstructorCategory category;
    private List<Visit> visits;
    private List<Weapon> weapons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public InstructorCategory getCategory() {
        return category;
    }

    public void setCategory(InstructorCategory category) {
        this.category = category;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Instructor instructor = (Instructor) o;
        return Objects.equals(id, instructor.id) &&
                Objects.equals(person, instructor.person) &&
                Objects.equals(category, instructor.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, category);
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", person=" + person +
                ", category='" + category  + '\'' +
                '}';
    }
}
