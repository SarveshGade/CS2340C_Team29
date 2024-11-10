package com.example.sprintproject.view.accomodations;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accomodation;
import com.example.sprintproject.view.dining.DiningActivity;
import com.example.sprintproject.view.forum.ForumActivity;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Calendar;

public class AccommodationsActivity extends AppCompatActivity {
    private EditText location;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private EditText checkIn;
    private EditText checkOut;
    private EditText numRooms;
    private EditText roomType;
    private Button addAccomodation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodations);

        checkIn = findViewById(R.id.checkInEditText);
        checkOut = findViewById(R.id.checkOutEditText);
        location = findViewById(R.id.location);
        numRooms = findViewById(R.id.numberOfRooms);
        roomType = findViewById(R.id.roomTypes);
        addAccomodation = findViewById(R.id.addAccommodationButton);


        checkIn.setOnClickListener(v -> showDatePickerDialog(checkIn));
        checkOut.setOnClickListener(v -> showDatePickerDialog(checkOut));

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

    private void resetFormFields() {
        location.setText("");
        checkIn.setText("");
        checkOut.setText("");
        numRooms.setText("");
        roomType.setSelection(0); // Reset spinner to the first item
    }

    private void showDatePickerDialog(EditText dateEditText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    dateEditText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

}