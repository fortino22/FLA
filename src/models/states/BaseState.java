package models.states;

import interfaces.IStateMachine;
import models.entity.ParentEntity;

public abstract class BaseState implements IStateMachine {
    protected ParentEntity entity;
    protected String customerName;
    
    public BaseState(ParentEntity entity, String customerName) {
        this.entity = entity;
        this.customerName = customerName;
    }
    
    public BaseState(ParentEntity entity) {
        this(entity, null);
    }

    @Override
    public void changeState(String customerName) {

    }
}
