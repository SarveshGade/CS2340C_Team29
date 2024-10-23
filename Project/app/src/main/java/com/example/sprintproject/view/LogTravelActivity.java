package com.example.sprintproject.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Destination;

import java.util.Calendar;

public class LogTravelActivity extends AppCompatActivity {
    private EditText locationEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button saveButton;
    private Button calculateMissingFieldButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_travel);

        locationEditText = findViewById(R.id.locationEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        saveButton = findViewById(R.id.saveButton);

        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        saveButton.setOnClickListener(v -> saveTravelLog());
    }

    private void showDatePickerDialog(EditText dateEditText) {
        // current date
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

        // minimum date to today's date
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void calculateMissingField() {
        String startDateStr = startDateEditText.getText().toString();
        String endDateStr = endDateEditText.getText().toString();

        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            int[] startDate = extractDate(startDateStr);
            int[] endDate = extractDate(endDateStr);

            if (startDate == null || endDate == null) {
                Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            int startDays = startDate[0] * 365 + startDate[1] * 30 + startDate[2];
            int endDays = endDate[0] * 365 + endDate[1] * 30 + endDate[2];
            int duration = endDays - startDays;

            if (duration < 0) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Duration: " + duration + " days", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Please fill in both start and end dates to calculate", Toast.LENGTH_SHORT).show();
        }
    }

    private int[] extractDate(String dateStr) {
        try {
            String[] dateParts = dateStr.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]);
            int day = Integer.parseInt(dateParts[2]);
            return new int[]{year, month, day};
        } catch (Exception e) {
            return null;
        }
    }

    private void saveTravelLog() {
        String location = locationEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();

        if (location.isEmpty()) {
            location = "Destination";
        }
        Destination destination = new Destination(location, startDate, endDate);

        if (destination.getVacationDuration() <= 0 || destination.getStartDate() == null || destination.getEndDate() == null) {
            Toast.makeText(this, "Please enter valid start and end dates.", Toast.LENGTH_SHORT).show();
            return;
        }

        // save the destination to the database here?

        Toast.makeText(this, "Travel log saved!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
