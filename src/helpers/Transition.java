package helpers;

import interfaces.IMenuHandler;

public class Transition {

    public static void execute(int choice, Runnable... functions) {
        if (choice > 0 && choice <= functions.length) {
            functions[choice - 1].run();
        }
    }

    public static boolean execute(int choice, IMenuHandler... actions) {
        if (choice > 0 && choice <= actions.length) {
            return actions[choice - 1].execute();
        }
        return false;
    }
}
