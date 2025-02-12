package views;

import helpers.Cleaner;

import java.util.Scanner;

public class GameStartView {

    public static void HomeMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" ______   _______    _______  _______  _______ _________ _______ \n" +
                "(  __  \\ (  ___  )  (  ____ )(  ____ \\(  ____ \\\\__   __/(  ___  )\n" +
                "| (  \\  )| (   ) |  | (    )|| (    \\/| (    \\/   ) (   | (   ) |\n" +
                "| |   ) || (___) |  | (____)|| (__    | (_____    | |   | |   | |\n" +
                "| |   | ||  ___  |  |     __)|  __)   (_____  )   | |   | |   | |\n" +
                "| |   ) || (   ) |  | (\\ (   | (            ) |   | |   | |   | |\n" +
                "| (__/  )| )   ( |  | ) \\ \\__| (____/\\/\\____) |   | |   | (___) |\n" +
                "(______/ |/     \\|  |/   \\__/(_______/\\_______)   )_(   (_______)\n" +
                "                                                                 ");
        System.out.print("Press Enter to Continue...");
        scanner.nextLine();
        Cleaner.cls();
    }
}
