package models.factory;

import models.entity.Restaurant;

public class RestaurantFactory {
    private static RestaurantFactory instance;
    
    private RestaurantFactory() {}
    
    public static RestaurantFactory getInstance() {
        if(instance == null) {
            instance = new RestaurantFactory();
        }
        return instance;
    }
    
    public Restaurant createRestaurant(String name) {
        return new Restaurant(name);
    }
}
