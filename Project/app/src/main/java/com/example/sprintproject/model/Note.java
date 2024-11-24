package com.example.sprintproject.model;

import java.util.Date;

public class Note {
    private String text;
    private Date timestamp;
    private String tripID;
    private String userEmail; // Added field for user's email

    // Required empty constructor for Firestore
    public Note() {}

    public Note(String text, Date timestamp, String tripID, String userEmail) {
        this.text = text;
        this.timestamp = timestamp;
        this.tripID = tripID;
        this.userEmail = userEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}