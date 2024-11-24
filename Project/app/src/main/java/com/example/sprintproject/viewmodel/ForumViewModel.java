package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.TravelCommunity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ForumViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final MutableLiveData<List<TravelCommunity>> forums =
            new MutableLiveData<>(new ArrayList<>());
    private boolean sortByCheckIn = true;

    public LiveData<List<TravelCommunity>> getForums() {
        return forums;
    }

    public void loadForums() {
        db.collection("TravelCommunity")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<TravelCommunity> forumList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        forumList.add(doc.toObject(TravelCommunity.class));
                    }
                    forums.setValue(forumList);
                    sortPosts();
                })
                .addOnFailureListener(e -> forums.setValue(null));
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

    public void sortPosts() {
        if (forums.getValue() != null) {
            List<TravelCommunity> sortedList = new ArrayList<>(forums.getValue());
            Collections.sort(sortedList, (a, b) -> {
                if (sortByCheckIn) {
                    return a.getStartDate().compareTo(b.getStartDate());
                } else {
                    return a.getEndDate().compareTo(b.getEndDate());
                }
            });
            forums.setValue(sortedList);
        }
    }

    public void setSortByCheckIn(boolean sortByCheckIn) {
        this.sortByCheckIn = sortByCheckIn;
        sortPosts();
    }
}