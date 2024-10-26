package com.example.sprintproject.view.location;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.viewmodel.LogTravelViewModel;

import java.util.Calendar;

public class LogTravelActivity extends AppCompatActivity {
    private EditText locationEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button saveButton;
    private LogTravelViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_travel);

        viewModel = new LogTravelViewModel();
        locationEditText = findViewById(R.id.locationEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        saveButton = findViewById(R.id.saveButton);

        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        saveButton.setOnClickListener(v -> saveTravelLog());

        // Observe the calculation result
        viewModel.getDestination().observe(this, destination -> {
            if (destination != null) {
                // Handle successful save
                Toast.makeText(this, "Travel log saved: " + destination.getLocation(), Toast.LENGTH_SHORT).show();
                finish(); // Close the activity
            }
        });

        // Observe the error message LiveData
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void saveTravelLog() {
        String location = locationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();

        viewModel.saveDestination(location, startDate, endDate);
    }
}