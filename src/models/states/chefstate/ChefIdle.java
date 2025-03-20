package models.states.chefstate;

import models.entity.Chef;
import models.states.BaseState.StartingState;

public class ChefIdle extends StartingState {
    public ChefIdle(Chef chef) {
        super(chef);
    }

    @Override
    public void update() {
        
    }

    @Override
    public String getStateName() {
        return "idle";
    }

    @Override
    public void changeState(String customerName) {
        Chef chef = (Chef)entity;
        if (chef.getCurrentCustomer() == null) {  
            chef.setState(new ChefCooking((Chef)entity, customerName));
        } else {
        }
    }
}
