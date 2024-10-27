package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.BaseUser;
import com.example.sprintproject.model.FirestoreManager;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class RegisterViewModel extends AndroidViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private FirebaseFirestore db;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        db = FirestoreManager.getInstance().getFirestore();
    }

    public MutableLiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void register(BaseUser baseUser) {
        mAuth.createUserWithEmailAndPassword(baseUser.getEmail(), baseUser.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // BaseUser registration successful
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            addUserToFirestore(userId, baseUser); // Store baseUser data in Firestore
                            registrationSuccess.setValue(true);
                        } else {
                            // Handle case where baseUser is null
                            errorMessage.setValue("Error getting baseUser information.");
                        }

                        registrationSuccess.setValue(true);
                    } else {
                        errorMessage.setValue("Authentication failed.");
                    }
                });
    }

    private void addUserToFirestore(String userId, BaseUser baseUser) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", baseUser.getEmail());
        userMap.put("password", baseUser.getPassword());
        userMap.put("startDate", baseUser.getStartDate());
        userMap.put("endDate", baseUser.getEndDate());
        userMap.put("totalAllocatedDays", baseUser.getTotalAllocatedDays());

        db.collection("Users")
                .document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "BaseUser data added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding baseUser data", e);
                    errorMessage.setValue("Error adding baseUser to Firestore.");
                });

    }
}