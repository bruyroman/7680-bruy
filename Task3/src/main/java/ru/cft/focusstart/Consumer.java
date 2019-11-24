package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private static int counterID = 0;
    private final int Id;
    private final int productConsumptionTime;
    private final ResourceWarehouse warehouse;

    public Consumer(ResourceWarehouse warehouse, int productConsumptionTime) {
        if (warehouse == null) {
            LOGGER.error("У потребителя склад не может быть равен null!");
        }

        if (productConsumptionTime < 0) {
            LOGGER.error("Время потребления продукта не может быть меньше нуля!");
        }

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
                Thread.sleep(0);
                LOGGER.info(Id + " потребитель, " + warehouse.pickUp().toString() + ", Ресурс потреблен");
            } catch (InterruptedException e) {
                LOGGER.error(Id + " потребитель: " + System.lineSeparator() + e);
            }
        }
    }
}