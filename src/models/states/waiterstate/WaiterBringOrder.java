package models.states.waiterstate;

import models.entity.Waiter;
import models.entity.Chef;
import models.states.BaseState.StartingState;

public class WaiterBringOrder extends StartingState {
    private static final int INITIAL_BRING_TIME = 1;

    private int remainingBringTime;

    public WaiterBringOrder(Waiter waiter, String customerName) {
        super(waiter, customerName);
        remainingBringTime = INITIAL_BRING_TIME;

        releaseAssignedChef(waiter);
    }

    @Override
    public void update() {
        remainingBringTime--;

        if (remainingBringTime <= 0) {
            transitionToServingFood();
        }
    }

    @Override
    public String getStateName() {
        return String.format("bring order to (%s)", customerName);
    }

    private void releaseAssignedChef(Waiter waiter) {
        Chef chef = waiter.getAssignedChef();
        if (chef != null) {
            chef.getState().changeState(null);
        }
    }

    private void transitionToServingFood() {
        Waiter waiter = (Waiter) entity;
        entity.setState(new WaiterServingFoodToCustomer(waiter, customerName));
    }
}