package com.example.sprintproject.model;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.firebase.firestore.FirebaseFirestore;
public class TravelCommunity {
    private Date dateTime;
    private String accomodation;
    private String dining;
    private String transportation;
    private String notes;
    private String username; //to connect

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    //private static final String TAG = "TravelCommunityModel"; // Tag for logging

    public TravelCommunity() { }

    public TravelCommunity(Date dateTime, String accomodation, String dining, String transportation, String notes, String tripID) {
        this.dateTime = dateTime;
        this.accomodation = accomodation;
        this.dining = dining;
        this.transportation = transportation;
        this.notes = notes;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public String getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(String accomodation) {
        this.accomodation = accomodation;
    }

    public String getDining() {
        return dining;
    }
    public void setDining(String dining) {
        this.dining = dining;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }










}
