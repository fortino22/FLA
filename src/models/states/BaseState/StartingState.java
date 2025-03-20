package models.states.BaseState;

import interfaces.IStateMachine;
import models.entity.ParentEntity;

public abstract class StartingState implements IStateMachine {
    protected final ParentEntity entity;
    protected final String customerName;


    public StartingState(ParentEntity entity, String customerName) {
        this.entity = entity;
        this.customerName = customerName;
    }


    public StartingState(ParentEntity entity) {
        this(entity, null);
    }


    @Override
    public void changeState(String customerName) {
    }
}