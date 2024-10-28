package com.example.sprintproject.model;
import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class BaseUser implements IUser {
    private String email;
    private String password;

    private static final String TAG = "UserModel"; // Tag for logging

    public BaseUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void addUserToFirestore(String userInfo) {
        FirebaseFirestore db = FirestoreManager.getInstance().getFirestore();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);

        db.collection("users")
                .document(userInfo) // Use email as the document ID
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "BaseUser added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding user", e);
                });
    }
}