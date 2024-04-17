package com.tdt4240gr18.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Objects;

public class LeaderboardEntry {
    private final String username;
    private int score;

    public LeaderboardEntry(String username, int score) {
        this.username = username;
        setScore(score);
    }

    public void render(SpriteBatch sb, BitmapFont font, int x, int y, int boxWidth, int rank) {
        GlyphLayout layoutUID = new GlyphLayout(font, username);
        GlyphLayout layoutScore = new GlyphLayout(font, Integer.toString(score));
        // Gdx.app.log("scoreWidth", Integer.toString((int) layoutScore.width));

        GlyphLayout layoutPos = new GlyphLayout(font, rank + ". ");
        int xPos = 0;
        boolean posReached = false;
        int UIDWidth = (int) layoutUID.width;
        int scoreWidth = (int) layoutScore.width;
        int posWidth = (int) layoutPos.width;
        int dotWidth = (int) (font.getData().getGlyph('.').width * font.getScaleX());
        StringBuilder textBuilder = new StringBuilder();
        //StringBuilder temptextBuilder = new StringBuilder();

        for (int i = dotWidth; i < boxWidth - scoreWidth; i += dotWidth) {
            if (!posReached){
                if (i > UIDWidth + posWidth) {
                    xPos = i;
                    posReached = true;
                }
            }
            else {
                textBuilder.append(".");
            }
        }
/*        // FOR TESTING ONLY
        for (int i = dotWidth; i < boxWidth; i += dotWidth) {
            temptextBuilder.append(".");
        }
        String tempdots = temptextBuilder.toString();
        font.draw(sb, tempdots, x, y);*/

        String dots = textBuilder.toString();
        font.draw(sb, layoutPos, x, y);
        font.draw(sb, layoutUID, x + posWidth, y);
        font.draw(sb, dots, xPos + x, y);
        font.draw(sb, layoutScore, x + boxWidth - scoreWidth, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LeaderboardEntry that = (LeaderboardEntry) obj;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
