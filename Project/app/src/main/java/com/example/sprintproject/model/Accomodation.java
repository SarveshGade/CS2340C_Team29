package com.example.sprintproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Accomodation {
    private String location;
    private String checkInDate;
    private String checkOutDate;
    private int numRooms;
    private String roomType;
    private String userId;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public Accomodation(String location, String checkInDate, String checkOutDate, int numRooms, String roomType, String userId) {
        this.location = location;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numRooms = numRooms;
        this.roomType = roomType;
        this.userId = userId;
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

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
