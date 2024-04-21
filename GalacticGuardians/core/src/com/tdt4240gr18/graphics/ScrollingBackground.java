package com.tdt4240gr18.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScrollingBackground {
    private final Texture[] backgrounds;
    private final float[] backgroundOffsets = {0, 0, 0, 0};
    private final float maxScrollingSpeed;

    public ScrollingBackground() {
        backgrounds = new Texture[4];
        backgrounds[0] = new Texture("backgroundLayer1.png");
        backgrounds[1] = new Texture("backgroundLayer2.png");
        backgrounds[2] = new Texture("backgroundLayer3.png");
        backgrounds[3] = new Texture("backgroundLayer4.png");
        maxScrollingSpeed = 500f;
    }

    public void render(float deltaTime, SpriteBatch sb) {
        // Calculate the new offsets for the backgrounds
        backgroundOffsets[0] += deltaTime * maxScrollingSpeed / 8;
        backgroundOffsets[1] += deltaTime * maxScrollingSpeed / 4;
        backgroundOffsets[2] += deltaTime * maxScrollingSpeed / 2;
        backgroundOffsets[3] += deltaTime * maxScrollingSpeed;

        // Draw the backgrounds
        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            if (backgroundOffsets[layer] > Gdx.graphics.getHeight()) {
                backgroundOffsets[layer] = 0;
            }
            sb.draw(backgrounds[layer], 0, -backgroundOffsets[layer], Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            sb.draw(backgrounds[layer], 0, -backgroundOffsets[layer] + Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}
