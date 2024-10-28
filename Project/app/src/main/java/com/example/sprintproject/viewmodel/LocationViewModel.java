package com.example.sprintproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Destination;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends ViewModel {
    private final FirebaseFirestore db;
    private final MutableLiveData<List<Destination>> destinationListLiveData;

    public LocationViewModel() {
        db = FirebaseFirestore.getInstance();
        destinationListLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<List<Destination>> getDestinationListLiveData() {
        return destinationListLiveData;
    }

    public void fetchTopDestinationsForUser(String userId) {
        db.collection("destinations") // Replace with your collection name
                .whereEqualTo("userId", userId) // Filter by userId
                .orderBy("timestamp",
                        com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(5) // Limit to the top 5 entries
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Destination> destinations = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Destination destination = document.toObject(Destination.class);
                        destinations.add(destination);
                    }
                    destinationListLiveData.setValue(destinations);
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("LocationViewModel", "Error fetching destinations: ", e);
                });
    }
}