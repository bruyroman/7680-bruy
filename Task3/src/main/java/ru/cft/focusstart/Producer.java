package ru.cft.focusstart;

public class Producer implements Runnable {

    private static int counterID = 0;
    private final int Id;
    private final int productProductionTime;
    private final ResourceWarehouse warehouse;

    public Producer(ResourceWarehouse warehouse, int productProductionTime) {
        this.warehouse = warehouse;
        Id = getNewId();
        this.productProductionTime = productProductionTime;
    }

    private synchronized static int getNewId() {
        return counterID++;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(productProductionTime);
                Resource resource = new Resource();
                System.out.println(resource.toString() + " произведен производителем №" + Id);
                warehouse.put(resource);
            } catch (InterruptedException e) {
                System.out.println("Proizv " + Id + " Error 1");
                e.printStackTrace();
            }
        }
    }
}
