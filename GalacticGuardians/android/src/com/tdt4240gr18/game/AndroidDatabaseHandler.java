package com.tdt4240gr18.game;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AndroidDatabaseHandler implements DatabaseInterface{

    @Override
    public void fetchDataFromDatabase() {
        // Fetch data from database

    }

    @Override
    public void insertTestData() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("");
            //Define path where I want entry
            DatabaseReference testRef = ref.child("LeaderboardEntries");

            // Create a unique key for new entry
            String entryKey = testRef.push().getKey();

            // Create data to add
            String userID = "UserID";
            int score = 123;

            Map<String, Object> entryData = new HashMap<>();
            entryData.put("userID", userID);
            entryData.put("score", score);



            //Add data to specified path
            assert entryKey != null;
            testRef.child(entryKey).setValue(entryData)
                    .addOnSuccessListener(aVoid -> System.out.println("Data added successfully!"))
                    .addOnFailureListener(e -> System.out.println("Failed to add data: " + e.getMessage()));
        }
}
