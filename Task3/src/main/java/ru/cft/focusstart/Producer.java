package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    private static int counterId = 0;
    private final int id;
    private final int productProductionTime;
    private final ResourceWarehouse warehouse;

    public Producer(ResourceWarehouse warehouse, int productProductionTime) {
        if (warehouse == null) {
            LOGGER.error("У производителя склад не может быть равен null!");
        }

        if (productProductionTime < 0) {
            LOGGER.error("Время производства продукта не может быть меньше нуля!");
        }

        this.warehouse = warehouse;
        id = getNewId();
        this.productProductionTime = productProductionTime;
    }

    private synchronized static int getNewId() {
        return counterId++;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(productProductionTime);
                Resource resource = new Resource();
                LOGGER.info(id + " производитель, " + resource.toString() + ", Ресурс произведен");
                warehouse.put(resource);
            } catch (InterruptedException e) {
                LOGGER.error(id + " производитель: ", e);
            }
        }
    }
}
