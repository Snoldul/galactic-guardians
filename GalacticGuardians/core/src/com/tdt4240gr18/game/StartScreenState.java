package com.tdt4240gr18.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class StartScreenState implements GameStateInterface{

    private GalacticGuardians game;

    private BitmapFont title = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));

    public StartScreenState(GalacticGuardians game){
        this.game = game;
    }

    @Override
    public void handleInput(float dt) {
        if (Gdx.input.isTouched()) {
            game.changeState(new PlayingState(game));
        }
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        String titleText = "Galactic Guardians";
        // Calculate the width of the text
        GlyphLayout layout = new GlyphLayout(title, titleText);
        float width = layout.width; // contains the width of the text

        float xPosition = (Gdx.graphics.getWidth() - width) / 2; // Center horizontally
        float yPosition = (Gdx.graphics.getHeight() + layout.height) / 2; // Center vertically

        batch.begin();
        title.draw(batch, layout, xPosition, yPosition);
        batch.end();
    }
    public void dispose() {
        title.dispose();
    }
}