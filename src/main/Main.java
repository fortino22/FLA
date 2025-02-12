package main;

import GameManager.MainController;
import models.entity.Restaurant;
import views.GameStartView;

public class Main {
    public static void main(String[] args) {
        GameStartView.HomeMenu();
        Restaurant restaurant = new Restaurant(null);
        MainController mainController = new MainController(restaurant);
        mainController.initialize();
    }
}
