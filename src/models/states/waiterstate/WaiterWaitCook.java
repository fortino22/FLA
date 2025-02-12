package models.states.waiterstate;

import models.entity.Waiter;
import models.entity.Chef;
import models.states.BaseState;
import models.states.chefstate.ChefDone;

public class WaiterWaitCook extends BaseState {
    private boolean orderAssigned = false;

    public WaiterWaitCook(Waiter waiter, String customerName) {
        super(waiter, customerName);
        tryAssignToChef(waiter);
    }

    private void tryAssignToChef(Waiter waiter) {
        if (!orderAssigned && waiter.getCurrentCustomer() != null) {
            waiter.getMediator().assignOrderToChef(waiter, waiter.getCurrentCustomer());
        }
    }

    @Override
    public void update() {
        Waiter waiter = (Waiter)entity;
        Chef assignedChef = waiter.getAssignedChef();
        if (assignedChef != null && assignedChef.getState() instanceof ChefDone) {
            entity.setState(new WaiterBringOrder(waiter, waiter.getCurrentCustomer().getInitial()));
        }
    }

    @Override
    public String getStateName() {
        return String.format("waiting chef cooking(%s)", customerName);
    }
}
