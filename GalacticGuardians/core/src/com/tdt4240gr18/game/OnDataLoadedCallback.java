package com.tdt4240gr18.game;

import java.util.ArrayList;

public interface OnDataLoadedCallback {
    void onDataLoaded(ArrayList<LeaderboardEntry> entries);
}
