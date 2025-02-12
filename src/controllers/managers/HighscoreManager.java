package controllers.managers;

import java.io.*;
import java.util.*;

public class HighscoreManager {
    private static volatile HighscoreManager instance;
    private static final String HighScoreFIle = "highscore.txt";
    private static final int MAX_HIGHSCORE = 10;
    private List<RestaurantScore> scores;

    private HighscoreManager() {
        scores = new ArrayList<>();
        loadHighscoresFromFIle();
    }

    public static HighscoreManager getInstance() {
        if (instance == null) {
            synchronized (HighscoreManager.class) {
                if (instance == null) {
                    instance = new HighscoreManager();
                }
            }
        }
        return instance;
    }

    private void loadHighscoresFromFIle() {
        try {
            File file = new File(HighScoreFIle);
            if(!file.exists()) {
                file.createNewFile();
                return;
            }
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split("#");
                if (data.length == 2) {
                    scores.add(new RestaurantScore(data[0], Integer.parseInt(data[1])));
                }
            }
            scanner.close();
            Collections.sort(scores);
        } catch (IOException e) {
            System.out.println("Error loading highscores: " + e.getMessage());
        }
    }

    public void addScore(String name, int score) {
        scores.add(new RestaurantScore(name, score));
        Collections.sort(scores);
        if (scores.size() >MAX_HIGHSCORE) {
            scores = scores.subList(0, 10);
        }
        saveHighscoresToFile();
    }

    private void saveHighscoresToFile() {
        try {
            FileWriter writer = new FileWriter(HighScoreFIle);
            for(RestaurantScore restaurantScore : scores) {
                writer.write(restaurantScore.name + "#" + restaurantScore.score + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving highscores: "+ e.getMessage());
        }
    }

    public List<RestaurantScore> getHighscores() {

        return new ArrayList<>(scores);
    }



    public static class RestaurantScore implements Comparable<RestaurantScore> {
        private String name;
        private int score;

        public RestaurantScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        @Override
        public int compareTo(RestaurantScore other) {

            return other.score - this.score;
        }

        @Override
        public String toString() {

            return String.format("%s - %d", name, score);
        }
    }
}
