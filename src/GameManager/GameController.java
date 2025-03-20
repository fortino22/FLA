package GameManager;

import controllers.facades.GameFacade;
import controllers.singleton.HighscoreSingleton;
import helpers.Validator;
import views.GameView;
import helpers.Transition;
import views.PauseMenuView;
import views.HighscoreView;
import views.ClosureView;
import java.util.Scanner;
import models.entity.Restaurant;


public class GameController {


    private static final int RESUME_GAME_OPTION = 1;
    private static final int CLOSE_RESTAURANT_OPTION = 3;
    private static final int GAME_LOOP_SLEEP_MS = 1000;

    private final UpgradeController upgradeController;
    private final GameFacade gameFacade;
    private final GameView gameView;
    private final PauseMenuView pauseMenuView;
    private final HighscoreView highscoreView;
    private final ClosureView closureView;
    private final HighscoreSingleton highscoreManager;

    private boolean isGameRunning;

    public GameController(Restaurant restaurant) {
        this.gameFacade = GameFacade.getInstance();
        this.gameView = new GameView();
        this.pauseMenuView = new PauseMenuView();
        this.highscoreView = new HighscoreView();
        this.closureView = new ClosureView();
        this.highscoreManager = HighscoreSingleton.getInstance();
        this.upgradeController = new UpgradeController(restaurant);
        this.isGameRunning = true;
    }


    public void start() {
        String restaurantName = Validator.getValidStringInput(this::promptRestaurantNameInput, 3, 15);
        gameFacade.initializeGame(restaurantName);
        runGameLoop();
    }

    private void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        Thread gameThread = createGameThread();
        gameThread.start();

        while (isGameRunning) {
            if (scanner.nextLine().isEmpty()) {
                gameFacade.pause();
                displayPauseMenu();
            }
        }

        scanner.close();
    }

    private Thread createGameThread() {
        return new Thread(() -> {
            try {
                while (isGameRunning) {
                    if (!gameFacade.isGamePaused()) {
                        gameFacade.update();
                        gameView.displayGame(gameFacade.getCurrentRestaurant());
                        System.out.println("\nPress 'Enter' to pause the game");
                    }
                    Thread.sleep(GAME_LOOP_SLEEP_MS);
                }
            } catch (InterruptedException e) {
                System.out.println("Game thread interrupted.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void closeRunningRestaurant() {
        Restaurant activeRestaurant = Restaurant.getActiveRestaurant();
        if (activeRestaurant == null) {
            System.out.println("No active restaurant to close.");
            return;
        }

        int finalScore = activeRestaurant.getScore();
        String restaurantName = activeRestaurant.getName();
        highscoreManager.addScore(restaurantName, finalScore);

        closureView.displayClosureMessage(restaurantName, finalScore);
        highscoreView.showHighscores(highscoreManager.getHighscores());

        promptToContinue();
        terminateGame();
    }

    private void promptToContinue() {
        System.out.println("\nPress Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    private void displayPauseMenu() {
        while (true) {
            pauseMenuView.showPauseMenu(gameFacade.getCurrentRestaurant());

            int choice = Validator.getValidIntInput(
                    () -> System.out.print("Choose menu [" + RESUME_GAME_OPTION + "-" + CLOSE_RESTAURANT_OPTION + "]: "),
                    RESUME_GAME_OPTION, CLOSE_RESTAURANT_OPTION
            );

            boolean shouldBreak = Transition.execute(choice,
                    () -> {
                        gameFacade.resume();
                        return true;
                    },
                    () -> {
                        upgradeController.start();
                        return false;
                    },
                    () -> {
                        closeRunningRestaurant();
                        return true;
                    }
            );

            if (shouldBreak) {
                break;
            }
        }
    }

    private void terminateGame() {
        isGameRunning = false;
        gameFacade.terminate();
        System.out.println("Game terminated successfully.");
        System.exit(0);
    }

    public void promptRestaurantNameInput() {
        System.out.print("Enter Restaurant Name [3 - 15 Characters]: ");
    }
}