package com.example.sprintproject.model;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Accomodation {
    private String location;
    private Date checkInDate;
    private Date checkOutDate;
    private int numRooms;
    private String roomType;
    private String tripID;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Accomodation() {

    }
    public Accomodation(String location, Date checkInDate, Date checkOutDate, int numRooms,
                        String roomType, String tripID) {
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = numRooms;
        this.roomType = roomType;
        this.tripID = tripID;
    }

    public String getLocation() {
        return location;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
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

}
