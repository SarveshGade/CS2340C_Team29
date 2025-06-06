package com.example.sprintproject.view.location;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        // retrieve user's destinations
        destinationContainer = findViewById(R.id.destinationContainer);

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            fetchUserDestinations(userId);
        }

        calculateDays.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(LocationActivity.this);
            builder.setTitle("Calculate Vacation Time");

            LinearLayout layout = new LinearLayout(LocationActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 40);

            final android.widget.EditText startDateInput =
                    new android.widget.EditText(LocationActivity.this);
            startDateInput.setHint("Start Date (yyyy-mm-dd)");
            layout.addView(startDateInput);
            final android.widget.EditText endDateInput =
                    new android.widget.EditText(LocationActivity.this);
            endDateInput.setHint("End Date (yyyy-mm-dd)");
            layout.addView(endDateInput);
            final android.widget.EditText durationInput =
                    new android.widget.EditText(LocationActivity.this);
            durationInput.setHint("Duration (in days)");
            layout.addView(durationInput);
            builder.setView(layout);
            builder.setPositiveButton("Calculate", (dialog, which) -> {
                String startDate = startDateInput.getText().toString().trim();
                String endDate = endDateInput.getText().toString().trim();
                String durationString = durationInput.getText().toString().trim();
                Integer duration = null;
                if (!durationString.isEmpty()) {
                    duration = Integer.parseInt(durationString);
                }
                // validate the input
                if (startDate.isEmpty() && endDate.isEmpty()
                        || startDate.isEmpty() && duration == null
                        || endDate.isEmpty() && duration == null) {
                    android.widget.Toast.makeText(LocationActivity.this,
                            "Please fill in at least two values!",
                            android.widget.Toast.LENGTH_LONG).show();
                    return;
                }
                // calculate the missing value based on the provided inputs and update db
                try {
                    if (!startDate.isEmpty() && !endDate.isEmpty()) {
                        // dates are provided, calculate duration
                        duration = calculateDuration(startDate, endDate);
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            updateUserData(userId, startDate, endDate, duration);
                        }
                    } else if (!startDate.isEmpty() && duration != null) {
                        // calculate endDate based on startDate and duration
                        endDate = calculateEndDate(startDate, duration);
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            updateUserData(userId, startDate, endDate, duration);
                        }
                    } else if (!endDate.isEmpty() && duration != null) {
                        // calculate startDate based on endDate and duration
                        startDate = calculateStartDate(endDate, duration);
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            updateUserData(userId, startDate, endDate, duration);
                        }
                    } else {
                        android.widget.Toast.makeText(LocationActivity.this,
                                "Please enter two out of the three values!",
                                android.widget.Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (java.text.ParseException e) {
                    android.widget.Toast.makeText(LocationActivity.this,
                            "Invalid date format!", android.widget.Toast.LENGTH_LONG).show();
                } catch (NumberFormatException e) {
                    android.widget.Toast.makeText(LocationActivity.this,
                            "Invalid duration format!", android.widget.Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });

        logTravelButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,
                    LogTravelActivity.class);
            startActivity(intent);
        });
        logisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,
                    LogisticsActivity.class);
            startActivity(intent);
        });
        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,
                    LocationActivity.class);
            startActivity(intent);
        });
        diningButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,
                    DiningActivity.class);
            startActivity(intent);
        });
        accommodationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,
                    AccommodationsActivity.class);
            startActivity(intent);
        });
        forumButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationActivity.this,
                    ForumActivity.class);
            startActivity(intent);
        });
    }
    private void displayDestinations(List<Map<String, Object>> destinations) {
        destinationContainer.removeAllViews();
        Log.d("LocationActivity", "Displaying destinations: " + destinations);

        for (int i = 0; i < destinations.size(); i++) {
            Map<String, Object> destination = destinations.get(i);

            // Create a horizontal LinearLayout for each destination
            LinearLayout destinationLayout = new LinearLayout(this);
            destinationLayout.setOrientation(LinearLayout.HORIZONTAL);
            destinationLayout.setPadding(16, 16, 16, 16); // Add padding for spacing
            destinationLayout.setGravity(Gravity.CENTER_VERTICAL);

            // Alternate background color of rows
            if (i % 2 == 0) {
                destinationLayout.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else {
                destinationLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            // Create and set the "Destination" TextView
            TextView destinationTextView = new TextView(this);
            String location = (String) destination.get("location");
            destinationTextView.setText(location);
            destinationTextView.setTypeface(null, Typeface.BOLD);
            destinationTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)); // Weight 1 to align left

            // Get the planned days from the "duration" field
            Long duration = (Long) destination.get("duration");
            String daysPlanned = (duration != null ? duration : 0) + " days planned";

            // Create and set the days planned TextView
            TextView daysTextView = new TextView(this);
            daysTextView.setText(daysPlanned);
            daysTextView.setGravity(Gravity.END);

            // Add both TextViews to the LinearLayout
            destinationLayout.addView(destinationTextView);
            destinationLayout.addView(daysTextView);

            // Add the LinearLayout to the container
            destinationContainer.addView(destinationLayout);
        }
    }

    private void fetchUserDestinations(String userId) {
        Log.d("LocationActivity", "Fetching destinations for user: " + userId);
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String tripID = userDoc.getString("tripID");
                    if (tripID != null) {
                        Log.d("LocationActivity", "Trip ID fetched: " + tripID);
                        db.collection("destinations")
                                .whereEqualTo("tripID", tripID)
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        List<Map<String, Object>> destinations = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Map<String, Object> destinationData = new HashMap<>();
                                            destinationData.put("destinationID", document.getId());
                                            destinationData.put("duration",
                                                    document.getLong("duration"));
                                            destinationData.put("endDate",
                                                    document.getString("endDate"));
                                            destinationData.put("location",
                                                    document.getString("location"));
                                            destinationData.put("startDate",
                                                    document.getString("startDate"));
                                            destinationData.put("userId",
                                                    document.getString("userId"));
                                            destinations.add(destinationData);
                                        }

                                        // Sort by startDate in descending order
                                        destinations.sort((d1, d2) -> {
                                            String date1 = (String) d1.get("startDate");
                                            String date2 = (String) d2.get("startDate");
                                            return date2.compareTo(date1); //descending order
                                        });

                                        // Take the last 5 trips (most recent first)
                                        if (destinations.size() > 5) {
                                            destinations = destinations.subList(0, 5);
                                        }

                                        Log.d("LocationActivity",
                                                "Destinations fetched: " + destinations.size());
                                        // Display destinations if data is fetched
                                        displayDestinations(destinations);
                                    } else {
                                        Log.w("LocationActivity",
                                                "Error fetching destinations: ",
                                                task.getException());
                                        Toast.makeText(LocationActivity.this,
                                                "Error fetching destinations",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Log.w("LocationActivity", "Trip ID is null.");
                        Toast.makeText(LocationActivity.this,
                                "User does not have a trip ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.w("LocationActivity", "Error fetching user document: ", e);
                    Toast.makeText(LocationActivity.this,
                            "Error retrieving user data", Toast.LENGTH_SHORT).show();
                });
    }

    public static int calculateDuration(String startDate, String endDate)
            throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start = sdf.parse(startDate);
        java.util.Date end = sdf.parse(endDate);
        return (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
    }
    public static String calculateEndDate(String startDate, int duration)
            throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start = sdf.parse(startDate);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(start);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, duration);
        return sdf.format(calendar.getTime());
    }
    public static String calculateStartDate(String endDate, int duration)
            throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date end = sdf.parse(endDate);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -duration);
        return sdf.format(calendar.getTime());
    }
    public void updateUserData(String userId, String startDate,
                               String endDate, int totalAllocatedDays) {

        // create a map to hold the fields you want to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("startDate", startDate);
        updates.put("endDate", endDate);
        updates.put("totalAllocatedDays", totalAllocatedDays);

        // update the document with the given ID
        db.collection("Users")
                .document(userId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    // successfully updated
                    Log.d("Firestore", "User data updated successfully.");

                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.w("Firestore", "Error updating user data", e);
                });
    }

}
