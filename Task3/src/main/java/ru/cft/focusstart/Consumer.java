package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private static int counterId = 0;
    private final int id;
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
        id = getNewId();
        this.productConsumptionTime = productConsumptionTime;
    }

    private synchronized static int getNewId() {
        return counterId++;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Resource resource = warehouse.pickUp();
                Thread.sleep(productConsumptionTime);
                LOGGER.info(id + " потребитель, " + resource.toString() + ", Ресурс потреблен");
            } catch (InterruptedException e) {
                LOGGER.error(id + " потребитель: ", e);
            }
        }
    }
}