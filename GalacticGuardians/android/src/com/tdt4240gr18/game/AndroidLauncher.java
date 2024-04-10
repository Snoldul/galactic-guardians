package com.tdt4240gr18.game;

import android.os.Bundle;
import android.util.Log;

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
		// TEMPORARY, FOR TESTING UNTIL "FRONTEND" BUTTONS ARE IMPLEMENTED
		//Don't know if this can be local and/or final
		FirebaseManager firebaseManager = new FirebaseManager();

		String testEmail = "test@test.no";
		String testPassword = "testPass123";
		String testUsername = "testeren";

		// TEMPORARY
		// Calling register user function
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

		// TEMPORARY
		// Calling login function
		firebaseManager.loginUser(testEmail, testPassword, this, new FirebaseManager.OnLoginListener() {
			@Override
			public void onSuccess(String userID) {
				// User logged in successfully
				// Actions that happen when user logs in goes here
				Log.d("Login success", "Login successful, userID: " + userID);
			}

			@Override
			public void onFailure(String errorMessage) {
				Log.e("Login", "Login failed: " + errorMessage);
			}
		});

		// TEMPORARY
		// Calling logout function
		firebaseManager.logoutUser();
	}
}
