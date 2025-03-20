package GameManager;

import models.entity.Restaurant;
import helpers.Validator;
import helpers.Transition;
import views.MainMenuView;
import views.HighscoreView;

import java.util.List;

import controllers.singleton.HighscoreSingleton;
import controllers.singleton.HighscoreSingleton.RestaurantScore;

public class MainController {

    private final MainMenuView mainMenuView;
    private final HighscoreView highscoreView;
    private final Restaurant restaurant;

    public MainController(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.mainMenuView = new MainMenuView();
        this.highscoreView = new HighscoreView();
        initialize();
    }

    public void initialize() {
        boolean isRunning = true;

        while (isRunning) {
            displayMainMenu();

            int choice = Validator.getValidIntInput(
                    () -> System.out.print("Enter your choice (1-3): "),
                    1, 3
            );

            isRunning = Transition.execute(choice,
                    this::startGame,
                    this::displayHighscores,
                    this::exitApplication
            );
        }
    }

    private void displayMainMenu() {
        mainMenuView.showMainMenu();
    }

    private boolean startGame() {
        GameController gameController = new GameController(restaurant);
        gameController.start();
        return true;
    }

    private boolean displayHighscores() {
        List<RestaurantScore> scores = HighscoreSingleton.getInstance().getHighscores();
        highscoreView.showHighscores(scores);
        return true;
    }

    private boolean exitApplication() {
        System.exit(0);
        return false;
    }
}
