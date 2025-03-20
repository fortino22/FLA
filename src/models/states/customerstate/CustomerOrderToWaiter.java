package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState.StartingState;

public class CustomerOrderToWaiter extends StartingState {

    public CustomerOrderToWaiter(Customer customer, String waiterName) {
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
