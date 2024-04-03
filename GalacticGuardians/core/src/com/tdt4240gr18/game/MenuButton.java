package com.tdt4240gr18.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MenuButton {
    private Texture buttonTexture;
    private Rectangle bounds;
    private BitmapFont font;
    private GlyphLayout layout;
    private String buttonText;
    private float textScale;


    public MenuButton(Texture texture, String text, float x, float y) {
        this.buttonTexture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        this.font = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        this.buttonText = text;
        textScale = 2;
        font.getData().setScale(textScale);

    }

    public void render(SpriteBatch sb) {
        sb.draw(buttonTexture, bounds.x, bounds.y);

        this.layout = new GlyphLayout(font, buttonText);

        float textWidth = layout.width;
        float textHeight = layout.height;

        float textX = bounds.x + ((bounds.width - textWidth) / 2);
        float textY = bounds.y + ((bounds.height + textHeight) / 2) + 12;

        font.draw(sb, layout, textX, textY);
    }

    public boolean isClicked(float x, float y) {
        return bounds.contains(x, y);
    }

    public void dispose() {
        buttonTexture.dispose();
        font.dispose();
    }

    public String getButtonText() {
        return buttonText;
    }

}
