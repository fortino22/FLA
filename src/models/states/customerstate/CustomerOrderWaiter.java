package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState;

public class CustomerOrderWaiter extends BaseState {

    public CustomerOrderWaiter(Customer customer, String waiterName) {
        super(customer, waiterName);
    }

    @Override
    public void update() {

    }

    @Override
    public String getStateName() {
        return String.format("%s - order (%s)", entity.getInitial(), customerName);
    }
    
    @Override
    public void changeState(String waiterName) {
        entity.setState(new CustomerWaitFood((Customer)entity));
    }
}
