package com.example.sprintproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LogisticsViewModel extends ViewModel {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private final MutableLiveData<Integer> totalUsedDays = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalAllocatedDays = new MutableLiveData<>(0);

    private final MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>(new ArrayList<>());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
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


    public void addNote(Note note) {
        db.collection("Notes").add(note)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Note added with ID: " + documentReference.getId());
                    // Optionally reload notes to refresh the list
                    fetchNotesByTripId(note.getTripID());
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error adding note", e));
    }

    public void fetchNotesByTripId(String tripId) {
        db.collection("Notes")
                .whereEqualTo("tripId", tripId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Note> noteList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Note note = document.toObject(Note.class);
                        noteList.add(note);
                    }
                    notesLiveData.setValue(noteList);
                    Log.d(TAG, "Fetched " + noteList.size() + " notes for tripId: " + tripId);
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching notes", e));
    }
}