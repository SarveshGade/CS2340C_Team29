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
    private String userId;  // New field for the unique user ID
    private String startDate;
    private String endDate;
    private int totalAllocatedDays;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = "UserModel"; // Tag for logging

    public User() {
        this.email = "";
        this.password = "";
        this.userId = userId;
    }

    public User(String email, String password, String startDate, String endDate) {
        this.email = email;
        this.password = password;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAllocatedDays = calculateTotalAllocatedDays();
    }

    public User(String email, String password) {
        this(email, password, "0", "0");

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public int getTotalAllocatedDays() {
        return totalAllocatedDays;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public void setTotalAllocatedDays(int totalAllocatedDays) {
        this.totalAllocatedDays = totalAllocatedDays;
    }


    public void addUserToFirestore() {
        FirebaseFirestore db = FirestoreManager.getInstance().getFirestore();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("startDate", startDate);
        userMap.put("endDate", endDate);
        userMap.put("totalAllocatedDays", totalAllocatedDays);

        db.collection("users")
                .document(userId)// Use email as the document ID
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding user", e);
                });
    }

    private int calculateTotalAllocatedDays() {
        if (startDate == null || endDate == null) {
            return 0;
        }
        try {
            Date start = DATE_FORMAT.parse(startDate);
            Date end = DATE_FORMAT.parse(endDate);
            if (end.before(start)) {
                return 0;
            }

            long diffInMillis = end.getTime() - start.getTime();
            return (int) (diffInMillis / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing failed: " + e.getMessage(), e);
            return 0;
        }
    }
}