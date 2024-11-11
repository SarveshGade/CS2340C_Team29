package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.view.accomodations.AccommodationsActivity;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)

public class AccomodationTest {
    private AccommodationsActivity accommodationsActivity;

    @Before
    public void setUp() {
        ActivityScenario<AccommodationsActivity> scenario = ActivityScenario.launch(AccommodationsActivity.class);
        scenario.onActivity(activity -> accommodationsActivity = activity);
    }

    @Test
    public void testValidateEmptyLocation() {
        String result = accommodationsActivity.validateReservationInput("", new Date(), "www.example.com");
        assertEquals("Location cannot be empty", result);
    }
    @Test
    public void testValidateEmptyDateTime() {
        String result = accommodationsActivity.validateReservationInput("Test Location", null, "www.example.com");
        assertEquals("Date and time must be selected", result);
    }


}
