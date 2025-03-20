package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState.StartingState;

public class CustomerWaitFoodFromChef extends StartingState {
    private int toleranceTimer = 0;

    public CustomerWaitFoodFromChef(Customer customer, String chefName) {
        super(customer, chefName);
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
        return String.format("%s - wait food (%s)", entity.getInitial(), customerName);
    }
}
