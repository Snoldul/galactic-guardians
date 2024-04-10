package com.tdt4240gr18.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		DatabaseInterface databaseInterface = () -> {
			// TEST
		};
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("GalacticGuardians");
		config.setWindowedMode(1200, 800);
		new Lwjgl3Application(new GalacticGuardians(databaseInterface), config);
	}
}
