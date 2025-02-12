package interfaces;

import controllers.mediators.RestaurantMediator;

public interface IRestaurant {
    void setMediator(RestaurantMediator mediator);
    void pause();
    void resume();
    void stop();
    void update();
}


