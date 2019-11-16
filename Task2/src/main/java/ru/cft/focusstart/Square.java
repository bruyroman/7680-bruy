package ru.cft.focusstart;

import java.text.DecimalFormat;

public class Square extends FigureRectangular {

    public static final String NAME = "Квадрат";
    public static final String CODE = "SQUARE";

    public Square(double sideLength) throws IllegalArgumentException {
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
    public String getInformation() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "Тип фигуры: " + getName() + System.lineSeparator() +
                "Площадь: " + df.format(getArea()) + System.lineSeparator() +
                "Периметр: " + df.format(getPerimeter()) + System.lineSeparator() +
                "Длина стороны: " + df.format(getSideLength()) + System.lineSeparator() +
                "Длина диагонали " + df.format(getLengthOfDiagonal());
    }

}
