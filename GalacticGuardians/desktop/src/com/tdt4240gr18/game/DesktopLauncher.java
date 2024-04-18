package com.tdt4240gr18.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		DatabaseInterface databaseInterface = new DatabaseInterface() {

			@Override
			public void getAllEntries(OnDataLoadedCallback callback) {
			}

			@Override
			public void getEntriesPerPage(int pageNumber, int entriesPerPage, OnDataLoadedCallback callback) {
			}

			@Override
			public LeaderboardEntry getEntry(String username) {
				return null;
			}

			@Override
			public void addScoreToLeaderboard(String username, int score) {

			}

		};
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("GalacticGuardians");
		config.setWindowedMode(1080 / 3, 2400 / 3);
		new Lwjgl3Application(new GalacticGuardians(databaseInterface), config);
	}
}
