package com.tdt4240gr18.services.database;

import com.tdt4240gr18.game.misc.LeaderboardEntry;
import com.tdt4240gr18.utils.OnDataLoadedCallback;

import java.util.List;

public interface DatabaseInterface {

    void registerUser(String email, String username, String password, OnRegistrationListener listener);

    void loginUser(String email, String password, OnLoginListener listener);

    void logoutUser();

    void getEmailByUsername(String username, OnEntryLoadedListener listener);

    void getUsernameByEmail(String email, OnEntryLoadedListener listener);

    void checkIfUserExists(String username, String email, DatabaseInterface.OnCheckUserListener userAlreadyExists);



    void getAllEntries(OnAllEntriesLoadedListener listener);

    void getEntriesPerPage(int pageNumber, int entriesPerPage, OnDataLoadedCallback callback);

    void getEntry(String username, onFullEntryLoadedListener listener);

    void addScoreToLeaderboard(String username, int score);

    void getScoreFromLeaderboard(String username, OnEntryLoadedListener listener);

    interface OnAllEntriesLoadedListener {
        void onAllEntriesLoaded(List<LeaderboardEntry> entries);
    }
    interface onFullEntryLoadedListener {
        void onSuccess(LeaderboardEntry entry);
        void onFailure(String errorMessage);
    }
    interface OnRegistrationListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    interface OnLoginListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
    interface OnEntryLoadedListener {
        void onSuccess(String entry);
        void onFailure(String errorMessage);
    }

    interface OnCheckUserListener {
        void onSuccess(boolean userExists);
        void onFailure(String errorMessage);
    }
}
