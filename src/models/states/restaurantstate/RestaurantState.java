package models.states.restaurantstate;

import interfaces.IRestaurantState;
import models.entity.Restaurant;

public abstract class RestaurantState implements IRestaurantState {
    @Override
    public void onEnter(Restaurant restaurant) {

    }
    
    @Override
    public void onExit(Restaurant restaurant) {

    }
}
