package com.example.sprintproject.model;
import java.util.Date;

public class Dining {
    private String location;
    private Date dateTime; // Make sure this is a `Date` type
    private String website;
    private String userId;

    // Default constructor for Firestore
    public Dining() {}

    public Dining(String location, Date dateTime, String website, String userId) {
        this.location = location;
        this.dateTime = dateTime;
        this.website = website;
        this.userId = userId;
    }

    // Getters and setters
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
