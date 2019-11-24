package ru.cft.focusstart;

public class Task3 {

    public static final int WAREHOUSE_SIZE = 5;

    public static final int PRODUCER_THREADS = 10;
    public static final int PRODUCT_PRODUCTION_TIME = 2000;

    public static final int CONSUMERS_THREADS = 5;
    public static final int PRODUCT_CONSUMPTION_TIME = 1500;

    public static void main(String[] args) {
        ResourceWarehouse resourceWarehouse = new ResourceWarehouse(WAREHOUSE_SIZE);

        for (int i = 0; i < PRODUCER_THREADS; i++) {
            new Thread(new Producer(resourceWarehouse, PRODUCT_PRODUCTION_TIME)).start();
        }

        for (int i = 0; i < CONSUMERS_THREADS; i++) {
            new Thread(new Consumer(resourceWarehouse, PRODUCT_CONSUMPTION_TIME)).start();
        }
    }
}
