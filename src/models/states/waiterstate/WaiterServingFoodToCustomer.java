package models.states.waiterstate;

import models.entity.Waiter;
import models.entity.Customer;
import models.states.BaseState.StartingState;
import models.states.customerstate.CustomerEat;

public class WaiterServingFoodToCustomer extends StartingState {
    private static final int INITIAL_SERVING_TIME = 1;

    private int remainingServingTime;

    public WaiterServingFoodToCustomer(Waiter waiter, String customerName) {
        super(waiter, customerName);
        remainingServingTime = INITIAL_SERVING_TIME;
    }

    @Override
    public void update() {
        remainingServingTime--;

        if (remainingServingTime <= 0) {
            transitionToIdleState();
        }
    }

    @Override
    public String getStateName() {
        return String.format("serving to (%s)", customerName);
    }

    private void transitionToIdleState() {
        Waiter waiter = (Waiter) entity;
        updateCustomerState(waiter);
        resetWaiterAssignments(waiter);
        entity.setState(new WaiterIdle(waiter));
    }

    private void updateCustomerState(Waiter waiter) {
        Customer customer = waiter.getCurrentCustomer();
        if (customer != null) {
            int chefSkill = waiter.getAssignedChef().getSkill();
            customer.setState(new CustomerEat(customer, chefSkill));
        }
    }

    private void resetWaiterAssignments(Waiter waiter) {
        waiter.setCurrentCustomer(null);
        waiter.setAssignedChef(null);
    }
}