package models.states.chefstate;

import models.entity.Chef;
import models.states.BaseState.StartingState;

public class ChefCooking extends StartingState {
    private static final int cookingTIme = 6;
    private int remainingCookingTime;

    public ChefCooking(Chef chef, String customerName) {
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
        entity.setState(new ChefFinish((Chef) entity, customerName));
    }

    @Override
    public String getStateName() {
        return String.format("cooking food for (%s)", customerName);
    }
}
