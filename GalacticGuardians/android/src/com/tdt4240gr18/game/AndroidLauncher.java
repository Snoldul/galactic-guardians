package com.tdt4240gr18.game;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.firebase.FirebaseApp;

public class AndroidLauncher extends AndroidApplication {
	//Don't know if this can be local and/or final
	private FirebaseManager firebaseManager;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GalacticGuardians(), config);

		FirebaseApp.initializeApp(this);
		// TEMPORARY, FOR TESTING UNTIL "FRONTEND" BUTTONS ARE IMPLEMENTED
		firebaseManager = new FirebaseManager();

		String testEmail = "test@test.no";
		String testPassword = "testPass123";
		String testUsername = "Testeren1";

		firebaseManager.registerUser(testEmail, testPassword, testUsername, this, new FirebaseManager.OnRegistrationListener() {
			@Override
			public void onSuccess() {
				Log.d("Registration", "Registration successful");
			}

			@Override
			public void onFailure(String errorMessage) {
				Log.e("Registration", "Registration failed: " + errorMessage);
			}
		});


	}
}
