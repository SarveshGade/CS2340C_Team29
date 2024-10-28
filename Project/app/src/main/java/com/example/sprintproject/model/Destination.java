package com.example.sprintproject.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.firebase.firestore.FirebaseFirestore;

public class Destination {
    private String location;
    private String startDate;
    private String endDate;
    private int duration; // duration in days

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String TAG = "DestinationModel"; // Tag for logging

    public Destination(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = calculateDuration(); // Initial duration calculation
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
        this.duration = calculateDuration(); // Recalculate duration when dates change
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
        this.duration = calculateDuration(); // Recalculate duration when dates change
    }

    private int calculateDuration() {
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

    public static boolean isValidDate(String dateStr) {
        try {
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public void saveToFirestore() {
        FirebaseFirestore firestore = FirestoreManager.getInstance()
                .getFirestore();
        firestore.collection("destinations")
                .add(this)
                .addOnSuccessListener(documentReference ->
                        System.out.println("Destination saved with ID: "
                                + documentReference.getId()))
                .addOnFailureListener(e ->
                        System.err.println("Error saving destination: " + e.getMessage()));
    }
}