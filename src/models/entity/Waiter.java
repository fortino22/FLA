package models.entity;

import models.states.waiterstate.WaiterIdle;
import models.states.waiterstate.WaiterTakeOrder;

public class Waiter extends ParentEntity {
    private int speed;
    private Customer currentCustomer;
    private Chef assignedChef;

    public Waiter(String initial) {
        super(initial);
        this.speed = 1;
        setState(new WaiterIdle(this));
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void handleOrder(Customer customer) {
        if (isIdleState()) {
            this.currentCustomer = customer;
            transitionToOrderState(customer);
        }
    }

    public void setAssignedChef(Chef chef) {
        this.assignedChef = chef;
    }

    public Chef getAssignedChef() {
        return assignedChef;
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

    private boolean isIdleState() {
        return state instanceof WaiterIdle;
    }

    private void transitionToOrderState(Customer customer) {
        setState(new WaiterTakeOrder(this, customer.getInitial()));
    }
}
