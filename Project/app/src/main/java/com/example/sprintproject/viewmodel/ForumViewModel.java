package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.ForumObserver;
import com.example.sprintproject.model.SortByCheckIn;
import com.example.sprintproject.model.SortByCheckOut;
import com.example.sprintproject.model.SortStrategy;
import com.example.sprintproject.model.SortingDecorator;
import com.example.sprintproject.model.TravelCommunity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForumViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final MutableLiveData<List<TravelCommunity>> forums =
            new MutableLiveData<>(new ArrayList<>());
    private boolean sortByCheckIn = true;
    private final List<ForumObserver> observers = new ArrayList<>();


    public LiveData<List<TravelCommunity>> getForums() {
        return forums;
    }
    public void addObserver(ForumObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ForumObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(List<TravelCommunity> updatedForums) {
        for (ForumObserver observer : observers) {
            observer.onForumsLoaded(updatedForums);
        }
    }

    private SortStrategy sortStrategy;

    public void loadForums() {

        db.collection("TravelCommunity")
                .get()
                .addOnSuccessListener(querySnapshot -> {


                    List<TravelCommunity> forumList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        forumList.add(doc.toObject(TravelCommunity.class));
                    }
                    if (forumList.isEmpty()) {
                        forumList.add(createDefaultPost());
                    }
                    forums.setValue(forumList);
                    sortPosts();
                })
                .addOnFailureListener(e -> forums.setValue(null));
    }
    private TravelCommunity createDefaultPost() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date start = null;
        Date end = null;
        try {
            start = dateFormat.parse("00-00-2024");
            end = dateFormat.parse("01-01-2024");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new TravelCommunity(
                "accommodation suggestions will be added here.",
                "destination suggestions will be added here.",
                "Dining suggestions will be added here.",
                "Please add your trip details.",
                start,
                end,
                "username"
        );
    }

    public String validateTripInput(Date checkIn, Date checkOut, String destination,
                                    String accommodations, String dining) {
        if (destination == null || destination.trim().isEmpty()) {
            return "Destination cannot be empty!";
        }
        if (accommodations == null || accommodations.trim().isEmpty()) {
            return "Accommodations cannot be empty!";
        }
        if (dining == null || dining.trim().isEmpty()) {
            return "Dining Reservations cannot be empty!";
        }
        if (checkIn == null) {
            return "Check-in date must be selected!";
        }
        if (checkOut == null) {
            return "Check-out date must be selected!";
        }
        if (!checkIn.before(checkOut)) {
            return "Check-in date must be before the check-out date!";
        }
        return null; // No errors
    }

    public void savePost(Date start, Date end, String destination, String accommodations,
                         String dining, String notes) {
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";

        db.collection("Users").document(userId).get().addOnSuccessListener(userDoc -> {
            String tripID = userDoc.getString("tripID");
            String username = userDoc.getString("email");
            TravelCommunity post = new TravelCommunity(accommodations, destination, dining,
                    notes, start, end, username);
            post.setTripID(tripID);
            db.collection("TravelCommunity").add(post).addOnSuccessListener(aVoid -> loadForums());
        });
    }

    public void setSortStrategy(SortStrategy strategy) {
        this.sortStrategy = strategy;
    }

    public void sortPosts() {
        if (forums.getValue() != null && sortStrategy != null) {
            List<TravelCommunity> sortedList = sortStrategy.sort(forums.getValue());
            forums.setValue(sortedList);
            notifyObservers(sortedList);
        }
    }

    public void setSortByCheckIn(boolean sortByCheckIn) {
        if (sortByCheckIn) {
            setSortStrategy(new SortingDecorator(new SortByCheckIn()));
        } else {
            setSortStrategy(new SortingDecorator(new SortByCheckOut()));
        }
        sortPosts();
    }
}