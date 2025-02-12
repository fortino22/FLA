package models.states.waiterstate;

import models.entity.Waiter;
import models.states.BaseState;

public class WaiterIdle extends BaseState {
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
        entity.setState(new WaiterTakeOrder((Waiter)entity, customerName));
    }
}
