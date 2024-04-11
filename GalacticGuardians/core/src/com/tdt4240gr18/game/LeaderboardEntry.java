package com.tdt4240gr18.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LeaderboardEntry {
    private final String UserID;
    private int score;

    public LeaderboardEntry(String UserID, int score) {
        this.UserID = UserID;
        setScore(score);
    }

    public void render(SpriteBatch sb, BitmapFont font, int x, int y, int boxWidth) {
        GlyphLayout layoutUID = new GlyphLayout(font, UserID);
        GlyphLayout layoutScore = new GlyphLayout(font, Integer.toString(score));
        int xPos = 0;
        int UIDWidth = (int) layoutUID.width;
        int scoreWidth = (int) layoutScore.width;
        int dotWidth = font.getData().getGlyph('.').width * (int) font.getScaleX();
        StringBuilder textBuilder = new StringBuilder();
        //StringBuilder temptextBuilder = new StringBuilder();

        for (int i = dotWidth; i < boxWidth; i += dotWidth) {
            if (i > UIDWidth) {
                xPos = i;
                break;
            }
        }
        for (int i = dotWidth; i < boxWidth - scoreWidth - dotWidth; i += dotWidth) {
            if (i > UIDWidth) {
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
        font.draw(sb, layoutUID, x, y);
        font.draw(sb, dots, xPos + x, y);
        font.draw(sb, layoutScore, x + boxWidth - scoreWidth, y);
    }

    public String getUserID() {
        return UserID;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
}
