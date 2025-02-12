package models.factory;

import models.entity.Customer;
import helpers.ManagerInitials;

public class CustomerFactory {
    private static CustomerFactory instance;
    
    private CustomerFactory() {}
    
    public static CustomerFactory getInstance() {
        if(instance == null) {
            instance = new CustomerFactory();
        }
        return instance;
    }
    
    public Customer createCustomer() {
        String initial = ManagerInitials.getInstance().GetUniqueInitial();
        return new Customer(initial);
    }
}
