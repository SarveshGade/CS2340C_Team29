package com.example.sprintproject.view.location;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.example.sprintproject.model.FirestoreManager;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;
import com.example.sprintproject.view.forum.ForumActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LocationActivity extends AppCompatActivity {
    private LinearLayout destinationContainer;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_location);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);

        Button logTravelButton = findViewById(R.id.logTravelButton);
        Button calculateDays = findViewById(R.id.calculateVacationTime);

        mAuth = FirebaseAuth.getInstance();
        db = FirestoreManager.getInstance().getFirestore();

        calculateDays.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LocationActivity.this);
            builder.setTitle("Calculate Vacation Time");

            LinearLayout layout = new LinearLayout(LocationActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 40);

            // input for start date
            final android.widget.EditText startDateInput = new android.widget.EditText(LocationActivity.this);
            startDateInput.setHint("Start Date (yyyy-MM-dd)");
            layout.addView(startDateInput);

            // input for end date
            final android.widget.EditText endDateInput = new android.widget.EditText(LocationActivity.this);
            endDateInput.setHint("End Date (yyyy-MM-dd)");
            layout.addView(endDateInput);

            //  the optional destination input
            final android.widget.EditText durationInput = new android.widget.EditText(LocationActivity.this);
            durationInput.setHint("Duration (in days)");
            layout.addView(durationInput);

            // set the layout to the dialog
            builder.setView(layout);

            // add positive and negative buttons
            builder.setPositiveButton("Calculate", (dialog, which) -> {
                // retrieve the start and end dates
                String startDate = startDateInput.getText().toString().trim();
                String endDate = endDateInput.getText().toString().trim();
                String durationString = durationInput.getText().toString().trim();
                Integer duration = null;

                // check if duration is not empty and parse it
                if (!durationString.isEmpty()) {
                    duration = Integer.parseInt(durationString);
                }
                // Log.d("CalculateDialog", "Initial Values - StartDate: " + startDate + ", EndDate: " + endDate + ", Duration: " + duration);

                // validate the input
                if (startDate.isEmpty() && endDate.isEmpty() && duration == null) {
                    // Log.d("CalculateDialog", "Initial Values - StartDate: " + startDate + ", EndDate: " + endDate + ", Duration: " + duration);
                    android.widget.Toast.makeText(LocationActivity.this, "Please fill in at least one value!", android.widget.Toast.LENGTH_LONG).show();
                    return;
                }

                // calculate the missing value based on the provided inputs and update firestore database
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                try {
                    if (!startDate.isEmpty() && !endDate.isEmpty()) {
                        // dates are provided, calculate duration
                        duration = calculateDuration(startDate, endDate);
                        // Log.d("CalculateDialog", "Calculated Duration: " + duration);
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            updateUserData(userId, startDate, endDate, duration);
                        }
                    } else if (!startDate.isEmpty() && duration != null) {
                        // calculate endDate based on startDate and duration
                        endDate = calculateEndDate(startDate, duration);
                        // Log.d("CalculateDialog", "Calculated EndDate: " + endDate);
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            updateUserData(userId, startDate, endDate, duration);
                        }
                    } else if (!endDate.isEmpty() && duration != null) {
                        // calculate startDate based on endDate and duration
                        startDate = calculateStartDate(endDate, duration);
                        // Log.d("CalculateDialog", "Calculated StartDate: " + startDate);
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            updateUserData(userId, startDate, endDate, duration);
                        }
                    } else {
                        // Log.d("CalculateDialog", "Insufficient data to calculate a missing value.");
                        android.widget.Toast.makeText(LocationActivity.this, "Please enter two out of the three values!", android.widget.Toast.LENGTH_LONG).show();
                        return;
                    }
                    // save to user here

                } catch (java.text.ParseException e) {
                    android.widget.Toast.makeText(LocationActivity.this, "Invalid date format!", android.widget.Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    android.widget.Toast.makeText(LocationActivity.this, "Invalid duration format!", android.widget.Toast.LENGTH_LONG).show();
                }
            });


            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            // Show the dialog
            builder.create().show();
        });

        logTravelButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, LogTravelActivity.class);
            startActivity(intent);
        });
        logisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });

        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, LocationActivity.class);
            startActivity(intent);
        });

        diningButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, DiningActivity.class);
            startActivity(intent);
        });

        accommodationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });

        forumButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this, ForumActivity.class);
            startActivity(intent);
        });
    }
    public static int calculateDuration(String startDate, String endDate) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start = sdf.parse(startDate);
        java.util.Date end = sdf.parse(endDate);
        return (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String calculateEndDate(String startDate, int duration) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start = sdf.parse(startDate);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, duration);
        return sdf.format(calendar.getTime());
    }
    public static String calculateStartDate(String endDate, int duration) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date end = sdf.parse(endDate);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -duration);
        return sdf.format(calendar.getTime());
    }
    public void updateUserData(String userId, String startDate, String endDate, int totalAllocatedDays) {

        // Create a map to hold the fields you want to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("startDate", startDate);
        updates.put("endDate", endDate);
        updates.put("totalAllocatedDays", totalAllocatedDays);

        // Update the document with the given ID
        db.collection("Users")
                .document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Successfully updated
                    Log.d("Firestore", "User data updated successfully.");

                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.w("Firestore", "Error updating user data", e);
                });
    }

}
