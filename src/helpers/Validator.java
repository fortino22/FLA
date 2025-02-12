package helpers;

import java.util.Scanner;
import interfaces.IValidation;
import interfaces.IParsing;

public class Validator {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static <T> T getInput(Runnable prompt, IValidation<T> validator, IParsing<T> parser) {

        while (true) {
            prompt.run();
            String userInput = SCANNER.nextLine().trim();
            try {
                T input = parser.parse(userInput);
                if (validator.validate(input)) {
                    return input;
                }
                System.out.println("Invalid input! Please try again.");
            } catch (Exception e) {
                System.out.println("Invalid format! Try again.");
            }
        }
    }

    public static int getValidIntInput(Runnable prompt, int min, int max) {
        return getInput(prompt, input -> input >= min && input <= max, Integer::parseInt);
    }

    public static String getValidStringInput(Runnable prompt, int min, int max) {
        return getInput(prompt, input -> input.length() >= min && input.length() <= max, input -> input);
    }
}
