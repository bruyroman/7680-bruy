package ru.cft.focusstart;


import java.util.concurrent.atomic.AtomicInteger;

public class ResourceWarehouse {

    public final int size;
    private final Resource[] resources;
    private AtomicInteger spaceTaken;

    public ResourceWarehouse(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Склад не может иметь вместимость менее одного ресурса!");
        }
        this.size = size;
        resources = new Resource[size];
        spaceTaken = new AtomicInteger(0);
    }

    public synchronized void put(Resource resource) throws InterruptedException {
        while (spaceTaken.get() == size) {
            wait();
        }

        Integer i = spaceTaken.getAndIncrement();
        resources[i] = resource;
        System.out.println(resource.toString() + " помещён на склад");
        notify();
    }

    public synchronized Resource pickUp() throws InterruptedException {
        while (spaceTaken.get() == 0) {
            wait();
        }

        Integer i = spaceTaken.decrementAndGet();
        System.out.println(resources[i].toString() + " забран со склада");
        notify();
        return resources[i];
    }

}
