package com.example.sprintproject.model;

public interface ITraveler {
    String getStartDate();
    String getEndDate();
    int getTotalAllocatedDays();
    String getTripID();
    void addTravelerToFirestore(String travelerInfo);
}
