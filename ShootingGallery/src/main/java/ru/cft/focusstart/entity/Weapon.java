package ru.cft.focusstart.entity;

import java.util.Objects;

public class Weapon {

    private Long id;
    private Instructor instructor;
    private String type;
    private String model;
    private String series;
    private Integer number;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weapon weapon = (Weapon) o;
        return Objects.equals(id, weapon.id) &&
                Objects.equals(instructor, weapon.instructor) &&
                Objects.equals(type, weapon.type) &&
                Objects.equals(model, weapon.model) &&
                Objects.equals(series, weapon.series) &&
                Objects.equals(number, weapon.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instructor, type, model, series, number);
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "id=" + id +
                ", instructor=" + instructor +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", series='" + series + '\'' +
                ", number=" + number +
                '}';
    }
}
