package com.tdt4240gr18.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MenuButton {
    private final ggTexture buttonTexture;
    private final Rectangle bounds;
    private BitmapFont font;
    private GlyphLayout layout;
    private String buttonText;


    public MenuButton(ggTexture texture, String text, float x, float y) {
        this.buttonTexture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
        this.font = new BitmapFont(Gdx.files.internal("RetroTitle.fnt"));
        this.buttonText = text;
        float textScale = 2;
        font.getData().setScale(textScale);
        this.layout = new GlyphLayout(font, buttonText);


    }
    public MenuButton(ggTexture texture, float x, float y) {
        this.buttonTexture = texture;
        this.bounds = new Rectangle(x, y, texture.getWidth(), texture.getHeight());

    }

    public void render(SpriteBatch sb) {
        sb.draw(buttonTexture, bounds.x, bounds.y);

        float textWidth = layout.width;
        float textHeight = layout.height;

        float textX = bounds.x + ((bounds.width - textWidth) / 2);
        float textY = bounds.y + ((bounds.height + textHeight) / 2) + 12;

        font.draw(sb, layout, textX, textY);
    }

    public void render(SpriteBatch sb, boolean containsText) { // containText is used in order to overload the constructor for buttons that don't have text
        sb.draw(buttonTexture, bounds.x, bounds.y);
    }

    public boolean isClicked(float x, float y) {
        return bounds.contains(x, y);
    }

    public void dispose() {
        buttonTexture.dispose();
        if (font != null) {
            font.dispose();
        }
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setY(float y) {
        bounds.y = y;
    }

    public void setX(float x) {
        bounds.x = x;
    }

    public ggTexture getTexture() {
        return buttonTexture;
    }

    public int getY() {
        return (int) bounds.y;
    }
}