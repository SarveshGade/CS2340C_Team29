package com.example.sprintproject.model;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.example.sprintproject.model.User;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String password;


    public User() {
        // This constructor is intentionally empty.
        // You can initialize default values here if needed.
        this.email = ""; // Default email
        this.password = ""; // Default password
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void addUserToFirestore(String userInfo) {
        FirebaseFirestore db = FirestoreManager.getInstance().getFirestore();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);

        db.collection("users")
                .document(userInfo) // Use email as the document ID
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding user", e);
                });
    }
}