package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.view.accomodations.AccommodationsActivity;
import com.example.sprintproject.view.dining.DiningActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class AccommodationTest {

    private AccommodationsActivity accommodationsActivity;

    @Before
    public void setUp() {
        ActivityScenario<AccommodationsActivity> scenario = ActivityScenario.launch(AccommodationsActivity.class);
        scenario.onActivity(activity -> accommodationsActivity = activity);
    }

    // validdate empty date/time refer to the validateReservationInput method in
    // the DiningActivity or check they are are all valid
    @Test
    public void testValidateEmptyCheckInDate() {
        String result = accommodationsActivity.validateReservationInput("Test Location", null, new Date(), 3);
        assertEquals("Check in date must be selected", result);
    }

    @Test
    public void testValidateValidInput() {
        String result = accommodationsActivity.validateReservationInput("Test Location", new Date(), new Date(), -4);
        assertEquals("Number of rooms cannot be less than 0!", result);
    }
}