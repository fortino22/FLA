package GameManager;

import controllers.facades.GameFacade;
import controllers.managers.HighscoreManager;
import models.entity.Restaurant;
import models.entity.*;
import helpers.Validator;
import helpers.Transition;
import views.UpgradeMenuView;
import java.util.concurrent.CopyOnWriteArrayList;

public class UpgradeController {
    private final GameFacade game;
    private final UpgradeMenuView view;
    private final HighscoreManager highscore;

    public UpgradeController(Restaurant restaurant) {
        this.game = GameFacade.getInstance();
        this.view = new UpgradeMenuView();
        this.highscore = HighscoreManager.getInstance();
    }

    private Restaurant activeRestaurant() {
        return Restaurant.getActiveRestaurant();
    }

    public void start() {
        while (true) {
            view.showUpgradeMenu(activeRestaurant());

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose menu [1-5]: "), 1, 5);

            if (choice == 5) return;

            Transition.execute(choice,
                    this::addSeats,
                    this::showEmployeeMenu,
                    this::upgradeWaiterStats,
                    this::upgradeChefStats
            );
        }
    }

    private void addSeats() {
        Restaurant restaurant = activeRestaurant();
        if (restaurant.getSeats() >= 13) {
            view.showMaxSeatsError();
            return;
        }

        int cost = 100 * restaurant.getSeats();
        if (restaurant.getMoney() < cost) {
            view.showInsufficientFundsError();
            return;
        }

        restaurant.setMoney(restaurant.getMoney() - cost);
        restaurant.setSeats(restaurant.getSeats() + 1);
    }

    private void showEmployeeMenu() {
        while (true) {
            view.showHireMenu(activeRestaurant());

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose employee type [1-3]: "), 1, 3);

            if (choice == 3) return;

            Transition.execute(choice, this::hireWaiter, this::hireChef);
        }
    }

    private void hireWaiter() {
        hireEmployee(activeRestaurant().getWaiters(), 150, game::addWaiter);
    }

    private void hireChef() {
        hireEmployee(activeRestaurant().getChefs(), 200, game::addChef);
    }

    private <T> void hireEmployee(CopyOnWriteArrayList<T> employees, int baseCost, Runnable addEmployee) {
        Restaurant restaurant = activeRestaurant();

        if (employees.size() >= 7) {
            view.showMaxEmployeesError();
            return;
        }

        int cost = baseCost * (employees.size() + 1);
        if (restaurant.getMoney() < cost) {
            view.showInsufficientFundsError();
            return;
        }

        restaurant.setMoney(restaurant.getMoney() - cost);
        addEmployee.run();
    }

    private void upgradeWaiterStats() {
        CopyOnWriteArrayList<Waiter> waiters = activeRestaurant().getWaiters();

        if (waiters.isEmpty()) {
            System.out.println("No waiters to upgrade!");
            return;
        }

        while (true) {
            view.showWaiterList(waiters);

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose waiter [0-" + waiters.size() + "]: "), 0, waiters.size());

            if (choice == 0) return;

            Waiter waiter = waiters.get(choice - 1);
            if (waiter.getSpeed() >= 5) {
                view.showMaxStatError();
                continue;
            }

            if (activeRestaurant().getMoney() < 150) {
                view.showInsufficientFundsError();
                continue;
            }

            waiter.setSpeed(waiter.getSpeed() + 1);
            activeRestaurant().setMoney(activeRestaurant().getMoney() - 150);
            return;
        }
    }

    private void upgradeChefStats() {
        CopyOnWriteArrayList<Chef> chefs = activeRestaurant().getChefs();

        if (chefs.isEmpty()) {
            System.out.println("No chefs to upgrade!");
            return;
        }

        while (true) {
            view.showChefList(chefs);

            int choice = Validator.getValidIntInput(() -> System.out.print("Choose chef [0-" + chefs.size() + "]: "), 0, chefs.size());

            if (choice == 0) return;

            Chef chef = chefs.get(choice - 1);
            view.showChefUpgradeOptions();

            int statChoice = Validator.getValidIntInput(() -> System.out.print("Choose stat to upgrade [1-2]: "), 1, 2);

            if (activeRestaurant().getMoney() < 150) {
                view.showInsufficientFundsError();
                continue;
            }

            boolean upgraded = false;
            if (statChoice == 1 && chef.getSpeed() < 5) {
                chef.setSpeed(chef.getSpeed() + 1);
                upgraded = true;
            } else if (statChoice == 2 && chef.getSkill() < 5) {
                chef.setSkill(chef.getSkill() + 1);
                upgraded = true;
            }

            if (upgraded) {
                activeRestaurant().setMoney(activeRestaurant().getMoney() - 150);
                return;
            } else {
                view.showMaxStatError();
            }
        }
    }
}