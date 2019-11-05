import java.util.Arrays;
import java.util.Scanner;

public class main {

    public static void main(String args[])
    {
        int sizeTable = getSizeTable();

        if (sizeTable <= 0 || sizeTable > 32){
            System.out.println("Введено некорректное значение!");
            return;
        }
        System.out.println(getMultiplicationTable(sizeTable));
    }

    private static int getSizeTable()
    {
        int sizeTable = -1;
        System.out.println("Введите размер таблицы (от 1 до 32): ");
        try (Scanner input = new Scanner(System.in)) {
            if (input.hasNextInt()){
                sizeTable = input.nextInt();
            }
        }
        return sizeTable;
    }

    private static String getMultiplicationTable(int size)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Integer cellLength = ((Integer) (size * size)).toString().length();
        String format = "%" + cellLength + "d";
        String dividingLine = getDividingLine(cellLength, size);

        for (int i = 0; i < size; i++) {
            if (i > 0){
                stringBuilder.append(dividingLine);
            }
            for (int j = 0; j < size; j++) {
                stringBuilder.append(String.format(format, (i + 1) * (j + 1)));
                if (j + 1 < size){
                    stringBuilder.append("|");
                }
            }
        }
        return stringBuilder.toString();
    }

    private static String getDividingLine(int cellLength, int countCells)
    {
        StringBuilder dividingLine = new StringBuilder();
        char[] block = new char[cellLength];
        Arrays.fill(block, '-');

        dividingLine.append(System.lineSeparator());
        for (int i = 1; i <= countCells; i++) {
            dividingLine.append(block);
            if (i != countCells) {
                dividingLine.append("+");
            }
        }
        dividingLine.append(System.lineSeparator());

        return dividingLine.toString();
    }
}