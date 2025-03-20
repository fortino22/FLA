package controllers.singleton;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public class HighscoreSingleton {
    private static final Logger LOGGER = Logger.getLogger(HighscoreSingleton.class.getName());
    private static volatile HighscoreSingleton instance;
    private static final String HIGHSCORE_FILE = "highscore.txt";
    private static final int MAX_HIGHSCORES = 10;
    private final List<RestaurantScore> scores;

    private HighscoreSingleton() {
        scores = new ArrayList<>();
        loadHighscoresFromFile();
    }

    public static HighscoreSingleton getInstance() {
        if (instance == null) {
            synchronized (HighscoreSingleton.class) {
                if (instance == null) {
                    instance = new HighscoreSingleton();
                }
            }
        }
        return instance;
    }

    private void loadHighscoresFromFile() {
        Path filePath = Paths.get(HIGHSCORE_FILE);

        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                return;
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Could not create highscore file", e);
                return;
            }
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                parseScoreLine(line);
            }
            Collections.sort(scores);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading highscores", e);
        }
    }

    private void parseScoreLine(String line) {
        String[] data = line.split("#");
        if (data.length == 2) {
            try {
                String name = data[0];
                int score = Integer.parseInt(data[1]);
                scores.add(new RestaurantScore(name, score));
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid score format: " + line, e);
            }
        }
    }

    public void addScore(String name, int score) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        scores.add(new RestaurantScore(name, score));
        Collections.sort(scores);

        if (scores.size() > MAX_HIGHSCORES) {
            scores.subList(MAX_HIGHSCORES, scores.size()).clear();
        }

        saveHighscoresToFile();
    }

    private void saveHighscoresToFile() {
        Path filePath = Paths.get(HIGHSCORE_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (RestaurantScore score : scores) {
                writer.write(score.getName() + "#" + score.getScore());
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving highscores", e);
        }
    }

    public List<RestaurantScore> getHighscores() {
        return Collections.unmodifiableList(scores);
    }

    public static class RestaurantScore implements Comparable<RestaurantScore> {
        private final String name;
        private final int score;

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
            return Integer.compare(other.score, this.score);
        }

        @Override
        public String toString() {
            return String.format("%s - %d", name, score);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RestaurantScore that = (RestaurantScore) o;
            return score == that.score && Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, score);
        }
    }
}