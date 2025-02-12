package models.states.restaurantstate;

import models.entity.Restaurant;
import helpers.Alert;

public class RestaurantPaused extends RestaurantState {
    @Override
    public void onEnter(Restaurant restaurant) {
        Alert.restaurantDebug("Restaurant entered Paused state");
        restaurant.getMediator().resumeAllOperations();
    }

    @Override
    public void update(Restaurant restaurant) {
        
    }

    @Override
    public String getStateName() {
        return "Paused";
    }
}
