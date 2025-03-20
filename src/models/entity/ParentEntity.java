package models.entity;

import interfaces.IStateMachine;
import helpers.Alert;
import interfaces.IRestaurant;
import controllers.mediators.RestaurantMediator;

public abstract class ParentEntity implements IRestaurant {
    private RestaurantMediator mediator;
    private volatile boolean isPaused = false;
    private String initial;
    protected IStateMachine state;

    public ParentEntity(String initial) {
        this.initial = initial;
    }

    public String getInitial() {
        return initial;
    }
    public void setState(IStateMachine state) {
        this.state = state;
    }

    public IStateMachine getState() {
        return state;
    }

    @Override
    public void setMediator(RestaurantMediator mediator) {
        this.mediator = mediator;
    }

    public RestaurantMediator getMediator() {
        return mediator;
    }

    @Override
    public void pause() {
        isPaused = true;
    }

    @Override
    public void resume() {
        isPaused = false;
    }

    @Override
    public void stop() {
        pause();
    }

    @Override
    public void update() {
        if (isNotPaused() && state != null) {
            state.update();
            logState();
        }
    }

    private boolean isNotPaused() {
        return !isPaused;
    }

    private void logState() {
        Alert.generalDebug(getInitial() + " state: " + state.getStateName());
    }
}
