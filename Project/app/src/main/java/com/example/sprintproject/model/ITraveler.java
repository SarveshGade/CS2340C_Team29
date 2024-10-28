package com.example.sprintproject.model;

public interface ITraveler {
    String getStartDate();
    String getEndDate();
    int getTotalAllocatedDays();
    void addTravelerToFirestore(String travelerInfo);
}
