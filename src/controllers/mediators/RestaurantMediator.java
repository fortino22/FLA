package controllers.mediators;

import models.entity.Restaurant;
import models.entity.*;
import models.factory.CustomerFactory;
import models.states.chefstate.ChefIdle;
import models.states.waiterstate.WaiterIdle;
import interfaces.ICustomerObserver;
import helpers.Alert;

import java.util.*;
import java.util.HashSet;
import java.util.Set;

import controllers.observers.CustomerGenerator;

public class RestaurantMediator implements ICustomerObserver {
    private Restaurant restaurant;
    private boolean isPaused;
    private final Random Random;
    private final Set<Customer> CustomersBeingServed;
    private final Map<Customer, Chef> CustomerChefAssignments = new HashMap<>();
    
    public RestaurantMediator() {
        this.Random = new Random();
        this.CustomersBeingServed = new HashSet<>();
    }
    
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.setMediator(this);
        restaurant.initialize();

        initializeEntities(restaurant.getChefs());
        initializeEntities(restaurant.getWaiters());
        initializeEntities(restaurant.getCustomers());

        InitializeCustomerGenerator();
    }


    private <T extends ParentEntity> void initializeEntities(List<T> entities) {
        for (T entity : entities) {
            entity.setMediator(this);
        }
    }

    private void InitializeCustomerGenerator() {
        CustomerGenerator customerGenerator = CustomerGenerator.getInstance();
        customerGenerator.setRestaurant(restaurant);
        customerGenerator.addObserver(this);
        new Thread(customerGenerator).start();
    }

    @Override
    public void CustomerGenerator() {
        Customer customer = CustomerFactory.getInstance().createCustomer();
        customer.setMediator(this);
        restaurant.addCustomer(customer);
        processNewCustomer(customer);
    }

    public void updateEntities() {
        if (isPaused || restaurant == null) return;

        updateAllEntities(restaurant.getChefs());
        updateAllEntities(restaurant.getWaiters());
        updateAllEntities(restaurant.getCustomers());
        trySpawnCustomer();
    }

    private <T extends ParentEntity> void updateAllEntities(List<T> entities) {
        for (T entity : entities) {
            entity.update();
        }
    }
    
    private void trySpawnCustomer() {
        if (Random.nextInt(100) < 25 && restaurant.getCustomers().size() < restaurant.getSeats()) {
            Customer customer = CustomerFactory.getInstance().createCustomer();
            customer.setMediator(this);
            restaurant.addCustomer(customer);
            processNewCustomer(customer);
        }
    }

    private void processNewCustomer(Customer customer) {
        Alert.restaurantMediatorDebug("Processing new customer: " + customer.getInitial());
        customer.startOrdering();
        assignCustomerToWaiter(customer);
    }

    public void assignCustomerToWaiter(Customer customer) {
        if (CustomersBeingServed.contains(customer)) {
            return;
        }

        Alert.restaurantMediatorDebug("Looking for available waiter for " + customer.getInitial());
        for (Waiter waiter : restaurant.getWaiters()) {
            if (waiter.getState() instanceof WaiterIdle) {
                Alert.restaurantMediatorDebug("Found idle waiter: " + waiter.getInitial());
                CustomersBeingServed.add(customer);
                waiter.handleOrder(customer);
                return;
            }
        }
    }

    public void CustomerFinished(Customer customer) {
        Alert.restaurantMediatorDebug("Customer " + customer.getInitial() + " finished and is leaving");
        Chef assignedChef = CustomerChefAssignments.remove(customer);
        if (assignedChef != null) {
            assignedChef.finishOrder();
        }
        CustomersBeingServed.remove(customer);
        restaurant.removeCustomer(customer);
    }

    public void assignOrderToChef(Waiter waiter, Customer customer) {
        if (customer == null || CustomerChefAssignments.containsKey(customer)) {
            return;
        }
        
        Alert.restaurantMediatorDebug("Looking for available chef for customer " + customer.getInitial());
        for (Chef chef : restaurant.getChefs()) {
            if (chef.getState() instanceof ChefIdle && chef.getCurrentCustomer() == null) {
                Alert.restaurantMediatorDebug("Found idle chef: " + chef.getInitial());
                CustomerChefAssignments.put(customer, chef);
                waiter.setAssignedChef(chef);  
                chef.handleOrder(customer, waiter);
                return;
            }
        }
    }

    public void pauseAllOperations() {
        isPaused = true;
        for (Chef chef : restaurant.getChefs()) {
            chef.pause();
        }
        for (Waiter waiter : restaurant.getWaiters()) {
            waiter.pause();
        }
        for (Customer customer : restaurant.getCustomers()) {
            customer.pause();
        }
    }

    public void resumeAllOperations() {
        isPaused = false;
        for (Chef chef : restaurant.getChefs()) {
            chef.resume();
        }
        for (Waiter waiter : restaurant.getWaiters()) {
            waiter.resume();
        }
        for (Customer customer : restaurant.getCustomers()) {
            customer.resume();
        }
    }

    public void shutdown() {
        stopEntities(restaurant.getChefs());
        stopEntities(restaurant.getWaiters());
        stopEntities(restaurant.getCustomers());
    }

    private <T extends ParentEntity> void stopEntities(List<T> entities) {
        for (T entity : entities) {
            entity.stop();
        }
    }
    
    public boolean isPaused() {

        return isPaused;
    }
}
