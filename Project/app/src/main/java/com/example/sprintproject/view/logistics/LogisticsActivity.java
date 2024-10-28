package com.example.sprintproject.view.logistics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
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

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


public class LogisticsActivity extends AppCompatActivity {

    private PieChart pieChart;
    private LogisticsViewModel logisticsViewModel;

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
            entries.add(new PieEntry(totalAllocatedDays, "Allotted"));
        } else {
            entries.add(new PieEntry(0, "Allotted")); // Optional: add a 0 entry
        }

        if (totalUsedDays != null && totalUsedDays > 0) {
            entries.add(new PieEntry(totalUsedDays, "Planned"));
        } else {
            entries.add(new PieEntry(0, "Planned")); // Optional: add a 0 entry
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        // Set colors for the chart slices
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567")); // Custom color 1
        colors.add(Color.parseColor("#309967")); // Custom color 2
        dataSet.setColors(colors);

        // Set text properties for values
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE); // Set labels color to black

        // Set up PieChart
        PieChart pieChart = findViewById(R.id.pieChart);
        pieChart.setNoDataText("No data available");
        pieChart.setData(new PieData(dataSet));
        pieChart.getDescription().setEnabled(false); // Hide description label
        pieChart.setEntryLabelColor(Color.WHITE);

        pieChart.invalidate(); // Refresh the chart
    }


}