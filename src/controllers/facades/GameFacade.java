package controllers.facades;

import controllers.mediators.RestaurantMediator;
import controllers.observers.CustomerGenerator;
import models.entity.Restaurant;
import models.entity.*;
import models.factory.ChefFactory;
import models.factory.WaiterFactory;
import helpers.CustomerInitials;

public class GameFacade {
    private static volatile GameFacade gameInstance;
    private Restaurant restaurantModel;
    private RestaurantMediator restaurantMediator;

    private GameFacade() {
        restaurantMediator = new RestaurantMediator();
    }

    public static GameFacade getInstance() {
        if (gameInstance == null) {
            gameInstance = new GameFacade();
        }
        return gameInstance;
    }

    public Restaurant getCurrentRestaurant() {
        return restaurantModel;
    }

    public boolean isGamePaused() {
        return restaurantMediator.isPaused();
    }

    public void initializeGame(String name) {
        restaurantModel = Restaurant.createRestaurant(name);
        restaurantMediator.setRestaurant(restaurantModel);
        setupInitialGameEnvironment();
    }

    public void update() {
        if (restaurantModel != null) {
            restaurantModel.update();
            restaurantMediator.updateEntities();
        }
    }

    public void pause() {
        restaurantMediator.pauseOperations();
    }

    public void resume() {
        restaurantMediator.resumeAllOperations();
    }

    public void terminate() {
        if (restaurantMediator != null) {
            restaurantMediator.shutdown();
        }
        CustomerGenerator.getInstance().stop();
    }

    public void addChef() {
        if (restaurantModel != null) {
            Chef chef = ChefFactory.getInstance().createChef();
            chef.setMediator(restaurantMediator);
            restaurantModel.addChef(chef);
        }
    }

    public void addWaiter() {
        if (restaurantModel != null) {
            Waiter waiter = WaiterFactory.getInstance().createWaiter();
            waiter.setMediator(restaurantMediator);
            restaurantModel.addWaiter(waiter);
        }
    }

    private void setupInitialGameEnvironment() {
        if (restaurantModel != null) {
            for (int i = 0; i < CustomerInitials.defaultWaiter; i++) {
                addWaiter();
                addChef();
            }
        }
    }
}