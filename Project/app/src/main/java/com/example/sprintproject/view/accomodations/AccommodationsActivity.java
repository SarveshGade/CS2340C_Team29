package com.example.sprintproject.view.accomodations;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accomodation;
import com.example.sprintproject.model.AccomodationsObserver;
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

public class AccommodationsActivity extends AppCompatActivity implements AccomodationsObserver {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Date selectedCheckIn;
    private Date selectedCheckOut;
    private Button checkInButton;
    private Button checkOutButton;
    private LinearLayout accommodationsList;
    private List<AccomodationsObserver> observers = new ArrayList<>();
    private List<Accomodation> accommodations = new ArrayList<>();
    private Button sortCheckInButton;
    private Button sortCheckOutButton;
    private boolean sortByCheckIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        addObserver(this);
        accommodationsList = findViewById(R.id.accommodationList);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);
        Button addAccommodationButton = findViewById(R.id.addAccommodation);
        sortCheckInButton = findViewById(R.id.sortCheckInButton);
        sortCheckOutButton = findViewById(R.id.sortCheckOutButton);

        sortCheckInButton.setOnClickListener(v -> {
            sortByCheckIn = true;
            sortAccommodations();
        });

        sortCheckOutButton.setOnClickListener(v -> {
            sortByCheckIn = false;
            sortAccommodations();
        });
        addAccommodationButton.setOnClickListener(v -> showAccommodationDialog());
        loadAccommodations();

        // Navigation button click listeners
        logisticsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationsActivity.this, LogisticsActivity.class);
            startActivity(intent);
        });
        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationsActivity.this, LocationActivity.class);
            startActivity(intent);
        });
        diningButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationsActivity.this, DiningActivity.class);
            startActivity(intent);
        });
        accommodationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationsActivity.this, AccommodationsActivity.class);
            startActivity(intent);
        });
        forumButton.setOnClickListener(v -> {
            Intent intent = new Intent(AccommodationsActivity.this, ForumActivity.class);
            startActivity(intent);
        });
    }

    private void sortAccommodations() {
        Collections.sort(accommodations, (a, b) -> {
            if (sortByCheckIn) {
                return a.getCheckInDate().compareTo(b.getCheckInDate());
            } else {
                return a.getCheckOutDate().compareTo(b.getCheckOutDate());
            }
        });
        notifyObservers(accommodations);
    }

    private void showAccommodationDialog() {
        EditText locationInput = new EditText(this);
        locationInput.setHint("Enter Location");

        EditText numRoomsInput = new EditText(this);
        numRoomsInput.setHint("Number of Rooms");
        numRoomsInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        // Create room type spinner
        Spinner roomTypeSpinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Single", "Double", "Suite", "Deluxe"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(adapter);

        checkInButton = new Button(this);
        checkInButton.setText("Select Check-in Date");
        checkInButton.setOnClickListener(v -> selectCheckInDate());

        checkOutButton = new Button(this);
        checkOutButton.setText("Select Check-out Date");
        checkOutButton.setOnClickListener(v -> selectCheckOutDate());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(locationInput);
        layout.addView(checkInButton);
        layout.addView(checkOutButton);
        layout.addView(numRoomsInput);
        layout.addView(roomTypeSpinner);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Accommodation")
                .setMessage("Please enter accommodation details")
                .setView(layout)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String location = locationInput.getText().toString().trim();
                    String numRoomsStr = numRoomsInput.getText().toString().trim();
                    String roomType = roomTypeSpinner.getSelectedItem().toString();

                    if (location.isEmpty() || numRoomsStr.isEmpty()
                            || selectedCheckIn == null || selectedCheckOut == null) {
                        Toast.makeText(AccommodationsActivity.this,
                                "All fields are required", Toast.LENGTH_SHORT).show();
                    } else {
                        int numRooms = Integer.parseInt(numRoomsStr);
                        saveAccommodation(location, selectedCheckIn, selectedCheckOut,
                                numRooms, roomType);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void selectCheckInDate() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedCheckIn = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            checkInButton.setText("Check-in: " + dateFormat.format(selectedCheckIn));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void selectCheckOutDate() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedCheckOut = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            checkOutButton.setText("Check-out: " + dateFormat.format(selectedCheckOut));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveAccommodation(String location, Date checkIn, Date checkOut,
                                        int numRooms, String roomType) {
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String tripID = userDoc.getString("tripID");
                    db.collection("accommodation").add(new Accomodation(location,
                                    checkIn, checkOut, numRooms, roomType, tripID))
                            .addOnSuccessListener(aVoid -> Toast.makeText(
                                    AccommodationsActivity.this,
                                    "Accommodation added successfully!",
                                    Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(AccommodationsActivity.this,
                                    "Error adding accommodation", Toast.LENGTH_SHORT).show());
                });
        Intent intent = new Intent(AccommodationsActivity.this, AccommodationsActivity.class);
        startActivity(intent);
    }

    private void loadAccommodations() {
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid()
                : "unknown_user";
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String tripID = userDoc.getString("tripID");

                    db.collection("accommodation")
                            .whereEqualTo("tripID", tripID)
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                accommodations.clear();

                                for (QueryDocumentSnapshot doc : querySnapshot) {
                                    Accomodation accommodation = doc.toObject(Accomodation.class);
                                    accommodations.add(accommodation);
                                }

                                sortAccommodations();
                            })
                            .addOnFailureListener(e -> Toast.makeText(AccommodationsActivity.this,
                                    "Error loading accommodations", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(AccommodationsActivity.this,
                                "Error retrieving user trip ID", Toast.LENGTH_SHORT).show());

    }

    public void onAccomodationsLoaded(List<Accomodation> accommodations) {
        accommodationsList.removeAllViews();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Date currentDate = new Date(); // Current date for comparison

        for (Accomodation accommodation : accommodations) {
            TextView accommodationView = new TextView(this);
            accommodationView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            accommodationView.setPadding(0, 16, 0, 16);
            Date checkIn = accommodation.getCheckInDate();
            Date checkOut = accommodation.getCheckOutDate();

            String checkInStr = checkIn != null ? dateFormat.format(checkIn) : "Invalid Date";
            String checkOutStr = checkOut != null ? dateFormat.format(checkOut) : "Invalid Date";

            // Determine if the reservation is past or upcoming
            String status = (checkIn != null && checkIn.before(currentDate)) ? "Status: Past"
                    : "Status: Upcoming";

            accommodationView.setText(String.format(
                    "Location: %s\nCheck-in: %s\nCheck-out: %s\n"
                            + "Number of Rooms: %s\nRoom Type: %s\n%s",
                    accommodation.getLocation(),
                    checkInStr,
                    checkOutStr,
                    accommodation.getNumRooms(),
                    accommodation.getRoomType(),
                    status
            ));
            accommodationsList.addView(accommodationView);
        }
    }

    public void addObserver(AccomodationsObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AccomodationsObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(List<Accomodation> accommodations) {
        for (AccomodationsObserver observer : observers) {
            observer.onAccomodationsLoaded(accommodations);
        }
    }

    // used to test code
    public String validateReservationInput(String location, Date checkInDate,
                                           Date checkOutDate, int numRooms) {
        if (location == null || location.isEmpty()) {
            return "Location cannot be empty";
        }
        if (checkInDate == null) {
            return "Check in date must be selected";
        }
        if (checkOutDate == null) {
            return "Check out date must be selected";
        }
        if (numRooms < 0) {
            return "Number of rooms cannot be less than 0!";
        }
        return null;
    }
}