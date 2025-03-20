package models.entity;

import interfaces.IRestaurantState;
import models.factory.RestaurantFactory;
import models.states.restaurantstate.RestaurantRunning;
import controllers.mediators.RestaurantMediator;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a restaurant in the simulation.
 * Uses the Singleton pattern to ensure only one active restaurant exists.
 */
public class Restaurant {
    private static Restaurant activeRestaurant;

    private final CopyOnWriteArrayList<Chef> chefs;
    private final CopyOnWriteArrayList<Waiter> waiters;
    private final CopyOnWriteArrayList<Customer> customers;

    private String name;
    private int money;
    private int score;
    private int seats;

    private IRestaurantState state;
    private RestaurantMediator mediator;


    public Restaurant(String name) {
        this.name = name;
        this.money = 1500;
        this.score = 0;
        this.seats = 4;
        this.chefs = new CopyOnWriteArrayList<>();
        this.waiters = new CopyOnWriteArrayList<>();
        this.customers = new CopyOnWriteArrayList<>();
    }


    public static Restaurant createRestaurant(String name) {
        if (activeRestaurant == null) {
            activeRestaurant = RestaurantFactory.getInstance().createRestaurant(name);
        }
        return activeRestaurant;
    }


    public static Restaurant getActiveRestaurant() {
        return activeRestaurant;
    }


    public void initialize() {
        if (state == null) {
            setState(new RestaurantRunning());
        }
    }


    public void update() {
        if (state != null) {
            state.update(this);
        }
    }

    public CopyOnWriteArrayList<Chef> getChefs() {
        return chefs;
    }

    public void addChef(Chef chef) {
        chefs.add(chef);
    }

    public CopyOnWriteArrayList<Waiter> getWaiters() {
        return waiters;
    }

    public void addWaiter(Waiter waiter) {
        waiters.add(waiter);
    }

    public CopyOnWriteArrayList<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    public IRestaurantState getState() {
        return state;
    }


    public void setState(IRestaurantState newState) {
        handleStateExit();
        this.state = newState;
        handleStateEnter();
    }

    private void handleStateExit() {
        if (this.state != null) {
            this.state.onExit(this);
        }
    }

    private void handleStateEnter() {
        if (this.state != null) {
            this.state.onEnter(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public RestaurantMediator getMediator() {
        return mediator;
    }

    public void setMediator(RestaurantMediator mediator) {
        this.mediator = mediator;
    }
}