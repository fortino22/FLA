package helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ManagerInitials {
    private static ManagerInitials instance;
    private List<String> availableInitials;
    private final Random random;

    private ManagerInitials() {
        this.random = new Random();
        ResetInitials();
    }

    public static synchronized ManagerInitials getInstance() {
        if (instance == null) {
            instance = new ManagerInitials();
        }
        return instance;
    }

    public void ResetInitials() {
        this.availableInitials = new ArrayList<>(List.of(CustomerInitials.INITIALS));
        Collections.shuffle(this.availableInitials, random);
    }

    public String GetUniqueInitial() {
        if (availableInitials.isEmpty()) {
            ResetInitials();
        }
        return availableInitials.remove(0);
    }
}