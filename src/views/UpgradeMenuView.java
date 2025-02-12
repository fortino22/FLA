package views;

import models.entity.Restaurant;
import models.entity.*;
import helpers.Cleaner;
import helpers.Alert;

import java.util.concurrent.CopyOnWriteArrayList;

public class UpgradeMenuView {

    private static final String SEPARATOR = "═════════════════════════════";

    public UpgradeMenuView() {
        Alert.restaurantInitializationDebug("UpgradeMenuView initialized");
    }

    public void showUpgradeMenu(Restaurant restaurant) {
        if (restaurant == null) {
            Alert.restaurantInitializationDebug("Restaurant object is null in showUpgradeMenu.");
            return;
        }

        Cleaner.cls();
        printMenuHeaderFooter("Restaurant Upgrade Options");

        System.out.println("Restaurant Name: " + restaurant.getName());
        System.out.println("Current Balance: Rp. " + restaurant.getMoney());
        System.out.println("Current Score: " + restaurant.getScore());
        System.out.println("Available Seats: " + restaurant.getSeats());

        System.out.println("1. Expand Restaurant Seating (Cost: Rp. " + (100 * restaurant.getSeats()) + ")");
        System.out.println("2. Add a New Employee");
        System.out.println("3. Upgrade Waiter (Cost: Rp. 150)");
        System.out.println("4. Upgrade Chef (Cost: Rp. 150)");
        System.out.println("5. Return to Main Menu");

        printMenuHeaderFooter("");
    }

    public void showHireMenu(Restaurant restaurant) {
        Cleaner.cls();
        printMenuHeaderFooter("Employee Hiring Options");

        System.out.println("1. Hire a Waiter (Cost: Rp. " + (150 * (restaurant.getWaiters().size() + 1)) + ")");
        System.out.println("2. Hire a Chef (Cost: Rp. " + (200 * (restaurant.getChefs().size() + 1)) + ")");
        System.out.println("3. Go Back");

        printMenuHeaderFooter("");
    }

    public void showWaiterList(CopyOnWriteArrayList<Waiter> waiters) {
        Cleaner.cls();
        printMenuHeaderFooter("Waiter Roster");

        if (waiters.isEmpty()) {
            System.out.println("No waiters are currently employed.");
        } else {
            for (int i = 0; i < waiters.size(); i++) {
                Waiter w = waiters.get(i);
                System.out.printf("%d. %s (Speed: %d)\n", i + 1, w.getInitial(), w.getSpeed());
            }
        }

        System.out.println("0. Return to Previous Menu");
        printMenuHeaderFooter("");
    }

    public void showChefList(CopyOnWriteArrayList<Chef> chefs) {
        Cleaner.cls();
        printMenuHeaderFooter("Chef Roster");

        if (chefs.isEmpty()) {
            System.out.println("No chefs are currently employed.");
        } else {
            for (int i = 0; i < chefs.size(); i++) {
                Chef c = chefs.get(i);
                System.out.printf("%d. %s (Speed: %d, Skill: %d)\n",
                        i + 1, c.getInitial(), c.getSpeed(), c.getSkill());
            }
        }

        System.out.println("0. Return to Previous Menu");
        printMenuHeaderFooter("");
    }

    public void showChefUpgradeOptions() {
        System.out.println("═════════════════════════════");
        System.out.println("1. Improve Speed");
        System.out.println("2. Improve Skill");
        System.out.println("═════════════════════════════");
    }

    public void showMaxSeatsError() {
        showError("You have reached the maximum seating capacity (13 seats).");
    }

    public void showMaxEmployeesError() {
        showError("You have reached the maximum number of employees (7 employees).");
    }

    public void showMaxStatError() {
        showError("This stat level has reached its maximum (Level 5).");
    }

    public void showInsufficientFundsError() {
        showError("You do not have enough funds to complete this action.");
    }

    private void printMenuHeaderFooter(String menuName) {
        System.out.println(SEPARATOR);
        if (!menuName.isEmpty()) {
            System.out.println(menuName);
        }
        System.out.println(SEPARATOR);
    }

    private void showError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }
}
