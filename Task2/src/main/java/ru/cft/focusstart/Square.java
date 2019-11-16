package ru.cft.focusstart;

public class Square extends FigureRectangular {

    public static final String NAME = "Квадрат";
    public static final String CODE = "SQUARE";

    public Square(double sideLength) {
        super(sideLength, sideLength);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public double getSideLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Тип фигуры: " + getName() + System.lineSeparator() +
                "Площадь: " + getArea() + System.lineSeparator() +
                "Периметр: " + getPerimeter() + System.lineSeparator() +
                "Длина стороны: " + getSideLength() + System.lineSeparator() +
                "Длина диагонали " + getLengthOfDiagonal();
    }
}
