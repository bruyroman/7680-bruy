package ru.cft.focusstart;

public abstract class FigureRectangular implements Figure2D {

    protected final double length;
    protected final double width;

    public FigureRectangular(double width, double length) {
        // проверка на отрицательные значения
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
    public String toString() {
        return "Тип фигуры: " + getName() + System.lineSeparator() +
                "Площадь: " + getArea() + System.lineSeparator() +
                "Периметр: " + getPerimeter() + System.lineSeparator() +
                "Длина: " + length + System.lineSeparator() +
                "Ширина: " + width + System.lineSeparator() +
                "Длина диагонали: " + getLengthOfDiagonal();
    }
}
