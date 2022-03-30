package tetris.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    private String name;
    private int score;

    public Map<String, Integer> hashMap = new HashMap<>();

    // public Player(String name, int score) {
    // this.name = name;
    // this.score = score;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    // public void setScore(int score) {
    // this.score = score;
    // }

}
