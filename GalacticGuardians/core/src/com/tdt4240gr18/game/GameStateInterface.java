package com.tdt4240gr18.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameStateInterface {
    void handleInput(float dt);
    void update(float dt);
    void render(SpriteBatch batch);
}
