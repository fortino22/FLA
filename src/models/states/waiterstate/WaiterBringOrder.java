package models.states.waiterstate;

import models.entity.Waiter;
import models.entity.Chef;
import models.states.BaseState;

public class WaiterBringOrder extends BaseState {
    private int bringTime = 1;

    public WaiterBringOrder(Waiter waiter, String customerName) {
        super(waiter, customerName);
        
        Chef chef = waiter.getAssignedChef();
        if (chef != null) {
            chef.getState().changeState(null);
        }
    }

    @Override
    public void update() {
        if (--bringTime <= 0) {
            entity.setState(new WaiterServingFood((Waiter)entity, customerName));
        }
    }

    @Override
    public String getStateName() {
        return String.format("bring order to (%s)", customerName);
    }
}
