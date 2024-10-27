package com.example.sprintproject.model;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String email;
    private String password;
    private String startDate;
    private String endDate;
    private int totalAllocatedDays;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = "UserModel"; // Tag for logging

    public User() {
        // This constructor is intentionally empty.
        // You can initialize default values here if needed.
        this.email = ""; // Default email
        this.password = ""; // Default password
    }

    public User(String email, String password, String estStartDate, String estEndDate) {
        this.email = email;
        this.password = password;
        this.startDate = estStartDate;
        this.endDate = estEndDate;
        this.totalAllocatedDays = calculateTotalAllocatedDays();
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
    // Update method to save new dates and duration to Firestore
    public void updateDatesAndDuration() {
        FirebaseFirestore firestore = FirestoreManager.getInstance().getFirestore();

        firestore.collection("users")
                .document(this.email)
                .update(
                        "startDate", startDate,
                        "endDate", endDate,
                        "totalAllocatedDays", totalAllocatedDays
                )
                .addOnSuccessListener(aVoid ->
                        System.out.println("User dates and duration updated successfully."))
                .addOnFailureListener(e ->
                        System.err.println("Error updating user data: " + e.getMessage()));
    }
    private int calculateTotalAllocatedDays() {
        if (startDate == null || endDate == null) return 0;
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            if (end.before(start)) return 0;

            long diffInMillis = end.getTime() - start.getTime();
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing failed: " + e.getMessage(), e);
            return 0;
        }
    }
}