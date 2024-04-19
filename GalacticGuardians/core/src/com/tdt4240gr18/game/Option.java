package com.tdt4240gr18.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Option {
    private String optionText;
    private final Rectangle optionBounds;
    private final Rectangle buttonBounds;
    private boolean isOn;
    private final Texture onTexture;
    private final Texture offTexture;
    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();

    public Option(String optionText, Texture onTexture, Texture offTexture, Rectangle optionBounds, Rectangle buttonBounds, boolean isOn){
        this.optionText = optionText;
        this.optionBounds = optionBounds;
        this.buttonBounds = buttonBounds;
        this.isOn = isOn;
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
        this.layout.setText(font, optionText);
        float textX = optionBounds.x;
        float centerY = optionBounds.y + optionBounds.height / 2;
        float textY = centerY + layout.height / 2;

        sb.draw(isOn ? onTexture : offTexture, buttonBounds.x, buttonBounds.y, buttonBounds.getWidth(), buttonBounds.getHeight());
        font.draw(sb, layout, textX, textY);
    }


    public boolean contains(float x, float y) {
        return buttonBounds.contains(x, y);
    }
    public String getOptionText() {
        return optionText;
    }

    public void dispose(){
        onTexture.dispose();
        offTexture.dispose();
    }
}
