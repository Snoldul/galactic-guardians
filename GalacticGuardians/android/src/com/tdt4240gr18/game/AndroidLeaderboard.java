package com.tdt4240gr18.game;

/** Android implementation, can access PlayGames directly **/
public class AndroidLeaderboard implements Leaderboard {

    public void submitScore(String user, int score) {
        // Ignore the user name, because Google Play reports the score for the currently signed-in player
        // See https://developers.google.com/games/services/android/signin for more information on this
        // PlayGames.getLeaderboardsClient(activity).submitScore(getString(R.string.leaderboard_id), score);
    }
}
