package com.tdt4240gr18.game.entity.components;

import com.badlogic.ashley.core.Component;

public class ScoreComponent implements Component {
    private int score = 0;

    public int getScore() {
        return this.score;
    }

    public void incrementScore(int increment) {
        this.score += increment;
    }
}
