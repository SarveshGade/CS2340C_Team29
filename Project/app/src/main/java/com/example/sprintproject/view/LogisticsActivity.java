package com.example.sprintproject.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class LogisticsActivity extends AppCompatActivity {
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
        ImageButton addNotes = findViewById(R.id.notesButton);
        ImageButton addContributorButton = findViewById(R.id.addContributor);
        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);
        Button createGraphButton = findViewById(R.id.create_graph);
        createGraphButton.setOnClickListener((l) -> drawGraph());

        addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        addContributorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(view.getContext());
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Add Contributor");
                builder.setMessage("Please enter contributor's name:");

                // input
                builder.setView(input);
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        addContributor(userInput); // Call your method with user input
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
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
        FirebaseApp.initializeApp(this);
    }
    // load contributors whenever the activity starts
    protected void onStart() {
        super.onStart();
        loadContributors();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    // method that creates a new contributor to the list
    public void addContributor(String user) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userContributorsRef = databaseReference.child("users").child(userId).child("contributors");

        // check for duplicates
        userContributorsRef.orderByChild("name").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) { // If no existing contributor with that name
                    // new contributor in Firebase
                    String contributorId = userContributorsRef.push().getKey();
                    // contributor's name
                    userContributorsRef.child(contributorId).child("name").setValue(user);
                } else {
                    // Contributor already exists
                    Log.d("Contributors", "Contributor already exists: " + user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Contributors", "Error checking for duplicates: " + databaseError.getMessage());
            }
        });
    }

    public void drawGraph() {
        List<PieEntry> entries = new ArrayList<>();

        PieChart pieChart = findViewById(R.id.pieChart);
        TextView noDataText = findViewById(R.id.noDataText);

        if (entries.isEmpty()) {
            pieChart.setVisibility(View.GONE);
            noDataText.setVisibility(View.VISIBLE);
        } else {
            noDataText.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);

            PieDataSet dataSet = new PieDataSet(entries, "Pie Data");
            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
        }
    }

    private void loadContributors() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userContributorsRef = databaseReference.child("users").child(userId).child("contributors");
        userContributorsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LinearLayout layout = findViewById(R.id.rootLayout);
                layout.removeAllViews();
                // on lunch add contributors
                for (DataSnapshot contributorSnapshot : dataSnapshot.getChildren()) {
                    String contributorName = contributorSnapshot.child("name").getValue(String.class);
                    // new contributor
                    if (contributorName != null) {
                        Button contributorButton = new Button(LogisticsActivity.this);
                        contributorButton.setText(contributorName);
                        layout.addView(contributorButton);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // possible errors
                Log.e("LoadContributors", "Error loading contributors: " + databaseError.getMessage());
            }
        });
    }

}