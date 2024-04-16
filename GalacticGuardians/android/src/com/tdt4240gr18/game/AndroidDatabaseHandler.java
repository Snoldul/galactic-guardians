package com.tdt4240gr18.game;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AndroidDatabaseHandler implements DatabaseInterface{

    public LeaderboardEntry getEntry(String username) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("").child("LeaderboardEntries").child(username);
        LeaderboardEntry entry = new LeaderboardEntry(username, 0);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer scoreValue = snapshot.child("score").getValue(Integer.class);
                int score = (scoreValue != null) ? scoreValue : -1;
                entry.setScore(score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
        return entry;
    }

    @Override
    public void getAllEntries(OnDataLoadedCallback callback) {

        ArrayList<LeaderboardEntry> entries = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("").child("LeaderboardEntries");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                    String username = entrySnapshot.getKey();
                    Integer scoreValue = entrySnapshot.child("score").getValue(Integer.class);
                    int score = (scoreValue != null) ? scoreValue : -1;
                    entries.add(new LeaderboardEntry(username, score));
                }
                callback.onDataLoaded(entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
    }

    @Override
    public void getEntriesPerPage(int pageNumber, int entriesPerPage, OnDataLoadedCallback callback) {

        ArrayList<LeaderboardEntry> entries = new ArrayList<>();
        ArrayList<LeaderboardEntry> entriesOnPage = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("").child("LeaderboardEntries");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot entrySnapshot : snapshot.getChildren()) {
                    String username = entrySnapshot.getKey();
                    Integer scoreValue = entrySnapshot.child("score").getValue(Integer.class);
                    int score = (scoreValue != null) ? scoreValue : -1;
                    entries.add(new LeaderboardEntry(username, score));
                }
                Collections.sort(entries, (t1, t2) -> t2.getScore() - t1.getScore());
                int startIndex = (pageNumber - 1) * entriesPerPage;
                int endIndex = Math.min(startIndex + entriesPerPage, entries.size());

                entriesOnPage.addAll(entries.subList(startIndex, endIndex));

                callback.onDataLoaded(entriesOnPage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
    }

    @Override
    public void addScoreToLeaderboard(String username, int score) {
        long time = System.currentTimeMillis();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("").child("LeaderboardEntries");

        Map<String, Object> entryData = new HashMap<>();
        entryData.put("score", score);
        entryData.put("time", time);

        ref.child(username).setValue(entryData)
                .addOnSuccessListener(aVoid -> System.out.println("Data added successfully!"))
                .addOnFailureListener(e -> System.out.println("Failed to add data: " + e.getMessage()));

    }

}
