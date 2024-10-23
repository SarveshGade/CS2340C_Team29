package com.example.sprintproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Destination {
    private String location;
    private String startDate;
    private String endDate;

    public Destination(String location, String startDate, String endDate) {
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getVacationDuration() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust format as needed
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            // ensure that the end date is after the start date
            if (end.before(start)) {
                return 0;
            }

            long durationInMillis = end.getTime() - start.getTime();
            return (int) (durationInMillis / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
