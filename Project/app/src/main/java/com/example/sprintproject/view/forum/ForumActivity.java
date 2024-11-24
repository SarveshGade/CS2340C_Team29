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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;

import com.example.sprintproject.model.ForumObserver;
import com.example.sprintproject.model.TravelCommunity;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;
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

public class ForumActivity extends AppCompatActivity implements ForumObserver {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Date selectedCheckIn;
    private Date selectedCheckOut;
    private Button checkInButton;
    private Button checkOutButton;
    private LinearLayout forumsList;
    private List<ForumObserver> observers = new ArrayList<>();
    private List<TravelCommunity> forums = new ArrayList<>();
    private Button sortCheckInButton;
    private Button sortCheckOutButton;
    private boolean sortByCheckIn = true;



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


        addObserver(this);
        forumsList = findViewById(R.id.forumsList);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);
        Button addTripButton = findViewById(R.id.addTripButton);
        sortCheckInButton = findViewById(R.id.sortCheckInButton);
        sortCheckOutButton = findViewById(R.id.sortCheckOutButton);

        sortCheckInButton.setOnClickListener(v -> {
            sortByCheckIn = true;
            sortPosts();
        });

        sortCheckOutButton.setOnClickListener(v -> {
            sortByCheckIn = false;
            sortPosts();
        });

        addTripButton.setOnClickListener(v -> showAddTripDialog());
        loadforums();

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
        ScrollView scrollView = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(16, 16, 16, 16);
        scrollView.addView(layout);



        checkInButton = new Button(this);
        checkInButton.setText("Select Check-in Date");
        checkInButton.setOnClickListener(v -> selectCheckInDate());

        checkOutButton = new Button(this);
        checkOutButton.setText("Select Check-out Date");
        checkOutButton.setOnClickListener(v -> selectCheckOutDate());
        layout.addView(checkInButton);
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

            if (destination.isEmpty() || accommodations.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            } else {
                savePost(selectedCheckIn, selectedCheckOut, destination,
                        accommodations, dining, notes);
            }

            Toast.makeText(this, "Trip added successfully!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    private void selectCheckInDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedCheckIn = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            checkInButton.setText("Check-in: " + dateFormat.format(selectedCheckIn));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void selectCheckOutDate() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedCheckOut = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            checkOutButton.setText("Check-out: " + dateFormat.format(selectedCheckOut));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void sortPosts() {
        Collections.sort(forums, (a, b) -> {
            if (sortByCheckIn) {
                return a.getStartDate().compareTo(b.getStartDate());
            } else {
                return a.getEndDate().compareTo(b.getEndDate());
            }
        });
        notifyObservers(forums);
    }

    private void savePost(Date start, Date end, String destination, String accommodations,
                          String dining, String notes) {
        if (start == null || end == null || start.before(new Date()) || end.before(new Date()) || end.before(start)) {
            Toast.makeText(this, "Please select valid dates for your trip!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";
        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String tripID = userDoc.getString("tripID");
                    String username = userDoc.getString("email");
                    db.collection("TravelCommunity").add(new TravelCommunity(accommodations,
                                    destination, dining, notes, tripID, start, end, username))
                            .addOnSuccessListener(aVoid -> Toast.makeText(
                                    ForumActivity.this,
                                    "Post added successfully!",
                                    Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(ForumActivity.this,
                                    "Error adding post", Toast.LENGTH_SHORT).show());
                });


        Intent intent = new Intent(ForumActivity.this, ForumActivity.class);
        startActivity(intent);
    }

    private void loadforums() {
        db.collection("TravelCommunity")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    forums.clear();

                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        TravelCommunity post = doc.toObject(TravelCommunity.class);
                        forums.add(post);
                    }
                    sortPosts();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(ForumActivity.this, "Error loading posts", Toast.LENGTH_SHORT).show()
                );
    }

    public void onForumsLoaded(List<TravelCommunity> posts) {
        forumsList.removeAllViews();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

        for (TravelCommunity post : posts) {
            TextView forumView = new TextView(this);
            forumView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            forumView.setPadding(0, 16, 0, 16);
            Date checkIn = post.getStartDate();
            Date checkOut = post.getEndDate();

            String checkInStr = checkIn != null ? dateFormat.format(checkIn) : "Invalid Date";
            String checkOutStr = checkOut != null ? dateFormat.format(checkOut) : "Invalid Date";
            int duration = calculateDuration(checkIn, checkOut);
            forumView.setText(String.format(
                    "Username: %s\nDestination: %s\nAccomodations: %s\nDining: %s\n"
                            + "Notes: %s\nDuration: %d",
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
    public static int calculateDuration(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return -1;
        }
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24)); // Convert to days
    }
    public void addObserver(ForumObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ForumObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(List<TravelCommunity> forums) {
        for (ForumObserver observer : observers) {
            observer.onForumsLoaded(forums);
        }
    }

}