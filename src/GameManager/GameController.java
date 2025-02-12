package GameManager;

import controllers.facades.GameFacade;
import controllers.managers.HighscoreManager;
import helpers.Validator;
import views.GameView;
import helpers.Transition;
import views.PauseMenuView;
import views.HighscoreView;
import java.util.Scanner;
import interfaces.IMenuHandler;
import models.entity.Restaurant;
public class GameController {

    private static final int RESUME_GAME_OPTION = 1;
    private static final int CLOSE_RESTAURANT_OPTION = 3;
    private static final int GAME_LOOP_SLEEP_MS = 1000;

    private final UpgradeController upgradeController;
    private final GameFacade gameFacade;
    private final GameView gameView;
    private final Restaurant restaurant;
    private final PauseMenuView pauseMenuView;
    private final HighscoreView highscoreView;
    private final HighscoreManager highscoreManager;

    private boolean isGameRunning;

    public GameController(Restaurant restaurant) {
        this.restaurant = restaurant;
        this.gameFacade = GameFacade.getInstance();
        this.gameView = new GameView();
        this.pauseMenuView = new PauseMenuView();
        this.highscoreView = new HighscoreView();
        this.highscoreManager = HighscoreManager.getInstance();
        this.upgradeController = new UpgradeController(restaurant);
        this.isGameRunning = true;
    }

    public void start() {
        String restaurantName = Validator.getValidStringInput(this::prompRestaurantNameInput, 3, 15);
        gameFacade.initializeGame(restaurantName);
        runGameLoop();
    }

    private void runGameLoop() {
        Scanner scanner = new Scanner(System.in);
        Thread gameThread = new Thread(() -> {
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
                System.out.println("An unexpected error occurred in the game loop: " + e.getMessage());
                e.printStackTrace();
            }
        });

        gameThread.start();

        while (isGameRunning) {
            if (scanner.nextLine().isEmpty()) {
                gameFacade.pause();
                displayPauseMenu();
            }
        }

        scanner.close();
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
        displayClosureMessage(restaurantName, finalScore);
        highscoreView.showHighscores(highscoreManager.getHighscores());
        terminateGame();
    }



    private void displayClosureMessage(String restaurantName, int finalScore) {
        System.out.println("\n═════════════════════════════");
        System.out.println("     RESTAURANT CLOSED");
        System.out.println("═════════════════════════════");
        System.out.println("Restaurant: " + restaurantName);
        System.out.println("Final Score: " + finalScore);
        System.out.println("═════════════════════════════");
    }

    private void displayPauseMenu() {
        while (true) {
            pauseMenuView.showPauseMenu(gameFacade.getCurrentRestaurant());

            int choice = Validator.getValidIntInput(
                    () -> System.out.print("Choose menu [" + RESUME_GAME_OPTION + "-" + CLOSE_RESTAURANT_OPTION + "]: "),
                    RESUME_GAME_OPTION, CLOSE_RESTAURANT_OPTION
            );

            boolean shouldBreak = Transition.execute(choice,
                    new IMenuHandler() {
                        public boolean execute() {
                            gameFacade.resume();
                            return true;
                        }
                    },
                    new IMenuHandler() {
                        public boolean execute() {
                            upgradeController.start();
                            return false;
                        }
                    },
                    new IMenuHandler() {
                        public boolean execute() {
                            closeRunningRestaurant();
                            return true;
                        }
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

    public void prompRestaurantNameInput() {
        System.out.print("Enter Restaurant Name [3 - 15 Characters]: ");
    }
}
