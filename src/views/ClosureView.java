package views;

import java.util.concurrent.TimeUnit;

public class ClosureView {

    public void displayClosureMessage(String restaurantName, int finalScore) {
        clearScreen();

        System.out.println("\n\n");
        printWithDelay("████████████████████████████████████████████████", 20);
        printWithDelay("█                                              █", 20);
        printWithDelay("█            RESTAURANT CLOSED                 █", 20);
        printWithDelay("█                                              █", 20);
        printWithDelay("████████████████████████████████████████████████", 20);
        System.out.println("\n");

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("  Restaurant Name: " + restaurantName);
        System.out.println("  Final Score:     " + finalScore + " points");
        System.out.println("\n");

        printStars();
        System.out.println("\n  Thank you for playing!");
        printStars();
    }

    private void printStars() {
        System.out.println("  ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★");
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private void printWithDelay(String text, long delayMillis) {
        System.out.println(text);
        try {
            TimeUnit.MILLISECONDS.sleep(delayMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}