package helpers;

import interfaces.IMenuHandler;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class Transition {

    private static final String INDEX_OUT_OF_BOUNDS_MESSAGE = "Choice must be between 1 and %d";

    public static void execute(int choice, Runnable... functions) {
        Objects.requireNonNull(functions, "Functions array cannot be null");

        if (choice < 1 || choice > functions.length) {
            throw new IllegalArgumentException(
                    String.format(INDEX_OUT_OF_BOUNDS_MESSAGE, functions.length));
        }

        Runnable selectedAction = functions[choice - 1];
        Objects.requireNonNull(selectedAction, "Selected action cannot be null");

        selectedAction.run();
    }

    public static boolean execute(int choice, IMenuHandler... actions) {
        Objects.requireNonNull(actions, "Actions array cannot be null");

        if (choice < 1 || choice > actions.length) {
            return false;
        }

        IMenuHandler selectedHandler = actions[choice - 1];
        return selectedHandler != null && selectedHandler.execute();
    }

    @SafeVarargs
    public static <T> Optional<T> executeWithResult(int choice, Supplier<T>... suppliers) {
        Objects.requireNonNull(suppliers, "Suppliers array cannot be null");

        if (choice < 1 || choice > suppliers.length) {
            return Optional.empty();
        }

        Supplier<T> selectedSupplier = suppliers[choice - 1];
        return selectedSupplier != null ?
                Optional.ofNullable(selectedSupplier.get()) :
                Optional.empty();
    }
}