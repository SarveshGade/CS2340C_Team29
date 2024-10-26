package com.example.sprintproject.view.location;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;
import com.example.sprintproject.view.forum.ForumActivity;

public class LocationActivity extends AppCompatActivity {
    private LinearLayout destinationContainer;
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
            final android.widget.EditText destinationInput = new android.widget.EditText(LocationActivity.this);
            destinationInput.setHint("Destination (Optional)");
            layout.addView(destinationInput);

            // Set the layout to the dialog
            builder.setView(layout);

            // Add positive and negative buttons
            builder.setPositiveButton("Calculate", (dialog, which) -> {
                // Retrieve the start and end dates
                String startDate = startDateInput.getText().toString().trim();
                String endDate = endDateInput.getText().toString().trim();

                // Validate the dates
                if (!startDate.isEmpty() && !endDate.isEmpty()) {
                    try {
                        // Parse the dates using SimpleDateFormat
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date start = sdf.parse(startDate);
                        java.util.Date end = sdf.parse(endDate);

                        // Calculate the difference in milliseconds
                        long differenceInMillis = end.getTime() - start.getTime();

                        // Convert milliseconds to days
                        long daysBetween = differenceInMillis / (1000 * 60 * 60 * 24);

                        // Show the result
                        android.widget.Toast.makeText(LocationActivity.this, "Days between: " + daysBetween, android.widget.Toast.LENGTH_LONG).show();
                    } catch (java.text.ParseException e) {
                        android.widget.Toast.makeText(LocationActivity.this, "Invalid date format!", android.widget.Toast.LENGTH_LONG).show();
                    }
                } else {
                    android.widget.Toast.makeText(LocationActivity.this, "Please enter both start and end dates!", android.widget.Toast.LENGTH_LONG).show();
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

}
