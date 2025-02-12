package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState;

public class CustomerOrder extends BaseState {
    private int toleranceTimer = 0;
    
    public CustomerOrder(Customer customer) {
        super(customer);
        if (customer.getMediator() != null) {
            customer.getMediator().assignCustomerToWaiter(customer);
        } else {

        }
    }

    @Override
    public void update() {
        if (++toleranceTimer >= 2) {  
            ((Customer)entity).reduceTolerance();
            toleranceTimer = 0;

            if (entity.getMediator() != null) {
                entity.getMediator().assignCustomerToWaiter((Customer)entity);
            }
        }
    }

    @Override
    public String getStateName() {
        return "order";
    }
    
    @Override
    public void changeState(String waiterName) {
        entity.setState(new CustomerOrderWaiter((Customer)entity, waiterName));
    }
}
