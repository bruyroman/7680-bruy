package ru.cft.focusstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceWarehouse {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceWarehouse.class);

    private final int size;
    private final Resource[] resources;
    private Integer spaceTaken;

    public ResourceWarehouse(int size) {
        if (size < 1) {
            LOGGER.error("Склад не может иметь вместимость менее одного ресурса!");
        }

        this.size = size;
        resources = new Resource[size];
        spaceTaken = 0;
    }

    public synchronized void put(Resource resource) throws InterruptedException {
        while (spaceTaken == size) {
            wait();
        }

        Integer i = spaceTaken++;
        resources[i] = resource;
        LOGGER.info("Склад, " + resource.toString() + ", Ресурс помещен на склад");
        notify();
    }

    public synchronized Resource pickUp() throws InterruptedException {
        while (spaceTaken == 0) {
            wait();
        }

        Integer i = --spaceTaken;
        Resource resource = resources[i];
        resources[i] = null;
        LOGGER.info("Склад, " + resource.toString() + ", Ресурс забран со склада");
        notify();
        return resource;
    }
}
