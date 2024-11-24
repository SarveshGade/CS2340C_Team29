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

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private LogisticsViewModel logisticsViewModel;
    private LinearLayout noteList;
    private List<Note> notes = new ArrayList<>();

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

        logisticsViewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);

        pieChart = findViewById(R.id.pieChart);
        pieChart.setNoDataText("");
        pieChart.invalidate();

        Button createGraphButton = findViewById(R.id.button_creategraph);
        createGraphButton.setOnClickListener(v -> drawChart());

        Button inviteButton = findViewById(R.id.inviteButton);
        inviteButton.setOnClickListener(v -> showInviteDialog());

        noteList = findViewById(R.id.notesList);
        Button addNoteButton = findViewById(R.id.addNote);
        addNoteButton.setOnClickListener(v -> showAddNoteDialog());

        setupNavigationButtons();
        logisticsViewModel.getNotes().observe(this, this::onNotesLoaded);
        logisticsViewModel.getInviteStatus().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "User successfully invited", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to invite user", Toast.LENGTH_SHORT).show();
            }
        });

        // Load existing notes
        logisticsViewModel.getTripID().observe(this, tripID -> {
            if (tripID != null && !tripID.isEmpty()) {
                logisticsViewModel.loadNotes();
            }
        });
    }

    public void drawChart() {
        List<PieEntry> entries = new ArrayList<>();

        Integer totalAllocatedDays = logisticsViewModel.getTotalAllocatedDays().getValue();
        Integer totalUsedDays = logisticsViewModel.getTotalUsedDays().getValue();

        // Handle null values more safely
        if (totalUsedDays != null && totalUsedDays > 0) {
            entries.add(new PieEntry(totalUsedDays, "Planned"));
        } else {
            entries.add(new PieEntry(0, "Planned"));
        }

        if (totalAllocatedDays != null && totalAllocatedDays > 0) {
            entries.add(new PieEntry(totalAllocatedDays - totalUsedDays, "Remaining"));
        } else {
            entries.add(new PieEntry(0, "Remaining"));
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

    private void showInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invite User");

        EditText userEmailInput = new EditText(this);
        userEmailInput.setHint("Enter email of the user to invite");

        builder.setView(userEmailInput);
        builder.setPositiveButton("Invite", (dialog, which) -> {
            String email = userEmailInput.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }
            logisticsViewModel.inviteUser(email);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
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
                        logisticsViewModel.addNote(noteText);
                    } else {
                        Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void setupNavigationButtons() {
        findViewById(R.id.logisticsButton).setOnClickListener(v ->
                startActivity(new Intent(this, LogisticsActivity.class)));
        findViewById(R.id.locationButton).setOnClickListener(v ->
                startActivity(new Intent(this, LocationActivity.class)));
        findViewById(R.id.diningButton).setOnClickListener(v ->
                startActivity(new Intent(this, DiningActivity.class)));
        findViewById(R.id.accommodationsButton).setOnClickListener(v ->
                startActivity(new Intent(this, AccommodationsActivity.class)));
        findViewById(R.id.forumButton).setOnClickListener(v ->
                startActivity(new Intent(this, ForumActivity.class)));
    }

    private void onNotesLoaded(List<Note> notes) {
        noteList.removeAllViews();
        for (Note note : notes) {
            TextView noteView = new TextView(this);
            noteView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            noteView.setPadding(0, 16, 0, 16);
            noteView.setText(String.format("Timestamp: %s\nUser email: %s\nNote: %s",
                    note.getTimestamp(), note.getUserEmail(), note.getText()));
            noteList.addView(noteView);
        }
    }
}
