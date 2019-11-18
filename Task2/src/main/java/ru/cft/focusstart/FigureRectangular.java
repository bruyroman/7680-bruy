package ru.cft.focusstart;

import java.text.DecimalFormat;

public abstract class FigureRectangular implements Figure2D {

    protected final double length;
    protected final double width;

    public FigureRectangular(double width, double length) throws IllegalArgumentException {
        if (width < 0 || length < 0) {
            throw new IllegalArgumentException("Входные значения не должны быть отрицательными.");
        }
        this.length = length;
        this.width = width;
    }

    @Override
    public double getArea() {
        return length * width;
    }

    @Override
    public double getPerimeter() {
        return (length + width) * 2;
    }

    public double getLengthOfDiagonal() {
        return Math.sqrt(length * length + width * width);
    }

    @Override
    public String getInformation() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "Тип фигуры: " + getName() + System.lineSeparator() +
                "Площадь: " + df.format(getArea()) + System.lineSeparator() +
                "Периметр: " + df.format(getPerimeter()) + System.lineSeparator() +
                "Длина: " + df.format(length) + System.lineSeparator() +
                "Ширина: " + df.format(width) + System.lineSeparator() +
                "Длина диагонали: " + df.format(getLengthOfDiagonal());
    }

    @Override
    public String toString() {
        return getInformation();
    }
}
