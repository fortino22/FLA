package controllers.facades;

import controllers.mediators.RestaurantMediator;
import controllers.observers.CustomerGenerator;
import models.entity.Restaurant;
import models.entity.*;
import models.factory.ChefFactory;
import models.factory.WaiterFactory;
import helpers.CustomerInitials;

public class GameFacade {
    private static volatile GameFacade instance;
    private Restaurant restaurant;
    private RestaurantMediator mediator;
    
    private GameFacade() {
        mediator = new RestaurantMediator();
    }
    
    public static GameFacade getInstance() {
        if (instance == null) {
            instance = new GameFacade();
        }
        return instance;
    }
    
    public void initializeGame(String name) {
        restaurant = Restaurant.createRestaurant(name);
        mediator.setRestaurant(restaurant);
        setupInitialGameEnvironment();
    }
    
    public void pause() {

        mediator.pauseAllOperations();
    }
    
    public void resume() {

        mediator.resumeAllOperations();
    }
    
    public boolean isGamePaused() {

        return mediator.isPaused();
    }
    
    public void update() {
        if (restaurant != null) {
            restaurant.update();
            mediator.updateEntities();
        }
    }
    
    public Restaurant getCurrentRestaurant() {

        return restaurant;
    }

    private void setupInitialGameEnvironment() {
        if (restaurant != null) {
            for (int i = 0; i < CustomerInitials.defaultWaiter; i++) {
                addWaiter();
                addChef();
            }
        }
    }
    
    public void addWaiter() {
        if (restaurant != null) {
            Waiter waiter = WaiterFactory.getInstance().createWaiter();
            waiter.setMediator(mediator);
            restaurant.addWaiter(waiter);
        }
    }
    
    public void addChef() {
        if (restaurant != null) {
            Chef chef = ChefFactory.getInstance().createChef();
            chef.setMediator(mediator);
            restaurant.addChef(chef);
        }
    }
    
    public void terminate() {
        if (mediator != null) {
            mediator.shutdown();
        }
        CustomerGenerator.getInstance().stop();
    }
}
