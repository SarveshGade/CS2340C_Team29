package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.view.dining.DiningActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class DiningTest {

    private DiningActivity diningActivity;

    @Before
    public void setUp() {
        ActivityScenario<DiningActivity> scenario = ActivityScenario.launch(DiningActivity.class);
        scenario.onActivity(activity -> diningActivity = activity);
    }

    @Test
    public void testValidateEmptyLocation() {
        String result = diningActivity.validateReservationInput("", new Date(), "www.example.com");
        assertEquals("Location cannot be empty", result);
    }
    @Test
    public void testValidateEmptyWebsite() {
        String result = diningActivity.validateReservationInput("Test Location", new Date(), "");
        assertEquals("Website cannot be empty", result);
    }
    // validdate empty date/time refer to the validateReservationInput method in
    // the DiningActivity or check they are are all valid
}
