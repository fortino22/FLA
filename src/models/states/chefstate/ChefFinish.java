package models.states.chefstate;

import models.entity.Chef;
import models.states.BaseState.StartingState;

public class ChefFinish extends StartingState {
    public ChefFinish(Chef chef, String customerName) {
        super(chef, customerName);
    }

    @Override
    public void update() {

    }

    @Override
    public String getStateName() {
        return String.format("finish cooking for (%s)", customerName);
    }

    @Override
    public void changeState(String customerName) {
        ((Chef)entity).finishOrder();
        entity.setState(new ChefIdle((Chef)entity));
    }


}
