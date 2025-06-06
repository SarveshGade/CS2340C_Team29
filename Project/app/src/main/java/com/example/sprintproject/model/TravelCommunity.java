package com.example.sprintproject.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TravelCommunity {
    private String accommodation;
    private String dining;
    private String notes;
    private String destination;
    private String tripID;
    private Date startDate;
    private Date endDate;
    private String username; //to connect

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public TravelCommunity(String accommodation, String destination,
                           String dining, String notes, Date startDate,
                           Date endDate, String username) {
        this.accommodation = accommodation;
        this.dining = dining;
        this.notes = notes;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.username = username;
    }

    public TravelCommunity() { }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public void setAccommodation(String accommodation) {
        this.accommodation = accommodation;
    }
    public String getAccommodation() {
        return accommodation;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDining() {
        return dining;
    }
    public void setDining(String dining) {
        this.dining = dining;
    }
    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
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
