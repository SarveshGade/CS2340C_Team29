package com.example.sprintproject.view.accomodations;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.sprintproject.model.Accomodation;
import com.example.sprintproject.model.Reservable;
import com.example.sprintproject.model.ReservationsObserver;
import com.example.sprintproject.view.dining.DiningActivity;
import com.example.sprintproject.view.forum.ForumActivity;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.view.logistics.LogisticsActivity;
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

public class AccommodationsActivity extends AppCompatActivity implements ReservationsObserver {
    private EditText location;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText checkIn;
    private EditText checkOut;
    private EditText numRooms;
    private EditText roomType;
    private Button addAccomodation;
    private LinearLayout reservationList;
    private List<ReservationsObserver> observers = new ArrayList<>();
    private Date checkInDate;
    private Date checkOutDate;
    private Button checkInButton;
    private Button checkOutButton;
    private List<Accomodation> reservations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodations);

        addObserver(this);
        reservationList = findViewById(R.id.reservationList);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addAccomodation.setOnClickListener(v -> addAccomodations());


        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);


        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this,
                        AccommodationsActivity.class);
                startActivity(intent);
            }
        });

        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationsActivity.this,
                        ForumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addAccomodations() {
        String locations = location.getText().toString();
        String checkInDate = checkIn.getText().toString();
        String checkOutDate = checkOut.getText().toString();
        int numberOfRooms = Integer.parseInt(numRooms.getText().toString());
        String roomTypes = roomType.getText().toString();

        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("accommodation").add(new Accomodation(locations, checkInDate, checkOutDate, numberOfRooms, roomTypes, userId))
                .addOnSuccessListener(aVoid -> Toast.makeText(AccommodationsActivity.this, "Reservation added successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AccommodationsActivity.this, "Error adding reservation", Toast.LENGTH_SHORT).show());
        Intent intent = new Intent(AccommodationsActivity.this, AccommodationsActivity.class);
        startActivity(intent);
    }

    private void showReservationDialog() {
        EditText locationInput = new EditText(this);
        locationInput.setHint("Enter Location");

        EditText roomTypeInput = new EditText(this);
        roomTypeInput.setHint("Enter Room Type");

        EditText numRoomsInput = new EditText(this);
        numRoomsInput.setHint("Enter Number of Rooms");
        numRoomsInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        checkInButton = new Button(this);
        checkInButton.setText("Select Check In Date");
        checkInButton.setOnClickListener(v -> selectDate(true));  // For check-in date

        checkOutButton = new Button(this);
        checkOutButton.setText("Select Check Out Date");
        checkOutButton.setOnClickListener(v -> selectDate(false));  // For check-out date

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(locationInput);
        layout.addView(checkInButton);
        layout.addView(checkOutButton);
        layout.addView(numRoomsInput);
        layout.addView(roomTypeInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Reservation")
                .setMessage("Please enter reservation details")
                .setView(layout)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String location = locationInput.getText().toString().trim();
                    String roomType = roomTypeInput.getText().toString().trim();
                    String numRoomsStr = numRoomsInput.getText().toString().trim();
                    int numRooms = numRoomsStr.isEmpty() ? 0 : Integer.parseInt(numRoomsStr);

                    if (location.isEmpty() || roomType.isEmpty() || checkInDate == null || checkOutDate == null) {
                        Toast.makeText(AccommodationsActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    } else {
                        saveReservation(location, checkInDate, checkOutDate, numRooms, roomType);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void selectDate(boolean isCheckIn) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

            if (isCheckIn) {
                checkInDate = calendar.getTime();
                checkInButton.setText(dateFormat.format(checkInDate));
            } else {
                checkOutDate = calendar.getTime();
                checkOutButton.setText(dateFormat.format(checkOutDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void saveReservation(String location, Date checkInDate, Date checkOutDate, int numRooms, String roomType) {
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("accomodation").add(new Accomodation(location, checkInDate, checkOutDate, numRooms, roomType, userId))
                .addOnSuccessListener(aVoid -> Toast.makeText(AccommodationsActivity.this,
                        "Reservation added successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AccommodationsActivity.this,
                        "Error adding reservation", Toast.LENGTH_SHORT).show());
        Intent intent = new Intent(AccommodationsActivity.this, AccommodationsActivity.class);
        startActivity(intent);
    }


    private void loadReservations() {
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("accomodation")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    reservations.clear();  // Clear existing items

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Accomodation accomodation = doc.toObject(Accomodation.class);
                        reservations.add(accomodation);
                    }

                    Collections.sort(reservations,
                            (a, b) -> a.getCheckInDate().compareTo(b.getCheckInDate()));

                    // Notify observers that data has been updated
                    notifyObservers(reservations);
                })
                .addOnFailureListener(e -> Toast.makeText(AccommodationsActivity.this,
                        "Error loading reservations", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onReservationsLoaded(List reservations) {
        reservationList.removeAllViews();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MMM dd, yyyy hh:mm a", Locale.getDefault());

        for (Accomodation reservation : reservations) {
            TextView reservationView = new TextView(this);
            reservationView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            reservationView.setPadding(0, 16, 0, 16);
            reservationView.setText(String.format(
                    "Location: %s\nCheck In Date: %s\nCheck Out Date: %s\nNumber of Rooms: %d\nRoom Type: %s",
                    reservation.getLocation(),
                    dateFormat.format(reservation.getCheckInDate()),
                    dateFormat.format(reservation.getCheckOutDate()),
                    reservation.getNumRooms(),
                    reservation.getRoomType()
            ));
            reservationList.addView(reservationView);
        }
    }

    public void addObserver(ReservationsObserver observer) {
        observers.add(observer);
    }

    // Unregister an observer
    public void removeObserver(ReservationsObserver observer) {
        observers.remove(observer);
    }

    // Notify all observers
    private void notifyObservers(List<Accomodation> reservations) {
        for (ReservationsObserver observer : observers) {
            observer.onReservationsLoaded(reservations);
        }
    }
}