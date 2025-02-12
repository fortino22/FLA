package models.entity;

import models.states.customerstate.CustomerOrder;

public class Customer extends ParentEntity {
    private static final int defaultTolarence = 10;
    private int tolerance;

    public Customer(String initial) {
        super(initial);
        this.tolerance = defaultTolarence;
    }

    public void startOrdering() {
        setState(new CustomerOrder(this));
    }

    public int getTolerance() {
        return tolerance;
    }

    public void reduceTolerance() {
        tolerance--;
        if (isToleranceExhausted()) {
            leaveRestaurant();
        }
    }

    private boolean isToleranceExhausted() {
        return tolerance <= 0;
    }

    private void leaveRestaurant() {
        System.out.println(getInitial() + " has left the restaurant due to lack of patience.");
    }

    @Override
    public String toString() {
        return String.format("(%d) %s", tolerance, state.getStateName());
    }
}
