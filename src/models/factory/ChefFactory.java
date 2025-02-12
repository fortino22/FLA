package models.factory;

import models.entity.Chef;
import helpers.ManagerInitials;

public class ChefFactory {
    private static ChefFactory instance;

    private ChefFactory() {}

    public static ChefFactory getInstance() {
        if(instance == null) {
            instance = new ChefFactory();
        }
        return instance;
    }

    public Chef createChef() {
        String initial = ManagerInitials.getInstance().GetUniqueInitial();
        return new Chef(initial);
    }
}
