package com.example.sprintproject.model;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Traveler extends BaseUser implements ITraveler {
    private String startDate;
    private String endDate;
    private int totalAllocatedDays;
    private int totalUsedDays;
    private String tripID;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = "TravelerModel"; // Tag for logging

    public Traveler(String email, String password, String startDate, String endDate, String tripID) {
        super(email, password);
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAllocatedDays = calculateTotalAllocatedDays();
        this.totalUsedDays = 0;
        this.tripID = tripID;
    }

    public Traveler(String email, String password) {
        this(email, password, "N/A", "N/A", "N/A");
    }

    public String getEmail() {
        return super.getEmail();
    }

    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String getTripID() { return tripID; };

    @Override
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

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public int getTotalUsedDays() {
        return totalUsedDays;
    }

    @Override
    public void addTravelerToFirestore(String travelerInfo) {
        FirebaseFirestore db = FirestoreManager.getInstance().getFirestore();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", super.getEmail());

        db.collection("Users")
                .document(travelerInfo) // Use email as the document ID
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Traveler added successfully");
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