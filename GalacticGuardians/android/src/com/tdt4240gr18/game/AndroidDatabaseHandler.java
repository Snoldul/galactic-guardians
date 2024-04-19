package com.tdt4240gr18.game;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AndroidDatabaseHandler implements DatabaseInterface{
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;

    public AndroidDatabaseHandler() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

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
    public void registerUser(String email,  String username, String password,OnRegistrationListener listener){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        CollectionReference usersCollection = mFirestore.collection("users");
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", email);
                        user.put("username", username);

                        usersCollection.add(user)
                                .addOnSuccessListener(documentReference -> Log.d("TAG", "DocumentSnapshot added with ID: " + userID))
                                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));

                        listener.onSuccess();
                    }
                    else {
                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        listener.onFailure(errorMessage);
                    }
                });
    }

    public void loginUser(String email, String password, OnLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        listener.onFailure(errorMessage);
                    }
                });
    }

    public void logoutUser() {
        mAuth.signOut();
        UserSession.getInstance().setIsLoggedIn(false);
        UserSession.getInstance().setUsername(null);
    }

    public void getEmailByUsername(String username, OnEntryLoadedListener listener) {
        mFirestore.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            String email = task.getResult().getDocuments().get(0).getString("email");
                            listener.onSuccess(email);
                        } else {
                            listener.onFailure("No user found with this username");
                        }
                    } else {
                        listener.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    @Override
    public void getUsernameByEmail(String email, OnEntryLoadedListener listener) {
        mFirestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            String username = task.getResult().getDocuments().get(0).getString("username");
                            listener.onSuccess(username);
                        } else {
                            listener.onFailure("No user found with this username");
                        }
                    } else {
                        listener.onFailure(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
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

    @Override
    public void getScoreFromLeaderboard(String username, OnEntryLoadedListener listener) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("").child("LeaderboardEntries").child(username);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer scoreValue = snapshot.child("score").getValue(Integer.class);
                int score = (scoreValue != null) ? scoreValue : -1;
                listener.onSuccess(String.valueOf(score));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to read value: " + error.toException());
            }
        });
    }

}
