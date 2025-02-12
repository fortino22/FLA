package models.states.chefstate;

import models.entity.Chef;
import models.states.BaseState;

public class ChefCook extends BaseState {
    private static final int cookingTIme = 6;
    private int remainingCookingTime;

    public ChefCook(Chef chef, String customerName) {
        super(chef, customerName);
        this.remainingCookingTime = cookingTIme - chef.getSpeed();
    }

    @Override
    public void update() {
        if (--remainingCookingTime <= 0) {
            transitionToChefDone();
        }
    }

    private void transitionToChefDone() {
        entity.setState(new ChefDone((Chef) entity, customerName));
    }

    @Override
    public String getStateName() {
        return String.format("cooking(%s)", customerName);
    }
}
