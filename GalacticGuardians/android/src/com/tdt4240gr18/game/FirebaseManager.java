package com.tdt4240gr18.game;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseManager {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore mFirestore;

    public FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    // Registration method
    public void registerUser(String email, String password, String username, Activity activity, OnRegistrationListener listener){
    mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity, task -> {
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

    // Login method
    public void loginUser(String email, String password, Activity activity, OnLoginListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                    } else {
                        String errorMessage = (Objects.requireNonNull(task.getException()).getMessage());
                        listener.onFailure(errorMessage);
                    }
                });
    }

    public void logoutUser() {
        mAuth.signOut();
    }

    public interface OnRegistrationListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface OnLoginListener {
        void onSuccess(String userID);
        void onFailure(String errorMessage);
    }
}
