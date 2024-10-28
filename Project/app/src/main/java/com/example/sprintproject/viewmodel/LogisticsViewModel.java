package com.example.sprintproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class LogisticsViewModel extends ViewModel {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private final MutableLiveData<Integer> totalUsedDays = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalAllocatedDays = new MutableLiveData<>(0);

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
}