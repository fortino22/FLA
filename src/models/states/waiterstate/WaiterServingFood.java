package models.states.waiterstate;

import models.entity.Waiter;
import models.entity.Customer;
import models.states.BaseState;
import models.states.customerstate.CustomerEat;

public class WaiterServingFood extends BaseState {
    private int servingTime = 1;

    public WaiterServingFood(Waiter waiter, String customerName) {
        super(waiter, customerName);
    }

    @Override
    public void update() {
        if (--servingTime <= 0) {
            Waiter waiter = (Waiter)entity;
            Customer customer = waiter.getCurrentCustomer();
            if (customer != null) {
                
                customer.setState(new CustomerEat(customer, waiter.getAssignedChef().getSkill()));
                
                waiter.setCurrentCustomer(null);
                waiter.setAssignedChef(null);
            }
            entity.setState(new WaiterIdle(waiter));
        }
    }

    @Override
    public String getStateName() {
        return String.format("serving to (%s)", customerName);
    }
}
