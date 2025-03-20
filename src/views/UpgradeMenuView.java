package views;

import models.entity.Restaurant;
import models.entity.*;
import helpers.Cleaner;
import helpers.Alert;

import java.util.concurrent.CopyOnWriteArrayList;

public class UpgradeMenuView {
    private static final String MENU_DIVIDER = "═════════════════════════════";
    private static final int MAX_RESTAURANT_SEATS = 13;
    private static final int MAX_STAFF_COUNT = 7;
    private static final int MAX_ATTRIBUTE_LEVEL = 5;
    private static final int WAITER_HIRING_BASE_COST = 150;
    private static final int CHEF_HIRING_BASE_COST = 200;
    private static final int SEAT_EXPANSION_MULTIPLIER = 100;
    private static final int STAFF_UPGRADE_COST = 150;

    public UpgradeMenuView() {
        Alert.restaurantInitializationDebug("UpgradeMenuView initialized");
    }

    public void displayUpgradeMainMenu(Restaurant restaurant) {
        if (restaurant == null) {
            Alert.restaurantInitializationDebug("Restaurant object is null in displayUpgradeMainMenu.");
            return;
        }

        Cleaner.cls();
        renderMenuFrame("Restaurant Upgrade Options");
        showRestaurantStatus(restaurant);
        listUpgradeOptions(restaurant);
        renderMenuFrame("");
    }

    private void showRestaurantStatus(Restaurant restaurant) {
        System.out.println("Restaurant Name: " + restaurant.getName());
        System.out.println("Current Balance: Rp. " + restaurant.getMoney());
        System.out.println("Current Score: " + restaurant.getScore());
        System.out.println("Available Seats: " + restaurant.getSeats());
    }

    private void listUpgradeOptions(Restaurant restaurant) {
        int expansionCost = SEAT_EXPANSION_MULTIPLIER * restaurant.getSeats();
        System.out.println("1. Expand Restaurant Seating (Cost: Rp. " + expansionCost + ")");
        System.out.println("2. Add a New Employee");
        System.out.println("3. Upgrade Waiter (Cost: Rp. " + STAFF_UPGRADE_COST + ")");
        System.out.println("4. Upgrade Chef (Cost: Rp. " + STAFF_UPGRADE_COST + ")");
        System.out.println("5. Return to Main Menu");
    }

    public void displayStaffRecruitmentMenu(Restaurant restaurant) {
        Cleaner.cls();
        renderMenuFrame("Employee Hiring Options");

        int waiterCost = WAITER_HIRING_BASE_COST * (restaurant.getWaiters().size() + 1);
        int chefCost = CHEF_HIRING_BASE_COST * (restaurant.getChefs().size() + 1);

        System.out.println("1. Hire a Waiter (Cost: Rp. " + waiterCost + ")");
        System.out.println("2. Hire a Chef (Cost: Rp. " + chefCost + ")");
        System.out.println("3. Go Back");

        renderMenuFrame("");
    }

    public void displayWaiterRoster(CopyOnWriteArrayList<Waiter> waiters) {
        Cleaner.cls();
        renderMenuFrame("Waiter Roster");

        if (waiters.isEmpty()) {
            System.out.println("No waiters are currently employed.");
        } else {
            renderWaiterList(waiters);
        }

        System.out.println("0. Return to Previous Menu");
        renderMenuFrame("");
    }

    private void renderWaiterList(CopyOnWriteArrayList<Waiter> waiters) {
        for (int index = 0; index < waiters.size(); index++) {
            Waiter currentWaiter = waiters.get(index);
            System.out.printf("%d. %s (Speed: %d)\n",
                    index + 1, currentWaiter.getInitial(), currentWaiter.getSpeed());
        }
    }

    public void displayChefRoster(CopyOnWriteArrayList<Chef> chefs) {
        Cleaner.cls();
        renderMenuFrame("Chef Roster");

        if (chefs.isEmpty()) {
            System.out.println("No chefs are currently employed.");
        } else {
            renderChefList(chefs);
        }

        System.out.println("0. Return to Previous Menu");
        renderMenuFrame("");
    }

    private void renderChefList(CopyOnWriteArrayList<Chef> chefs) {
        for (int index = 0; index < chefs.size(); index++) {
            Chef currentChef = chefs.get(index);
            System.out.printf("%d. %s (Speed: %d, Skill: %d)\n",
                    index + 1, currentChef.getInitial(), currentChef.getSpeed(), currentChef.getSkill());
        }
    }

    public void displayChefUpgradeMenu() {
        System.out.println(MENU_DIVIDER);
        System.out.println("1. Improve Speed");
        System.out.println("2. Improve Skill");
        System.out.println(MENU_DIVIDER);
    }

    public void displaySeatingLimitError() {
        displayErrorMessage("You have reached the maximum seating capacity (" + MAX_RESTAURANT_SEATS + " seats).");
    }

    public void displayStaffLimitError() {
        displayErrorMessage("You have reached the maximum number of employees (" + MAX_STAFF_COUNT + " employees).");
    }

    public void displayAttributeLimitError() {
        displayErrorMessage("This stat level has reached its maximum (Level " + MAX_ATTRIBUTE_LEVEL + ").");
    }

    public void displayInsufficientFundsError() {
        displayErrorMessage("You do not have enough funds to complete this action.");
    }

    private void renderMenuFrame(String menuTitle) {
        System.out.println(MENU_DIVIDER);
        if (!menuTitle.isEmpty()) {
            System.out.println(menuTitle);
        }
        System.out.println(MENU_DIVIDER);
    }

    private void displayErrorMessage(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }
}