package views;

import models.entity.Restaurant;
import helpers.Cleaner;
import helpers.TableBuilder;

public class GameView {

    public void startGame() {
        this.start();
    }

    public void start() {

    }

    public void displayGame(Restaurant restaurant) {
        Cleaner.cls();
        displayRestaurantInfo(restaurant);
        displayEntities(restaurant);
    }

    private void displayRestaurantInfo(Restaurant restaurant) {
        System.out.println("═════════════════════════════");
        System.out.println("Restaurant: " + restaurant.getName());
        System.out.println("Money: Rp. " + restaurant.getMoney());
        System.out.println("Score: " + restaurant.getScore());
        System.out.println("Seats: " + restaurant.getSeats());
        System.out.println("═════════════════════════════\n");
    }

    private void displayEntities(Restaurant restaurant) {
        System.out.println(TableBuilder.CreateTable(restaurant.getCustomers(), restaurant.getWaiters(), restaurant.getChefs()));
    }
}
