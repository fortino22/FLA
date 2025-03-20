package models.entity;

import models.states.chefstate.ChefIdle;
import models.states.chefstate.ChefCooking;

public class Chef extends ParentEntity {
    private int speed;
    private int skill;
    private Customer currentCustomer;

    public Chef(String initial) {
        super(initial);
        this.speed = 1;
        this.skill = 1;
        setState(new ChefIdle(this));
    }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getSkill() { return skill; }
    public void setSkill(int skill) { this.skill = skill; }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void handleOrder(Customer customer) {
        if (currentCustomer == null) {
            processOrderWithoutWaiter(customer);
        } else {
            processOrderWithExistingCustomer();
        }
    }

    public void handleOrder(Customer customer, Waiter waiter) {
        if (currentCustomer == null) {
            assignOrderToChef(customer, waiter);
        } else {
            processOrderWithExistingCustomer();
        }
    }

    private void processOrderWithoutWaiter(Customer customer) {
        state.changeState(customer.getInitial());
    }

    private void processOrderWithExistingCustomer() {
        System.out.println("Chef " + getInitial() + " is already working with a customer.");
    }

    private void assignOrderToChef(Customer customer, Waiter waiter) {
        System.out.println("Chef " + getInitial() + " received order from waiter " + waiter.getInitial());
        currentCustomer = customer;
        setState(new ChefCooking(this, customer.getInitial()));
    }

    public void finishOrder() {
        currentCustomer = null;
    }

    @Override
    public String toString() {

        return state.getStateName();
    }
}
