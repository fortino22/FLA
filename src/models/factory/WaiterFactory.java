package models.factory;

import models.entity.Waiter;
import helpers.ManagerInitials;

public class WaiterFactory {
    private static WaiterFactory instance;
    
    private WaiterFactory() {}
    
    public static WaiterFactory getInstance() {
        if(instance == null) {
            instance = new WaiterFactory();
        }
        return instance;
    }
    
    public Waiter createWaiter() {
        String initial = ManagerInitials.getInstance().GetUniqueInitial();
        return new Waiter(initial);
    }
}
