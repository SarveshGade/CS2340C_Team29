package com.example.sprintproject.view.dining;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Dining;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.view.location.LogTravelActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DiningActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Date selectedDateTime;
    private Button dateTimeButton;
    private LinearLayout reservationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dining);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        reservationList = findViewById(R.id.reservationList);

        Button reservationButton = findViewById(R.id.addReservation);
        reservationButton.setOnClickListener(v -> showReservationDialog());
        loadReservations();

    }

    private void showReservationDialog() {
        EditText locationInput = new EditText(this);
        locationInput.setHint("Enter Location");

        EditText websiteInput = new EditText(this);
        websiteInput.setHint("Enter Website");

        dateTimeButton = new Button(this);
        dateTimeButton.setText("Select Date and Time");
        dateTimeButton.setOnClickListener(v -> selectDateTime());

        // Layout to hold the inputs
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(locationInput);
        layout.addView(dateTimeButton);
        layout.addView(websiteInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Reservation")
                .setMessage("Please enter reservation details")
                .setView(layout)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String location = locationInput.getText().toString().trim();
                    String website = websiteInput.getText().toString().trim();

                    if (location.isEmpty() || website.isEmpty() || selectedDateTime == null) {
                        Toast.makeText(DiningActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    } else {
                        saveReservation(location, selectedDateTime, website);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void selectDateTime() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR, hourOfDay % 12);  // Convert to 12-hour format
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.AM_PM, hourOfDay >= 12 ? Calendar.PM : Calendar.AM);

                selectedDateTime = calendar.getTime();

                // Format and display selected date and time on the button
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
                dateTimeButton.setText(dateFormat.format(selectedDateTime));
            }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), false).show(); // Use 12-hour format
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveReservation(String location, Date dateTime, String website) {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("Dining").add(new Dining(location, dateTime, website, userId))
                .addOnSuccessListener(aVoid -> Toast.makeText(DiningActivity.this, "Reservation added successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(DiningActivity.this, "Error adding reservation", Toast.LENGTH_SHORT).show());
        Intent intent = new Intent(DiningActivity.this, DiningActivity.class);
        startActivity(intent);
    }
    private void loadReservations() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("Dining")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    reservationList.removeAllViews();  // Clear existing items

                    List<Dining> reservations = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Dining dining = doc.toObject(Dining.class);
                        reservations.add(dining);
                    }

                    Collections.sort(reservations, (a, b) -> a.getDateTime().compareTo(b.getDateTime()));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

                    for (Dining reservation : reservations) {
                        TextView reservationView = new TextView(this);
                        reservationView.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        reservationView.setPadding(0, 16, 0, 16);
                        reservationView.setText(String.format(
                                "Location: %s\nDate and Time: %s\nWebsite: %s",
                                reservation.getLocation(),
                                dateFormat.format(reservation.getDateTime()),
                                reservation.getWebsite()
                        ));
                        reservationList.addView(reservationView);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(DiningActivity.this, "Error loading reservations", Toast.LENGTH_SHORT).show());
    }
}
