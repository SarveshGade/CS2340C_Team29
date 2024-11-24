package com.example.sprintproject.view.logistics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Accomodation;
import com.example.sprintproject.model.AccomodationsObserver;
import com.example.sprintproject.model.Note;
import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;
import com.example.sprintproject.view.forum.ForumActivity;
import com.example.sprintproject.view.location.LocationActivity;
import com.example.sprintproject.viewmodel.LogisticsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.content.Intent;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private LogisticsViewModel logisticsViewModel;
    private LinearLayout noteList;
    private List<Note> notes = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_logistics);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pieChart = findViewById(R.id.pieChart);

        pieChart.setNoDataText(""); // Set no data text to empty to hide it
        pieChart.invalidate();

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        Button createGraphButton = findViewById(R.id.button_creategraph);
        createGraphButton.setOnClickListener((l) -> drawChart());

        Button inviteButton = findViewById(R.id.inviteButton);
        inviteButton.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder =
                    new android.app.AlertDialog.Builder(LogisticsActivity.this);
            builder.setTitle("Invite User");

            LinearLayout layout = new LinearLayout(LogisticsActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 40);

            final android.widget.EditText userEmailInput =
                    new android.widget.EditText(LogisticsActivity.this);
            userEmailInput.setHint("Enter Email of User to Invite");
            layout.addView(userEmailInput);
            builder.setView(layout);
            builder.setPositiveButton("Invite", (dialog, which) -> {
                String userEmail = userEmailInput.getText().toString().trim();
                //save userEmail as invited user in database
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
        noteList = findViewById(R.id.notesList);
        Button addNoteButton = findViewById(R.id.addNote);
        // Add click listener for the Add Note button
        addNoteButton.setOnClickListener(v -> showAddNoteDialog());

        // Load existing notes
        loadNotes();


        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, AccommodationsActivity.class);
                startActivity(intent);
            }
        });

        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogisticsActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }
    public void drawChart() {
        List<PieEntry> entries = new ArrayList<>();

        Integer totalAllocatedDays = logisticsViewModel.getTotalAllocatedDays().getValue();
        Integer totalUsedDays = logisticsViewModel.getTotalUsedDays().getValue();

        // Handle null values more safely
        if (totalAllocatedDays != null && totalAllocatedDays > 0) {
            entries.add(new PieEntry(totalAllocatedDays - totalUsedDays, "Remaining"));
        } else {
            entries.add(new PieEntry(0, "Remaining"));
        }

        if (totalUsedDays != null && totalUsedDays > 0) {
            entries.add(new PieEntry(totalUsedDays, "Planned"));
        } else {
            entries.add(new PieEntry(0, "Planned"));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        // Set colors for the chart slices
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        dataSet.setColors(colors);

        // Set text properties for values
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setDrawValues(true);

        // Set up PieChart
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setNoDataText("No data available");
        pieChart.setData(new PieData(dataSet));
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.TRANSPARENT);

        pieChart.invalidate(); // Refresh the chart
    }

    private void showAddNoteDialog() {
        EditText noteInput = new EditText(this);
        noteInput.setHint("Enter your note");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note")
                .setView(noteInput)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String noteText = noteInput.getText().toString().trim();
                    if (!noteText.isEmpty()) {
                        saveNote(noteText);
                    } else {
                        Toast.makeText(LogisticsActivity.this,
                                "Note cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void saveNote(String noteText) {
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";
        String userEmail = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getEmail() : "unknown_email";

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String tripId = userDoc.getString("tripID");
                    Note note = new Note(noteText,new Date(), tripId, userEmail);

                    db.collection("Notes").add(note)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(LogisticsActivity.this,
                                        "Note added successfully!", Toast.LENGTH_SHORT).show();
                                loadNotes(); // Reload notes after adding
                            })
                            .addOnFailureListener(e -> Toast.makeText(LogisticsActivity.this,
                                    "Error adding note", Toast.LENGTH_SHORT).show());
                });
    }

    private void loadNotes() {
        String userId = mAuth.getCurrentUser() != null
                ? mAuth.getCurrentUser().getUid() : "unknown_user";

        db.collection("Users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String tripId = userDoc.getString("tripID");
                    db.collection("Notes")
                            .whereEqualTo("tripId", tripId)
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                notes.clear();

                                for (QueryDocumentSnapshot doc : querySnapshot) {
                                    Note note = doc.toObject(Note.class);
                                    notes.add(note);
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(LogisticsActivity.this,
                                    "Error loading notes", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(LogisticsActivity.this,
                        "Error retrieving user trip ID", Toast.LENGTH_SHORT).show());
    }

    private void onNotesLoaded(List<Note> notes) {
        noteList.removeAllViews();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Date currentDate = new Date();

        for (Note note : notes) {
            TextView noteView = new TextView(this);
            noteView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            noteView.setPadding(0, 16, 0, 16);
            Date datePosted = note.getTimestamp();

            String checkInStr = datePosted != null ? dateFormat.format(datePosted) : "Invalid Date";
            noteView.setText(String.format(
                    "Timestamp: %s\nUsername: %s\n%s",
                    datePosted,
                    note.getUserEmail(),
                    note.getText()
            ));
            noteList.addView(noteView);
        }

    }
}