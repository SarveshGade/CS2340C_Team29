package com.example.sprintproject.model;
import java.util.Date;

public class Dining {
    private String location;
    private Date dateTime;
    private String website;
    private String tripID;

    public Dining() { }

    public Dining(String location, Date dateTime, String website, String tripID) {
        this.location = location;
        this.dateTime = dateTime;
        this.website = website;
        this.tripID = tripID;
    }

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

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }
}
