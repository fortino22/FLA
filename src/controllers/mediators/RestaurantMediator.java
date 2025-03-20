package controllers.mediators;

import models.entity.Restaurant;
import models.entity.*;
import models.factory.CustomerFactory;
import models.states.chefstate.ChefIdle;
import models.states.waiterstate.WaiterIdle;
import interfaces.ICustomerObserver;
import helpers.Alert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import controllers.observers.CustomerGenerator;

public class RestaurantMediator implements ICustomerObserver {
    private Restaurant restaurant;
    private boolean isPaused;
    private final Random random;
    private final Set<Customer> customersBeingServed;
    private final Map<Customer, Chef> customerChefAssignments;

    private static final int CUSTOMER_SPAWN_CHANCE = 25;

    public RestaurantMediator() {
        this.random = new Random();
        this.customersBeingServed = ConcurrentHashMap.newKeySet();
        this.customerChefAssignments = new ConcurrentHashMap<>();
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        restaurant.setMediator(this);
        restaurant.initialize();

        initializeAllEntities();
        initializeCustomerGenerator();
    }

    private void initializeAllEntities() {
        initializeEntities(restaurant.getChefs());
        initializeEntities(restaurant.getWaiters());
        initializeEntities(restaurant.getCustomers());
    }

    private <T extends ParentEntity> void initializeEntities(List<T> entities) {
        entities.forEach(entity -> entity.setMediator(this));
    }

    private void initializeCustomerGenerator() {
        CustomerGenerator customerGenerator = CustomerGenerator.getInstance();
        customerGenerator.setRestaurant(restaurant);
        customerGenerator.addObserver(this);
        new Thread(customerGenerator).start();
    }

    @Override
    public void CustomerGenerator() {
        createAndProcessNewCustomer();
    }

    public void updateEntities() {
        if (isPaused || restaurant == null) return;

        updateAllEntities();
        trySpawnRandomCustomer();
    }

    private void updateAllEntities() {
        updateEntitiesList(restaurant.getChefs());
        updateEntitiesList(restaurant.getWaiters());
        updateEntitiesList(restaurant.getCustomers());
    }

    private <T extends ParentEntity> void updateEntitiesList(List<T> entities) {
        entities.forEach(ParentEntity::update);
    }

    private void trySpawnRandomCustomer() {
        if (random.nextInt(100) < CUSTOMER_SPAWN_CHANCE &&
                restaurant.getCustomers().size() < restaurant.getSeats()) {
            createAndProcessNewCustomer();
        }
    }

    private void createAndProcessNewCustomer() {
        Customer customer = CustomerFactory.getInstance().createCustomer();
        customer.setMediator(this);
        restaurant.addCustomer(customer);
        processNewCustomer(customer);
    }

    private void processNewCustomer(Customer customer) {
        customer.startOrdering();
        assignCustomerToWaiter(customer);
    }

    public void assignCustomerToWaiter(Customer customer) {
        if (customersBeingServed.contains(customer)) {
            return;
        }


        restaurant.getWaiters().stream()
                .filter(waiter -> waiter.getState() instanceof WaiterIdle)
                .findFirst()
                .ifPresent(waiter -> {
                    customersBeingServed.add(customer);
                    waiter.handleOrder(customer);
                });
    }

    public void CustomerFinished(Customer customer) {

        Chef assignedChef = customerChefAssignments.remove(customer);
        if (assignedChef != null) {
            assignedChef.finishOrder();
        }

        customersBeingServed.remove(customer);
        restaurant.removeCustomer(customer);
    }

    public void assignOrderToChef(Waiter waiter, Customer customer) {
        if (customer == null || customerChefAssignments.containsKey(customer)) {
            return;
        }


        restaurant.getChefs().stream()
                .filter(chef -> chef.getState() instanceof ChefIdle && chef.getCurrentCustomer() == null)
                .findFirst()
                .ifPresent(chef -> {
                    customerChefAssignments.put(customer, chef);
                    waiter.setAssignedChef(chef);
                    chef.handleOrder(customer, waiter);
                });
    }

    public void pauseOperations() {
        isPaused = true;
        executeForAllEntities(ParentEntity::pause);
    }

    public void resumeAllOperations() {
        isPaused = false;
        executeForAllEntities(ParentEntity::resume);
    }

    public void shutdown() {
        executeForAllEntities(ParentEntity::stop);
    }

    private void executeForAllEntities(java.util.function.Consumer<ParentEntity> operation) {
        restaurant.getChefs().forEach(operation);
        restaurant.getWaiters().forEach(operation);
        restaurant.getCustomers().forEach(operation);
    }

    public boolean isPaused() {
        return isPaused;
    }
}