package interfaces;

import models.entity.Restaurant;

public interface IRestaurantState {
    void update(Restaurant restaurant);
    void onEnter(Restaurant restaurant);
    void onExit(Restaurant restaurant);
    String getStateName();
}
