package ru.cft.focusstart;

public class Consumer implements Runnable {

    private static int counterID = 0;
    private final int Id;
    private final int productConsumptionTime;
    private final ResourceWarehouse warehouse;

    public Consumer(ResourceWarehouse warehouse, int productConsumptionTime) {
        this.warehouse = warehouse;
        Id = getNewId();
        this.productConsumptionTime = productConsumptionTime;
    }

    private synchronized static int getNewId() {
        return counterID++;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(productConsumptionTime);
                System.out.println(warehouse.pickUp().toString() + " потреблен потребителем №" + Id);
            } catch (InterruptedException e) {
                System.out.println("Potreb " + Id + " Error 1");
                e.printStackTrace();
            }
        }
    }
}