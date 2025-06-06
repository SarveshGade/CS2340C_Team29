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

    @Test
    public void testValidateEmptyCheckInDate() {
        String result = accommodationsActivity.validateReservationInput("Test Location", null, new Date(), 3, "Standard");
        assertEquals("Check in date must be selected", result);
    }

    @Test
    public void testValidateValidInput() {
        String result = accommodationsActivity.validateReservationInput("Test Location", new Date(), new Date(), -4, "Standard");
        assertEquals("Number of rooms cannot be less than 0!", result);
    }

    @Test
    public void testValidateEmptyCheckOutDate() {
        String result = accommodationsActivity.validateReservationInput("Test Location", new Date(), null, 3, "Standard");
        assertEquals("Check out date must be selected", result);
    }

    @Test
    public void testValidateEmptyLocation() {
        String result = accommodationsActivity.validateReservationInput("", new Date(), new Date(), 2, "Standard");
        assertEquals("Location cannot be empty", result);
    }

    @Test
    public void testValidateNullLocation() {
        String result = accommodationsActivity.validateReservationInput(null, new Date(), new Date(), 1, "Standard");
        assertEquals("Location cannot be empty", result);
    }

    @Test
    public void testValidateEmptyRoomType() {
        String result = accommodationsActivity.validateReservationInput("Test Location", new Date(), new Date(), 1, "");
        assertEquals("Room type cannot be empty", result);
    }

    @Test
    public void testValidateNullRoomType() {
        String result = accommodationsActivity.validateReservationInput("Test Location", new Date(), new Date(), 1, null);
        assertEquals("Room type cannot be empty", result);
    }


}