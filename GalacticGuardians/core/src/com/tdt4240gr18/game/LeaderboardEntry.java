package com.tdt4240gr18.game;

public class LeaderboardEntry {
    private final String UserID;
    private int score;

    public LeaderboardEntry(String UserID, int score) {
        this.UserID = UserID;
        setScore(score);
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
