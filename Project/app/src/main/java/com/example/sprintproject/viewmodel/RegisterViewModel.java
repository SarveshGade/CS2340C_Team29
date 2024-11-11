package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.FirestoreManager;
import com.example.sprintproject.model.Traveler;
import com.example.sprintproject.model.Trip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public void register(Traveler traveler) {
        mAuth.createUserWithEmailAndPassword(traveler.getEmail(), traveler.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            addUserToFirestore(userId, traveler);

                            ArrayList<String> users = new ArrayList<>();
                            users.add(userId);

                            Trip newTrip = new Trip(users, new ArrayList<>());
                            addTripToFirestore(userId, newTrip);

                            registrationSuccess.setValue(true);
                        } else {
                            errorMessage.setValue("Error getting user information.");
                        }
                    } else {
                        errorMessage.setValue("Authentication failed.");
                    }
                });
    }

    public String validateEmail(String email) {
        if (email.isEmpty()) {
            return "Email cannot be empty";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Not a valid email";
        }
        return null;
    }

    public boolean containsDigit(String password) {
        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public String validatePassword(String password) {
        if (password.isEmpty()) {
            return "Password cannot be empty";
        }
        if (!containsDigit(password)) {
            return "Doesn't contain digit";
        }
        if (password.length() < 6) {
            return "Password is too short";
        }
        return null;
    }

    private void addUserToFirestore(String userId, Traveler traveler) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", traveler.getEmail());
        userMap.put("password", traveler.getPassword());
        userMap.put("startDate", traveler.getStartDate());
        userMap.put("endDate", traveler.getEndDate());
        userMap.put("totalAllocatedDays", traveler.getTotalAllocatedDays());
        userMap.put("totalUsedDays", traveler.getTotalUsedDays());

        db.collection("Users")
                .document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> Log.d("Firestore",
                        "Traveler data added successfully"))
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding traveler data", e);
                    errorMessage.setValue("Error adding traveler to Firestore.");
                });
    }

    private void addTripToFirestore(String userId, Trip trip) {
        Map<String, Object> tripMap = new HashMap<>();
        tripMap.put("travelers", trip.getTravelers());
        tripMap.put("notes", trip.getNotes());

        db.collection("Trips")
                .add(tripMap) // Automatically generates a unique trip ID
                .addOnSuccessListener(documentReference -> {
                    String tripID = documentReference.getId();
                    Log.d("Firestore", "Trip created with ID: " + tripID);

                    // Update the user document with the trip ID
                    db.collection("Users")
                            .document(userId)
                            .update("tripID", tripID)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore",
                                    "Traveler updated with trip ID"))
                            .addOnFailureListener(e -> {
                                Log.w("Firestore", "Error updating traveler with trip ID", e);
                                errorMessage.setValue("Error linking trip to user.");
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding trip data", e);
                    errorMessage.setValue("Error creating trip for the user.");
                });
    }
}