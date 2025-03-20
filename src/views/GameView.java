package views;

import models.entity.Restaurant;
import helpers.Cleaner;
import helpers.TableBuilder;

public class GameView {

    public void startGame() {
        start();
    }

    private void start() {
    }

    public void displayGame(Restaurant restaurant) {
        clearScreenAndDisplayInfo(restaurant);
        displayEntityTables(restaurant);
    }

    private void clearScreenAndDisplayInfo(Restaurant restaurant) {
        Cleaner.cls();
        displayRestaurantHeader(restaurant);
        displayRestaurantStats(restaurant);
        System.out.println();
    }

    private void displayRestaurantHeader(Restaurant restaurant) {
        System.out.println("╔═════════════════════════════╗");
        System.out.println("║  " + String.format("%-25s", "Restaurant: " + restaurant.getName()) + "  ║");
        System.out.println("╚═════════════════════════════╝");
    }

    private void displayRestaurantStats(Restaurant restaurant) {
        System.out.println("╔═════════════════════════════╗");
        System.out.println("║  " + String.format("%-25s", "Money: Rp. " + restaurant.getMoney()) + "  ║");
        System.out.println("║  " + String.format("%-25s", "Score: " + restaurant.getScore()) + "  ║");
        System.out.println("║  " + String.format("%-25s", "Seats: " + restaurant.getSeats()) + "  ║");
        System.out.println("╚═════════════════════════════╝");
    }

    private void displayEntityTables(Restaurant restaurant) {
        System.out.println(TableBuilder.CreateTable(
                restaurant.getCustomers(),
                restaurant.getWaiters(),
                restaurant.getChefs()
        ));
    }
}