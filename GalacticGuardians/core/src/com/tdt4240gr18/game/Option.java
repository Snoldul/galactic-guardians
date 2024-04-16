package com.tdt4240gr18.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Option {
    private String name;
    private Rectangle bounds;
    private boolean isOn;
    private Texture onTexture;
    private Texture offTexture;
    private BitmapFont font;
    private GlyphLayout layout;

    public Option(String name, Texture onTexture, Texture offTexture, Rectangle bounds){
        this.name = name;
        this.bounds = bounds;
        this.isOn = true;
        font = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        this.onTexture = onTexture;
        this.offTexture = offTexture;
        float textScale = 2;
        font.getData().setScale(textScale);
    }
    public void toggle() {
        isOn = !isOn;
    }
    public void render(SpriteBatch sb){
        this.layout = new GlyphLayout(font, name);
        float buttonSize = bounds.height * 0.65f;

        float textX = bounds.x;
        float centerY = bounds.y + bounds.height / 2;
        float textY = centerY + layout.height / 2;
        float imageX = bounds.x + bounds.width - buttonSize;
        float imageY = bounds.y + (bounds.height - buttonSize) / 2;

        sb.draw(isOn ? onTexture : offTexture, imageX, imageY, buttonSize, buttonSize);
        font.draw(sb, layout, textX, textY);
    }


    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    public void dispose(){
        onTexture.dispose();
        offTexture.dispose();
    }
}
