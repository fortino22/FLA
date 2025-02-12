package models.entity;

public class RestaurantScore implements Comparable<RestaurantScore> {

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
        return name + " - " + score;
    }
}
