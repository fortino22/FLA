package helpers;

public class Alert {

    public enum DebugCategory {
        GENERAL,
        RESTAURANT,
        RESTAURANT_MEDIATOR
    }

    private static final boolean[] debugFlags = new boolean[DebugCategory.values().length];

    public static boolean isDebugEnabled(DebugCategory category) {
        return debugFlags[category.ordinal()];
    }

    public static void log(DebugCategory category, String message) {
        if (isDebugEnabled(category)) {
            System.out.println("[" + category.name().replace("_", " ") + " Debug] " + message);
        }
    }

    public static void restaurantDebug(String message) {
        log(DebugCategory.RESTAURANT, message);
    }

    public static void restaurantInitializationDebug(String message) {
        log(DebugCategory.RESTAURANT, "Initialization: " + message);
    }

    public static void restaurantMediatorDebug(String message) {
        log(DebugCategory.RESTAURANT_MEDIATOR, message);
    }

    public static void generalDebug(String message) {
        log(DebugCategory.GENERAL, message);
    }
}