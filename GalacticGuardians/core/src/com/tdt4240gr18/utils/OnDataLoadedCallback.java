package com.tdt4240gr18.utils;

import com.tdt4240gr18.game.misc.LeaderboardEntry;

import java.util.ArrayList;

public interface OnDataLoadedCallback {
    void onDataLoaded(ArrayList<LeaderboardEntry> entries);
}
