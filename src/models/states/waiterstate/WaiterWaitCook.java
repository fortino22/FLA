package models.states.waiterstate;

import models.entity.Waiter;
import models.entity.Chef;
import models.entity.Customer;
import models.states.BaseState.StartingState;
import models.states.chefstate.ChefFinish;

public class WaiterWaitCook extends StartingState {
    private boolean orderAssigned;

    public WaiterWaitCook(Waiter waiter, String customerName) {
        super(waiter, customerName);
        this.orderAssigned = false;
        tryAssignToChef();
    }

    @Override
    public void update() {
        if (isChefFinishedCooking()) {
            transitionToBringOrderState();
        }
    }

    @Override
    public String getStateName() {
        return String.format("waiting chef cooking(%s)", customerName);
    }

    private void tryAssignToChef() {
        Waiter waiter = getWaiterEntity();
        Customer currentCustomer = waiter.getCurrentCustomer();

        if (!orderAssigned && currentCustomer != null) {
            waiter.getMediator().assignOrderToChef(waiter, currentCustomer);
        }
    }

    private boolean isChefFinishedCooking() {
        Chef assignedChef = getWaiterEntity().getAssignedChef();
        return assignedChef != null && assignedChef.getState() instanceof ChefFinish;
    }

    private void transitionToBringOrderState() {
        Waiter waiter = getWaiterEntity();
        Customer currentCustomer = waiter.getCurrentCustomer();
        String customerInitial = currentCustomer.getInitial();

        entity.setState(new WaiterBringOrder(waiter, customerInitial));
    }

    private Waiter getWaiterEntity() {
        return (Waiter) entity;
    }
}