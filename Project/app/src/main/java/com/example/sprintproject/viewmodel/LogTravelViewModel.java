package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Destination;
import com.example.sprintproject.model.FirestoreManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogTravelViewModel extends ViewModel {
    private final MutableLiveData<Destination> destinationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirestoreManager.getInstance().getFirestore();

    public LiveData<Destination> getDestination() {
        return destinationLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    public void saveDestination(String location, String startDate, String endDate) {
        String validationMessage = validateInputs(location, startDate, endDate);

        if (validationMessage.isEmpty()) {
            String userId = getUserId();
            db.collection("Users").document(userId)
                    .get()
                    .addOnSuccessListener(userDoc -> {
                        String tripID = userDoc.getString("tripID");

                        if (tripID != null) {
                            Destination destination = new Destination(location,
                                    startDate, endDate, tripID);
                            destinationLiveData.setValue(destination);
                            destination.saveToFirestore();

                            updateTotalUsedDays(userId, destination.getDuration());

                            errorMessageLiveData.setValue(null); // Clear any previous error message
                        } else {
                            errorMessageLiveData.setValue("User is not authenticated.");
                        }
                    });
        } else {
            errorMessageLiveData.setValue(validationMessage);
            destinationLiveData.setValue(null); // Clear destination on error
        }
    }

    private String validateInputs(String location, String startDate, String endDate) {
        if (location.isEmpty()) {
            return "Location cannot be empty.";
        }
        if (!Destination.isValidDate(startDate)) {
            return "Start date is invalid.";
        }
        if (!Destination.isValidDate(endDate)) {
            return "End date is invalid.";
        }
        return ""; // All inputs are valid
    }

    private String getUserId() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            return firebaseUser.getUid();
        }
        return null;
    }

    private void updateTotalUsedDays(String userId, int duration) {
        DocumentReference userRef = db.collection("Users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long currentTotalUsedDays = documentSnapshot.getLong("totalUsedDays");
                if (currentTotalUsedDays != null) {
                    // Update totalUsedDays
                    userRef.update("totalUsedDays", currentTotalUsedDays + duration);
                }
            }
        }).addOnFailureListener(e -> {
            errorMessageLiveData.setValue("Failed to update user's total used days.");
        });
    }
}