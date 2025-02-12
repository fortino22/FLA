package interfaces;

public interface IStateMachine {
    void update();
    String getStateName();
    void changeState(String customerName); 
}
