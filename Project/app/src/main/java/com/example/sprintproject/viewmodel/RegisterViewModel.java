package com.example.sprintproject.viewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.sprintproject.model.User;
import com.example.sprintproject.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
        db = FirebaseFirestore.getInstance();
    }

    public MutableLiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void register(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            addUserToFirestore(userId, user); // Store user data in Firestore
                            registrationSuccess.setValue(true);
                        } else {
                            // Handle case where user is null
                            errorMessage.setValue("Error getting user information.");
                        }

                        registrationSuccess.setValue(true);
                    } else {
                        errorMessage.setValue("Authentication failed.");
                    }
                });
    }

    private void addUserToFirestore(String userId,User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("password", user.getPassword());

        db.collection("Users")
                .document(userId)
                .set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User data added successfully");
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding user data", e);
                    errorMessage.setValue("Error adding user to Firestore.");
                });

    }
}