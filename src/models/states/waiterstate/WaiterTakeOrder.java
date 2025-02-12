package models.states.waiterstate;

import models.entity.Waiter;
import models.states.BaseState;

public class WaiterTakeOrder extends BaseState {
    private int orderTime;

    public WaiterTakeOrder(Waiter waiter, String customerName) {
        super(waiter, customerName);
        this.orderTime = 6 - ((Waiter)entity).getSpeed();
    }

    @Override
    public void update() {
        if (--orderTime <= 0) {
            entity.setState(new WaiterWaitCook((Waiter)entity, customerName));
        }
    }

    @Override
    public String getStateName() {
        return String.format("taking order from (%s)", customerName);
    }
}