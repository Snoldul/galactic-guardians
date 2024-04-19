package com.tdt4240gr18.game.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.tdt4240gr18.game.entity.components.ScoreComponent;


public class ScoreSystem extends IteratingSystem {
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final PooledEngine engine;

    private float moveAreaHeight = 0;
    public ScoreSystem(PooledEngine engine) {
        super(Family.all(ScoreComponent.class).get());
        font = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        batch = new SpriteBatch();
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (moveAreaHeight == 0){
            Rectangle moveArea = engine.getSystem(PlayerControlSystem.class).getMoveArea();
            moveAreaHeight = moveArea.height;
        }
        batch.begin();
        if (entity != null) {
            // Get the TextComponent containing the score value
            ScoreComponent scoreComponent = entity.getComponent(ScoreComponent.class);
            if (scoreComponent != null) {
                String valueString = String.valueOf(scoreComponent.getScore());

                // Set font scale for rendering
                font.getData().setScale(1.5f); // Adjust scale as needed

                // Calculate the width of a single character in the font
                GlyphLayout layout = new GlyphLayout(font, "A"); // Use any character to measure
                float characterWidth = layout.width;

                // Calculate the total width of the text
                float textWidth = valueString.length() * characterWidth;

                // Calculate the x position to center the text horizontally
                float x = Gdx.graphics.getWidth() - 30 - textWidth;

                float y = moveAreaHeight + characterWidth * 1.5f; // Adjust y position as needed

                // Render the score on-screen
                font.draw(batch, valueString, x, y);
            }
        }
        batch.end();
    }
}
