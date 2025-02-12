package models.states.restaurantstate;

import models.entity.Restaurant;
import helpers.Alert;

public class RestaurantClose extends RestaurantState {
    @Override
    public void onEnter(Restaurant restaurant) {
        Alert.restaurantDebug("Restaurant entered Close state");
        restaurant.getMediator().shutdown();
    }

    @Override
    public void update(Restaurant restaurant) {
        
    }

    @Override
    public String getStateName() {
        return "Closed";
    }
}
