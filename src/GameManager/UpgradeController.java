package GameManager;

import controllers.facades.GameFacade;
import controllers.singleton.HighscoreSingleton;
import models.entity.Restaurant;
import models.entity.*;
import helpers.Validator;
import helpers.Transition;
import views.UpgradeMenuView;
import java.util.concurrent.CopyOnWriteArrayList;

public class UpgradeController {
    private final GameFacade game;
    private final UpgradeMenuView view;
    private static final int SEAT_EXPANSION_MULTIPLIER = 100;
    private static final int WAITER_HIRING_BASE_COST = 150;
    private static final int CHEF_HIRING_BASE_COST = 200;
    private static final int STAFF_UPGRADE_COST = 150;
    private static final int MAX_RESTAURANT_SEATS = 13;
    private static final int MAX_STAFF_COUNT = 7;
    private static final int MAX_ATTRIBUTE_LEVEL = 5;

    public UpgradeController(Restaurant restaurant) {
        this.game = GameFacade.getInstance();
        this.view = new UpgradeMenuView();
    }

    private Restaurant activeRestaurant() {
        return Restaurant.getActiveRestaurant();
    }

    public void start() {
        while (true) {
            view.displayUpgradeMainMenu(activeRestaurant());

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose menu [1-5]: "), 1, 5);

            if (choice == 5) return;

            Transition.execute(choice,
                    this::expandSeating,
                    this::showStaffRecruitmentMenu,
                    this::upgradeWaiterAttributes,
                    this::upgradeChefAttributes
            );
        }
    }

    private void expandSeating() {
        Restaurant restaurant = activeRestaurant();
        if (restaurant.getSeats() >= MAX_RESTAURANT_SEATS) {
            view.displaySeatingLimitError();
            return;
        }

        int expansionCost = SEAT_EXPANSION_MULTIPLIER * restaurant.getSeats();
        if (restaurant.getMoney() < expansionCost) {
            view.displayInsufficientFundsError();
            return;
        }

        restaurant.setMoney(restaurant.getMoney() - expansionCost);
        restaurant.setSeats(restaurant.getSeats() + 1);
    }

    private void showStaffRecruitmentMenu() {
        while (true) {
            view.displayStaffRecruitmentMenu(activeRestaurant());

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose employee type [1-3]: "), 1, 3);

            if (choice == 3) return;

            Transition.execute(choice, this::recruitWaiter, this::recruitChef);
        }
    }

    private void recruitWaiter() {
        recruitStaffMember(activeRestaurant().getWaiters(), WAITER_HIRING_BASE_COST, game::addWaiter);
    }

    private void recruitChef() {
        recruitStaffMember(activeRestaurant().getChefs(), CHEF_HIRING_BASE_COST, game::addChef);
    }

    private <T> void recruitStaffMember(CopyOnWriteArrayList<T> staffList, int baseCost, Runnable addStaffMember) {
        Restaurant restaurant = activeRestaurant();

        if (staffList.size() >= MAX_STAFF_COUNT) {
            view.displayStaffLimitError();
            return;
        }

        int hiringCost = baseCost * (staffList.size() + 1);
        if (restaurant.getMoney() < hiringCost) {
            view.displayInsufficientFundsError();
            return;
        }

        restaurant.setMoney(restaurant.getMoney() - hiringCost);
        addStaffMember.run();
    }

    private void upgradeWaiterAttributes() {
        CopyOnWriteArrayList<Waiter> waiters = activeRestaurant().getWaiters();

        if (waiters.isEmpty()) {
            System.out.println("No waiters to upgrade!");
            return;
        }

        while (true) {
            view.displayWaiterRoster(waiters);

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose waiter [0-" + waiters.size() + "]: "), 0, waiters.size());

            if (choice == 0) return;

            Waiter selectedWaiter = waiters.get(choice - 1);
            if (selectedWaiter.getSpeed() >= MAX_ATTRIBUTE_LEVEL) {
                view.displayAttributeLimitError();
                continue;
            }

            if (activeRestaurant().getMoney() < STAFF_UPGRADE_COST) {
                view.displayInsufficientFundsError();
                continue;
            }

            selectedWaiter.setSpeed(selectedWaiter.getSpeed() + 1);
            activeRestaurant().setMoney(activeRestaurant().getMoney() - STAFF_UPGRADE_COST);
            return;
        }
    }

    private void upgradeChefAttributes() {
        CopyOnWriteArrayList<Chef> chefs = activeRestaurant().getChefs();

        if (chefs.isEmpty()) {
            System.out.println("No chefs to upgrade!");
            return;
        }

        while (true) {
            view.displayChefRoster(chefs);

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose chef [0-" + chefs.size() + "]: "), 0, chefs.size());

            if (choice == 0) return;

            Chef selectedChef = chefs.get(choice - 1);
            view.displayChefUpgradeMenu();

            int attributeChoice = Validator.getValidIntInput(() -> System.out.print("Choose stat to upgrade [1-2]: "), 1, 2);

            if (activeRestaurant().getMoney() < STAFF_UPGRADE_COST) {
                view.displayInsufficientFundsError();
                continue;
            }

            boolean upgraded = false;
            if (attributeChoice == 1 && selectedChef.getSpeed() < MAX_ATTRIBUTE_LEVEL) {
                selectedChef.setSpeed(selectedChef.getSpeed() + 1);
                upgraded = true;
            } else if (attributeChoice == 2 && selectedChef.getSkill() < MAX_ATTRIBUTE_LEVEL) {
                selectedChef.setSkill(selectedChef.getSkill() + 1);
                upgraded = true;
            }

            if (upgraded) {
                activeRestaurant().setMoney(activeRestaurant().getMoney() - STAFF_UPGRADE_COST);
                return;
            } else {
                view.displayAttributeLimitError();
            }
        }
    }
}