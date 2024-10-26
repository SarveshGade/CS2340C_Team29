package com.example.sprintproject.model;


import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreManager {
    private volatile static FirestoreManager uniqueInstance;
    private final FirebaseFirestore firestore;

    private FirestoreManager() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static FirestoreManager getInstance() {
        if (uniqueInstance == null) {
            synchronized (FirestoreManager.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new FirestoreManager();
                }
            }
        }
        return uniqueInstance;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }
}
