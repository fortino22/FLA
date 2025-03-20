package models.states.waiterstate;

import models.entity.Waiter;
import models.states.BaseState.StartingState;

public class WaiterTakeOrderFromCustomer extends StartingState {
    private static final int BASE_ORDER_TIME = 6;

    private int remainingOrderTime;

    public WaiterTakeOrderFromCustomer(Waiter waiter, String customerName) {
        super(waiter, customerName);
        this.remainingOrderTime = calculateOrderTime(waiter);
    }

    @Override
    public void update() {
        remainingOrderTime--;

        if (remainingOrderTime <= 0) {
            transitionToWaitCookState();
        }
    }

    @Override
    public String getStateName() {
        return String.format("taking order from (%s)", customerName);
    }

    private int calculateOrderTime(Waiter waiter) {
        return BASE_ORDER_TIME - waiter.getSpeed();
    }

    private void transitionToWaitCookState() {
        Waiter waiter = (Waiter) entity;
        entity.setState(new WaiterWaitCook(waiter, customerName));
    }
}