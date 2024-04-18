package com.tdt4240gr18.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;

public class ggTexture extends Texture {

    public ggTexture(String internalPath, float widthRatio) {
        super(new Pixmap(Gdx.files.internal(internalPath)));

        if (!getTextureData().isPrepared()) {
            getTextureData().prepare();
        }


        int newWidth = (int) (Gdx.graphics.getWidth() * widthRatio);
        float aspectRatio = (float) this.getHeight() / this.getWidth();
        int newHeight = (int) (newWidth * aspectRatio);

        Pixmap scaledPixmap = new Pixmap(newWidth, newHeight, Pixmap.Format.RGBA8888);

        Pixmap originalPixmap = getTextureData().consumePixmap();
        scaledPixmap.drawPixmap(originalPixmap,
                0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(),
                0, 0, scaledPixmap.getWidth(), scaledPixmap.getHeight());

        boolean isManaged = getTextureData().isManaged();

        // Create new Texturedata instance for this ggTexture
        TextureData textureData = new PixmapTextureData(scaledPixmap, null, false, false, isManaged);
        load(textureData);

        setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        scaledPixmap.dispose();
        originalPixmap.dispose();
    }
}
