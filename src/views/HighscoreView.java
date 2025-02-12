package views;

import java.util.List;
import java.util.Scanner;

import controllers.managers.HighscoreManager.RestaurantScore;
import helpers.Cleaner;

public class HighscoreView {
    public void showHighscores(List<RestaurantScore> scores) {
        Cleaner.cls();
        System.out.println("═══════════════════════");
        System.out.println("      HIGHSCORES");
        System.out.println("═══════════════════════");

        if (scores.isEmpty()) {
            System.out.println("No highscores yet!");
        } else {
            for (int i = 0; i < scores.size(); i++) {
                RestaurantScore score = scores.get(i);
                System.out.printf("%d. %s - %d\n",
                        i + 1,
                        score.getName(),
                        score.getScore()
                );
            }
        }

        System.out.println("═══════════════════════");
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }
}