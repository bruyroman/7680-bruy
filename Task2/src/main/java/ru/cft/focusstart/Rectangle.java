package ru.cft.focusstart;

public class Rectangle extends FigureRectangular {

    public static final String NAME = "Прямоугольник";
    public static final String CODE = "RECTANGLE";

    public Rectangle(double width, double length) {
        super(width, length);
    }

    @Override
    public String getName() {
        return NAME;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
}
