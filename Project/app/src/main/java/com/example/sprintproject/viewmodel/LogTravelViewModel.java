package com.example.sprintproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sprintproject.model.Destination;

public class LogTravelViewModel extends ViewModel {
    private final MutableLiveData<Destination> destinationLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();

    public LiveData<Destination> getDestination() {
        return destinationLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageLiveData;
    }

    public void saveDestination(String location, String startDate, String endDate) {
        String validationMessage = validateInputs(location, startDate, endDate);

        if (validationMessage.isEmpty()) {
            Destination destination = new Destination(location, startDate, endDate);
            destinationLiveData.setValue(destination);
            errorMessageLiveData.setValue(null); // Clear any previous error message
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
        return ""; // All valid inputs
    }
}