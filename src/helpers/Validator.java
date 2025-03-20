package helpers;

import java.util.Scanner;

public class Validator {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String getInput(Runnable prompt) {
        prompt.run();
        return SCANNER.nextLine().trim();
    }

    public static int getValidIntInput(Runnable prompt, int min, int max) {
        while (true) {
            prompt.run();
            String userInput = SCANNER.nextLine().trim();
            try {
                int input = Integer.parseInt(userInput);
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("Invalid input! Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid format! Please enter a valid number.");
            }
        }
    }

    public static String getValidStringInput(Runnable prompt, int min, int max) {
        while(true){
            String input = getInput(prompt);
            if(input.length() >= min && input.length() <= max){
                return input;
            }
            System.out.println("Invalid input! Please enter a string with length between " + min + " and " + max + ".");
        }
    }
}