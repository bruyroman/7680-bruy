package ru.cft.focusstart;

import java.util.Arrays;

public class MultiplicationTable {

    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 32;
    private int size;

    public MultiplicationTable(int size) throws IllegalArgumentException {
        sizeCheck(size);
        this.size = size;
    }

    private static void sizeCheck(int size) throws IllegalArgumentException {
        if (size < MIN_SIZE) {
            throw new IllegalArgumentException("Введено слишком маленькое значение!");
        }
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("Введено слишком большое значение!");
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        String format = "%" + getCellLength() + "d";
        String dividingLine = getDividingLine();

        for (int i = 0; i < size; i++) {
            if (i > 0) {
                stringBuilder.append(dividingLine);
            }

            for (int j = 0; j < size; j++) {
                stringBuilder.append(String.format(format, (i + 1) * (j + 1)));
                if (j + 1 < size) {
                    stringBuilder.append("|");
                }
            }
        }
        return stringBuilder.toString();
    }

    private Integer getCellLength() {
        return ((Integer) (size * size)).toString().length();
    }

    private String getDividingLine() {
        StringBuilder dividingLine = new StringBuilder();
        char[] block = new char[getCellLength()];
        Arrays.fill(block, '-');

        dividingLine.append(System.lineSeparator());
        for (int i = 1; i <= size; i++) {
            dividingLine.append(block);
            if (i != size) {
                dividingLine.append("+");
            }
        }
        dividingLine.append(System.lineSeparator());

        return dividingLine.toString();
    }
}
