package com.tdt4240gr18.game.entity.components;

import com.badlogic.ashley.core.Component;

public class LivesComponent implements Component {
    public int lives = 3;
    public boolean lifeLoss = false;

    public void decrementLives(int decrement) {
        lives -= decrement;
        lifeLoss = true;
    }
}
