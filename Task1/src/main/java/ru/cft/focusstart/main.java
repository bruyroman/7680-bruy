package ru.cft.focusstart;

import java.util.Scanner;

public class main {

    public static void main(String args[]) {
        int sizeTable;
        System.out.println(String.format("Введите размер таблицы (от %d до %d): ", MultiplicationTable.MIN_SIZE, MultiplicationTable.MAX_SIZE));

        try (Scanner input = new Scanner(System.in)) {
            if (input.hasNextInt()){
                sizeTable = input.nextInt();
            } else {
                System.out.println("Введены неверные данные!");
                return;
            }
        }

        try {
            MultiplicationTable multiplicationTable = new MultiplicationTable(sizeTable);
            System.out.println(multiplicationTable.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}