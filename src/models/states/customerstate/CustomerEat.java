package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState;
import models.entity.Restaurant;

public class CustomerEat extends BaseState {
    private int eatTime = 6;  
    private int chefSkill;
    private Restaurant restaurant;

    public CustomerEat(Customer customer, int chefSkill) {
        super(customer);
        this.chefSkill = chefSkill;
        this.restaurant = Restaurant.getActiveRestaurant();
    }

    @Override
    public void update() {
        if (--eatTime <= 0) {
            int reward = chefSkill * 30;
            restaurant.setScore(restaurant.getScore() + reward);
            restaurant.setMoney(restaurant.getMoney() + reward);
            ((Customer)entity).getMediator().CustomerFinished((Customer)entity);
        }
    }

    @Override
    public String getStateName() {
        return String.format("eating(%ds)", eatTime);
    }
}
