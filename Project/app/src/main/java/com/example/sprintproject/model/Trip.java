package com.example.sprintproject.model;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private List<String> travelerIDs;
    private List<Note> notes;

    public Trip(ArrayList<String> ids, ArrayList<Note> notes) {
        this.travelerIDs = ids;
        this.notes = notes;
    }

    public List<String> getTravelers() {
        return travelerIDs;
    }

    public void setTravelers(List<String> travelers) {
        this.travelerIDs = travelers;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
}
