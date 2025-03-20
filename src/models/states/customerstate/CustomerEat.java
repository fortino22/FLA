package models.states.customerstate;

import models.entity.Customer;
import models.states.BaseState.StartingState;
import models.entity.Restaurant;

public class CustomerEat extends StartingState {
    private static final int DEFAULT_EAT_TIME = 6;
    private static final int REWARD_MULTIPLIER = 30;

    private int remainingEatTime;
    private final int chefSkillLevel;
    private final Restaurant currentRestaurant;

    public CustomerEat(Customer customer, int chefSkillLevel) {
        super(customer);
        this.chefSkillLevel = chefSkillLevel;
        this.currentRestaurant = Restaurant.getActiveRestaurant();
        this.remainingEatTime = DEFAULT_EAT_TIME;
    }

    @Override
    public void update() {
        remainingEatTime--;

        if (remainingEatTime <= 0) {
            processCustomerFinished();
        }
    }

    private void processCustomerFinished() {
        int reward = calculateReward();
        updateRestaurantStats(reward);
        notifyCustomerFinished();
    }

    private int calculateReward() {
        return chefSkillLevel * REWARD_MULTIPLIER;
    }

    private void updateRestaurantStats(int reward) {
        currentRestaurant.setScore(currentRestaurant.getScore() + reward);
        currentRestaurant.setMoney(currentRestaurant.getMoney() + reward);
    }

    private void notifyCustomerFinished() {
        Customer customer = (Customer) entity;
        customer.getMediator().CustomerFinished(customer);
    }

    @Override
    public String getStateName() {
        return String.format("eating(%ds)", remainingEatTime);
    }
}