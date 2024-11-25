package com.example.sprintproject.view.forum;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accomodation;
import com.example.sprintproject.model.ForumObserver;
import com.example.sprintproject.model.TravelCommunity;
import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.viewmodel.ForumViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForumActivity extends AppCompatActivity implements ForumObserver {
    private ForumViewModel viewModel;
    private LinearLayout forumsList;
    private Button checkInButton;
    private Button checkOutButton;
    private Date selectedCheckIn;
    private Date selectedCheckOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        forumsList = findViewById(R.id.forumsList);
        Button sortCheckInButton = findViewById(R.id.sortCheckInButton);
        Button sortCheckOutButton = findViewById(R.id.sortCheckOutButton);
        Button addTripButton = findViewById(R.id.addTripButton);

        viewModel = new ViewModelProvider(this).get(ForumViewModel.class);
        viewModel.addObserver(this);


        viewModel.getForums().observe(this, forums -> {
            forumsList.removeAllViews();
            if (forums != null) {
                onForumsLoaded(forums);
            } else {
                Toast.makeText(this, "Error loading posts", Toast.LENGTH_SHORT).show();
            }
        });

        sortCheckInButton.setOnClickListener(v -> {
            viewModel.setSortByCheckIn(true);
        });

        sortCheckOutButton.setOnClickListener(v -> {
            viewModel.setSortByCheckIn(false);
        });

        addTripButton.setOnClickListener(v -> showAddTripDialog());

        viewModel.loadForums();

        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);

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
    public void onForumsLoaded(List<TravelCommunity> updatedForums) {
        forumsList.removeAllViews();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Date currentDate = new Date(); // Current date for comparison

        for (TravelCommunity post : updatedForums) {
            TextView forumView = new TextView(this);
            forumView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            forumView.setPadding(0, 16, 0, 16);
            Date checkIn = post.getStartDate();
            Date checkOut = post.getEndDate();


            int duration = calculateDuration(checkIn, checkOut);
            forumView.setText(String.format(
                    "Username: %s\nDestination: %s\nAccommodations: %s\n"
                            + "Dining: %s\nNotes: %s\nDuration: ",
                    post.getUsername(),
                    post.getDestination(),
                    post.getAccommodation(),
                    post.getDining(),
                    post.getNotes(),
                    duration
            ));
            forumsList.addView(forumView);
        }
    }

    private void showAddTripDialog() {
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        scrollView.addView(layout);

        checkInButton = new Button(this);
        checkInButton.setText(R.string.select_check_in_date);
        checkInButton.setOnClickListener(v -> selectCheckInDate());
        layout.addView(checkInButton);

        checkOutButton = new Button(this);
        checkOutButton.setText(R.string.select_check_out_date);
        checkOutButton.setOnClickListener(v -> selectCheckOutDate());
        layout.addView(checkOutButton);

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
            String destination = etDestination.getText().toString().trim();
            String accommodations = etAccommodations.getText().toString().trim();
            String dining = etDining.getText().toString().trim();
            String notes = etNotes.getText().toString().trim();

            String validationError = viewModel.validateTripInput(selectedCheckIn, selectedCheckOut,
                    destination, accommodations, dining);
            if (validationError != null) {
                Toast.makeText(this, validationError, Toast.LENGTH_SHORT).show();
            } else {
                viewModel.savePost(selectedCheckIn, selectedCheckOut, destination,
                        accommodations, dining, notes);
                Toast.makeText(this, "Trip added successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void selectCheckInDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedCheckIn = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            checkInButton.setText("Check-in: "
                    + dateFormat.format(selectedCheckIn));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void selectCheckOutDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedCheckOut = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MMM dd, yyyy", Locale.getDefault());
            checkOutButton.setText("Check-out: " + dateFormat.format(selectedCheckOut));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return date != null ? dateFormat.format(date) : "Select Date";
    }

    public static int calculateDuration(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        if (endDate.before(startDate)) {
            return -1; // Return -1 for invalid date ranges
        }
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24)); // Convert to days
    }
}