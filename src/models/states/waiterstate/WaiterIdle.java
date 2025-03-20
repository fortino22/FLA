package models.states.waiterstate;

import models.entity.Waiter;
import models.states.BaseState.StartingState;

public class WaiterIdle extends StartingState {
    public WaiterIdle(Waiter waiter) {
        super(waiter);
    }

    @Override
    public void update() {

    }

    @Override
    public String getStateName() {
        return String.format("idle state", entity.getInitial());
    }

    @Override
    public void changeState(String customerName) {
        entity.setState(new WaiterTakeOrderFromCustomer((Waiter)entity, customerName));
    }
}
