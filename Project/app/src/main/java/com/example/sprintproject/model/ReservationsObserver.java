package com.example.sprintproject.model;

import java.util.List;

public interface ReservationsObserver {
    public void onReservationsLoaded(List<Reservable> reservations);
}