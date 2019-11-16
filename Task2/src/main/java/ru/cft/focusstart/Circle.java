package ru.cft.focusstart;

public class Circle implements Figure2D {

    public static final String NAME = "Круг";
    public static final String CODE = "CIRCLE";

    private double radius;

    public Circle(double radius) throws IllegalArgumentException {
        if (radius < 0) {
            throw new IllegalArgumentException("Входные значения не должны быть отрицательными.");
        }
        this.radius = radius;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getDiameter() {
        return 2 * radius;
    }

    @Override
    public String getInformation() {
        return "Тип фигуры: " + getName() + System.lineSeparator() +
                "Площадь: " + getArea() + System.lineSeparator() +
                "Периметр: " + getPerimeter() + System.lineSeparator() +
                "Радиус: " + getRadius() + System.lineSeparator() +
                "Диаметр " + getDiameter();
    }

    @Override
    public String toString() {
        return getInformation();
    }
}
