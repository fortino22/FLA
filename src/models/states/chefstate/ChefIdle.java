package models.states.chefstate;

import models.entity.Chef;
import models.states.BaseState;

public class ChefIdle extends BaseState {
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
            chef.setState(new ChefCook((Chef)entity, customerName));
        } else {
        }
    }
}
