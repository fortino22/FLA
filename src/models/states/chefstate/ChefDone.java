package models.states.chefstate;

import models.entity.Chef;
import models.states.BaseState;

public class ChefDone extends BaseState {
    public ChefDone(Chef chef, String customerName) {
        super(chef, customerName);
    }

    @Override
    public void update() {

    }

    @Override
    public String getStateName() {
        return String.format("finish(%s)", customerName);
    }

    @Override
    public void changeState(String customerName) {
        ((Chef)entity).finishOrder();
        entity.setState(new ChefIdle((Chef)entity));
    }


}
