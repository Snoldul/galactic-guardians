package com.tdt4240gr18.game;

public interface DatabaseInterface {

    void getAllEntries(OnDataLoadedCallback callback);

    void getEntriesPerPage(int pageNumber, int entriesPerPage, OnDataLoadedCallback callback);

    LeaderboardEntry getEntry(String username);


    void addScoreToLeaderboard(String username, int score);

}
