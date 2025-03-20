package models.entity;

import models.states.waiterstate.WaiterIdle;
import models.states.waiterstate.WaiterTakeOrderFromCustomer;


public class Waiter extends ParentEntity {
    private static final int DEFAULT_SPEED = 1;

    private int speed;
    private Customer currentCustomer;
    private Chef assignedChef;


    public Waiter(String initial) {
        super(initial);
        initializeDefaultAttributes();
    }

    private void initializeDefaultAttributes() {
        this.speed = DEFAULT_SPEED;
        setState(new WaiterIdle(this));
    }


    public void handleOrder(Customer customer) {
        if (isIdleState()) {
            assignCustomer(customer);
            transitionToOrderState(customer);
        }
    }


    private void assignCustomer(Customer customer) {
        this.currentCustomer = customer;
    }


    private boolean isIdleState() {
        return state instanceof WaiterIdle;
    }


    private void transitionToOrderState(Customer customer) {
        setState(new WaiterTakeOrderFromCustomer(this, customer.getInitial()));
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Chef getAssignedChef() {
        return assignedChef;
    }

    public void setAssignedChef(Chef chef) {
        this.assignedChef = chef;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    @Override
    public String toString() {
        return String.format("%s", state.getStateName());
    }
}