package com.tdt4240gr18.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.FirebaseApp;

public class AndroidLauncher extends AndroidApplication {
	private final AndroidDatabaseHandler databaseHandler = new AndroidDatabaseHandler();

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GalacticGuardians(databaseHandler), config);

		FirebaseApp.initializeApp(this);
	}
}
