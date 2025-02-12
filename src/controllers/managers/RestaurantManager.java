package controllers.managers;

public class RestaurantManager {
    private static RestaurantManager instance;
    private boolean isPaused;

    private RestaurantManager() {
        this.isPaused = false;
    }

    public static RestaurantManager getInstance() {
        if (instance == null) {
            synchronized (RestaurantManager.class) {
                if (instance == null) {
                    instance = new RestaurantManager();
                }
            }
        }
        return instance;
    }
}
