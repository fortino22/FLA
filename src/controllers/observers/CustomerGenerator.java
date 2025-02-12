package controllers.observers;

import models.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import interfaces.ICustomerObserver;

public class CustomerGenerator implements Runnable {
    private static CustomerGenerator instance;
    private List<ICustomerObserver> observers;
    private volatile boolean isRunning;
    private Random random;
    private Restaurant restaurant;
    
    private CustomerGenerator() {
        this.observers = new ArrayList<>();
        this.random = new Random();
        this.isRunning = true;
    }

    public static CustomerGenerator getInstance() {
        if (instance == null) {
            synchronized (CustomerGenerator.class) {
                if (instance == null) {
                    instance = new CustomerGenerator();
                }
            }
        }
        return instance;
    }
    
    public void setRestaurant(Restaurant restaurant) {

        this.restaurant = restaurant;
    }

    public void addObserver(ICustomerObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    @Override
    public void run() {
        while (isRunning) {
            if (random.nextInt(100) < 25 && ShouldGenerateCustomer()) {
                notifyObservers();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    private boolean ShouldGenerateCustomer() {
        return random.nextInt(100) < 25 && CanGenerateCustomer();
    }

    private boolean CanGenerateCustomer() {
        return restaurant != null &&
                restaurant.getCustomers().size() < restaurant.getSeats();
    }
    
    private void notifyObservers() {
        for (ICustomerObserver observer : observers) {
            observer.CustomerGenerator();
        }
    }
    
    public void stop() {
        isRunning = false;
    }
}
