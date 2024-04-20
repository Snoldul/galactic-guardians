package com.tdt4240gr18.game;

public interface DatabaseInterface {

    void registerUser(String email, String username, String password, OnRegistrationListener listener);

    void loginUser(String email, String password, OnLoginListener listener);

    void logoutUser();

    void getEmailByUsername(String username, OnEntryLoadedListener listener);

    void getUsernameByEmail(String email, OnEntryLoadedListener listener);

    void checkIfUserExists(String username, String email, DatabaseInterface.OnCheckUserListener userAlreadyExists);



    void getAllEntries(OnDataLoadedCallback callback);

    void getEntriesPerPage(int pageNumber, int entriesPerPage, OnDataLoadedCallback callback);

    LeaderboardEntry getEntry(String username);

    void addScoreToLeaderboard(String username, int score);

    void getScoreFromLeaderboard(String username, OnEntryLoadedListener listener);


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
