package ru.cft.focusstart;

public class Resource {

    private static int counterId = 0;
    private final int id;

    public Resource() {
        id = getNewId();
    }

    private synchronized static int getNewId() {
        return counterId++;
    }

    @Override
    public String toString() {
        return "Ресурс №" + id;
    }
}
