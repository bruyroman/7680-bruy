package ru.cft.focusstart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Task2 {

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                System.out.println("Должен быть как минимум один входной аргумент.");
                return;
            }

            String[] lines = getAllLinesFromFile(args[0]);
            if (args.length == 1) {
                System.out.println(getFigure2D(lines).toString());
            } else {
                saveInFile(args[1], getFigure2D(lines).toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String[] getAllLinesFromFile(String path) throws IllegalArgumentException, IOException {
        File file = new File(path);

        if (!file.isFile()) {
            throw new IllegalArgumentException("Указанный файл не найден.");
        }

        String[] lines;
        try (Stream<String> str = Files.lines(Paths.get(path))) {
            lines = str.toArray(value -> new String[value]);
        } catch (Exception e) {
            throw new IOException("Ошибка чтения указанного файла, проверьте чтоб он был символьным.", e);
        }
        return lines;
    }

    public static Figure2D getFigure2D(String[] params) throws IllegalArgumentException {

        if (params == null || params.length < 2) {
            throw new IllegalArgumentException("Количество входных параметров не должно быть менее двух.");
        }

        Figure2D figure2D;
        switch (params[0]) {
            case Circle.CODE:
                figure2D = new Circle(Integer.parseInt(params[1]));
                break;

            case Square.CODE:
                figure2D = new Square(Integer.parseInt(params[1]));
                break;

            case Rectangle.CODE:
                if (params.length < 3) {
                    throw new IllegalArgumentException("Для фигуры типа RECTANGLE количество входных параметров не должен быть менее трёх.");
                }
                figure2D = new Rectangle(Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                break;

            default:
                throw new IllegalArgumentException("Указанная фигура " + params[0] + " отсутствует.");
        }

        return figure2D;
    }

    public static void saveInFile(String path, String text) throws IOException {

        File file = new File(path);

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
            out.write(text);
        } catch (IOException e) {
            throw new IOException("Ошибка записи в файл.", e);
        }
    }
}

