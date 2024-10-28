package com.example.sprintproject;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.sprintproject.view.location.LocationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

@RunWith(AndroidJUnit4.class)
public class LocationActivityTest {

    @Rule
    public ActivityScenarioRule<LocationActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LocationActivity.class);

    @Test
    public void testCalculateVacationTime_withStartAndEndDate() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            String startDate = "2024-01-01";
            String endDate = "2024-01-10";
            // call the method directly to calculate duration
            int actualDuration = 0;
            try {
                actualDuration = LocationActivity.calculateDuration(startDate, endDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            int expectedDuration = 9;

            assertEquals(expectedDuration, actualDuration);
        });
    }
    @Test
    public void testCalculateVacationTime_withStartDateAndDuration() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            String startDate = "2024-01-01";
            int duration = 10;
            // call the method directly to calculate end date
            String actualEndDate = null;
            try {
                actualEndDate = LocationActivity.calculateEndDate(startDate, duration);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String expectedEndDate = "2024-01-11";

            assertEquals(expectedEndDate, actualEndDate);
        });
    }

    @Test
    public void testCalculateVacationTime_withEndDateAndDuration() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            String endDate = "2024-01-11";
            int duration = 10;
            // call the method directly to calculate end date
            String actualEndDate = null;
            try {
                actualEndDate = LocationActivity.calculateStartDate(endDate, duration);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String expectedEndDate = "2024-01-01";

            assertEquals(expectedEndDate, actualEndDate);
        });
    }
}
