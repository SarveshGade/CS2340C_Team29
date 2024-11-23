package com.example.sprintproject.view.forum;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forum);
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
        Button addTripButton = findViewById(R.id.addTripButton); // Button to add a trip post

        addTripButton.setOnClickListener(v -> showAddTripDialog());

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, AccommodationsActivity.class);
                startActivity(intent);
            }
        });

        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });

    }
    private void showAddTripDialog() {
        // Create a vertical LinearLayout for the dialog content
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        scrollView.addView(layout);

        // Start date input
        EditText etStartDate = new EditText(this);
        etStartDate.setHint("Select Start Date");
        etStartDate.setFocusable(false);
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate, null));
        layout.addView(etStartDate);

        // End date input
        EditText etEndDate = new EditText(this);
        etEndDate.setHint("Select End Date");
        etEndDate.setFocusable(false);
        etEndDate.setOnClickListener(v -> {
            // Ensure end date cannot be before start date
            if (etStartDate.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please select a start date first.", Toast.LENGTH_SHORT).show();
            } else {
                showDatePicker(etEndDate, etStartDate.getText().toString());
            }
        });
        layout.addView(etEndDate);

        EditText etDestination = new EditText(this);
        etDestination.setHint("Destination");
        layout.addView(etDestination);

        EditText etAccommodations = new EditText(this);
        etAccommodations.setHint("Accommodations");
        layout.addView(etAccommodations);

        EditText etDining = new EditText(this);
        etDining.setHint("Dining Reservations");
        layout.addView(etDining);


        EditText etNotes = new EditText(this);
        etNotes.setHint("Notes about your trip");
        etNotes.setLines(4);
        layout.addView(etNotes);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Travel Post");
        builder.setView(scrollView);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();
            String accommodations = etAccommodations.getText().toString().trim();
            String dining = etDining.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            if (startDate.isEmpty() || endDate.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }
            // save to the firebase

            Toast.makeText(this, "Trip added successfully!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void showDatePicker(EditText etDate, String minDate) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    etDate.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        if (minDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                calendar.setTime(sdf.parse(minDate));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }

        datePickerDialog.show();
    }
}