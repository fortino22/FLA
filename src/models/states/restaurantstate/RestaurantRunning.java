package models.states.restaurantstate;

import models.entity.Restaurant;
import models.entity.Chef;
import models.entity.Waiter;
import models.entity.Customer;
import helpers.Alert;

public class RestaurantRunning extends RestaurantState {

    @Override
    public void onEnter(Restaurant restaurant) {
        if (restaurant.getMediator() != null) {
            restaurant.getMediator().resumeAllOperations();
        } else {
        }
    }

    @Override
    public void update(Restaurant restaurant) {
        if (restaurant.getMediator().isPaused()) {
            restaurant.setState(new RestaurantPaused());
            return;
        }

        updateEntities(restaurant);
    }

    @Override
    public String getStateName() {
        return "Running";
    }

    private void updateEntities(Restaurant restaurant) {
        restaurant.getChefs().forEach(Chef::update);
        restaurant.getWaiters().forEach(Waiter::update);
        restaurant.getCustomers().forEach(Customer::update);
    }
}
