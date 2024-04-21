package com.tdt4240gr18.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class ErrorState extends State {
    private final BitmapFont font;
    private final GlyphLayout layout, returnMessage;
    private final int width = Gdx.graphics.getWidth();
    private final int height = Gdx.graphics.getHeight();

    public ErrorState(GameStateManager gsm, String errMess, BitmapFont font) {
        super(gsm);
        Gdx.app.log("ErrorState", "Error message: " + errMess);
        this.font = font;
        font.getData().setScale(1.5f);
        this.layout = new GlyphLayout(font, errMess, Color.WHITE,width * 0.8f, Align.left,true);
        this.returnMessage = new GlyphLayout(font, "Click anywhere to return", Color.WHITE, width * 0.8f, Align.left, true);
    }


    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            gsm.popAndReturn().dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        font.draw(sb, layout, (width - layout.width) / 2, (float) height * 4 / 5);
        font.draw(sb, returnMessage, (width - returnMessage.width) / 2, (float) height * 4 / 5 - layout.height  - 50);
        sb.end();
    }

    @Override
    public void dispose() {}
}