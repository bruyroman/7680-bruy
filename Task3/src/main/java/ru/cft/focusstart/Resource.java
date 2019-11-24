package ru.cft.focusstart;

public class Resource {

    private static int counterID = 0;
    private final int Id;

    public Resource() {
        Id = getNewId();
    }

    private synchronized static int getNewId() {
        return counterID++;
    }

    @Override
    public String toString() {
        return "Ресурс №" + Id;
    }
}
