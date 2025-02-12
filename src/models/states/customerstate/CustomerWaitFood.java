package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState;

public class CustomerWaitFood extends BaseState {
    private int toleranceTimer = 0;

    public CustomerWaitFood(Customer customer) {
        super(customer);
    }

    @Override
    public void update() {
        if (++toleranceTimer >= 4) {  
            ((Customer)entity).reduceTolerance();
            toleranceTimer = 0;
        }
    }

    @Override
    public String getStateName() {
        return String.format("%s - wait food", entity.getInitial());
    }
    
    @Override
    public void changeState(String name) {
        entity.setState(new CustomerWaitFoodChef((Customer)entity, name));
    }

}
