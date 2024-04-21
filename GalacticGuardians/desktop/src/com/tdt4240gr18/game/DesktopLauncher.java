package com.tdt4240gr18.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.tdt4240gr18.GalacticGuardians;
import com.tdt4240gr18.game.misc.LeaderboardEntry;
import com.tdt4240gr18.services.database.DatabaseInterface;
import com.tdt4240gr18.utils.OnDataLoadedCallback;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		DatabaseInterface databaseInterface = new DatabaseInterface() {

			@Override
			public void registerUser(String email, String password, String username, OnRegistrationListener listener) {

			}

			@Override
			public void loginUser(String email, String password, OnLoginListener listener) {

			}

			@Override
			public void logoutUser() {

			}

			@Override
			public void getEmailByUsername(String username, OnEntryLoadedListener listener) {

			}

			@Override
			public void getUsernameByEmail(String email, OnEntryLoadedListener listener) {

			}

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

			@Override
			public void getScoreFromLeaderboard(String username, OnEntryLoadedListener listener) {

			}

		};
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("GalacticGuardians");
		config.setWindowedMode(1080 / 3, 2400 / 3);
		new Lwjgl3Application(new GalacticGuardians(databaseInterface), config);
	}
}
