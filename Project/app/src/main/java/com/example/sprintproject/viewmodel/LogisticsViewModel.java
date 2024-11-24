package com.example.sprintproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogisticsViewModel extends ViewModel {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private final MutableLiveData<Integer> totalUsedDays = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalAllocatedDays = new MutableLiveData<>(0);
    private final MutableLiveData<String> tripID = new MutableLiveData<>();
    private final MutableLiveData<List<Note>> notes = new MutableLiveData<>();
    private MutableLiveData<Boolean> inviteStatus = new MutableLiveData<>();
    private static final String TAG = "LogisticsViewModel";

    public LogisticsViewModel() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadUserData();
    }

    public MutableLiveData<Integer> getTotalUsedDays() {
        return totalUsedDays;
    }

    public MutableLiveData<Integer> getTotalAllocatedDays() {
        return totalAllocatedDays;
    }

    public MutableLiveData<String> getTripID() {
        return tripID;
    }

    public MutableLiveData<List<Note>> getNotes() {
        return notes;
    }

    public MutableLiveData<Boolean> getInviteStatus() {
        return inviteStatus;
    }

    private void loadUserData() {
        String userId = getUserId();
        if (userId != null) {
            db.collection("Users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.contains("totalAllocatedDays")) {
                        totalAllocatedDays.setValue(documentSnapshot
                                .getLong("totalAllocatedDays").intValue());
                    } else {
                        Log.w(TAG, "No totalAllocatedDays field found.");
                    }

                    if (documentSnapshot.contains("totalUsedDays")) {
                        totalUsedDays.setValue(documentSnapshot
                                .getLong("totalUsedDays").intValue());
                    } else {
                        Log.w(TAG, "No totalUsedDays field found.");
                    }

                    if (documentSnapshot.contains("tripID")) {
                        tripID.setValue(documentSnapshot
                                .getString("tripID"));
                        Log.w(TAG, "Trip id is: " + tripID.getValue());
                    } else {
                        Log.w(TAG, "No tripID field found.");
                    }
                } else {
                    Log.w(TAG, "Document does not exist.");
                }
            }).addOnFailureListener(e -> Log.e(TAG, "Error loading user data", e));
        } else {
            Log.w(TAG, "User is not authenticated.");
        }
    }

    private String getUserId() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getUid();
        }
        return null;
    }

    public void addNote(String text) {
        String tripIdValue = tripID.getValue();
        String userEmail = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getEmail() : "unknown_email";
        if (tripIdValue == null || tripIdValue.isEmpty()) {
            Log.w(TAG, "Trip ID is not set.");
            return;
        }

        Note newNote = new Note(text, new Date(), tripIdValue, userEmail);
        db.collection("Notes").add(newNote).addOnSuccessListener(documentReference -> {
            Log.i(TAG, "Note added successfully.");
            loadNotes(); // Reload notes after adding
        }).addOnFailureListener(e -> Log.e(TAG, "Error adding note", e));
    }

    public void loadNotes() {
        String tripIdValue = tripID.getValue();
        if (tripIdValue == null || tripIdValue.isEmpty()) {
            Log.w(TAG, "Trip ID is not set, cannot load notes.");
            return;
        }

        db.collection("Notes")
                .whereEqualTo("tripID", tripIdValue)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Note> loadedNotes = new ArrayList<>();
                    querySnapshot.forEach(doc -> loadedNotes.add(doc.toObject(Note.class)));
                    notes.setValue(loadedNotes); // Update LiveData
                    Log.i(TAG, "Notes loaded successfully.");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error loading notes", e));
    }

    public void inviteUser(String email) {
        if (email == null || email.isEmpty()) {
            inviteStatus.setValue(false); // Invalid email
            return;
        }

        // Observe the tripID LiveData (or directly access it)
        String currentTripID = tripID.getValue();
        if (currentTripID == null || currentTripID.isEmpty()) {
            Log.e("LogisticsViewModel", "trip id is null");
            inviteStatus.setValue(false); // Trip ID is unavailable
            return;
        }


        // Find the user by email in Firestore
        db.collection("Users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        Log.e("LogisticsViewModel", email + " not found");
                        inviteStatus.setValue(false); // User not found
                        return;
                    }

                    // Get the first (and only) document
                    DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                    String userID = doc.getId(); // Get user ID (document ID)

                    // Update the user's tripID in Firestore
                    db.collection("Users").document(userID)
                            .update("tripID", currentTripID)
                            .addOnSuccessListener(aVoid -> {
                                // Add the userID to the trip's participants list
                                db.collection("Trips").document(currentTripID)
                                        .update("travelers", FieldValue.arrayUnion(userID))
                                        .addOnSuccessListener(aVoid1 -> {
                                            inviteStatus.setValue(true); // Success
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("LogisticsViewModel",
                                                    "Error adding user to trip participants", e);
                                            inviteStatus.setValue(false);
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("LogisticsViewModel", "Error updating tripID", e);
                                inviteStatus.setValue(false); // Failure to update tripID
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("LogisticsViewModel", "Error querying user by email", e);
                    inviteStatus.setValue(false); // Error finding user
                });
    }
}