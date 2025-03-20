package controllers.singleton;

public class RestaurantSingleton {
    private static RestaurantSingleton instance;
    private boolean isPaused;

    private RestaurantSingleton() {
        this.isPaused = false;
    }

    public static RestaurantSingleton getInstance() {
        if (instance == null) {
            synchronized (RestaurantSingleton.class) {
                if (instance == null) {
                    instance = new RestaurantSingleton();
                }
            }
        }
        return instance;
    }
}
