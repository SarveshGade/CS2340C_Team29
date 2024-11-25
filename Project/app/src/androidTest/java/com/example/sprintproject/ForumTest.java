package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.view.forum.ForumActivity;
import com.example.sprintproject.viewmodel.ForumViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class ForumTest {

    private ForumActivity forumActivity;
    ForumViewModel forumViewModel;

    @Before
    public void setUp() {
        ActivityScenario<ForumActivity> scenario = ActivityScenario.launch(ForumActivity.class);
        scenario.onActivity(activity -> forumActivity = activity);
        forumViewModel = new ForumViewModel();

    }


    @Test
    public void testCalculateDurationValidDates() {
        Date startDate = new Date(2024 - 1900, 11, 23);
        Date endDate = new Date(2024 - 1900, 11, 28);
        int duration = ForumActivity.calculateDuration(startDate, endDate);
        assertEquals(5, duration);
    }

    @Test
    public void testCalculateDurationWithNullDates() {
        int duration = ForumActivity.calculateDuration(null, new Date());
        assertEquals(-1, duration);

        duration = ForumActivity.calculateDuration(new Date(), null);
        assertEquals(-1, duration);
    }

    @Test
    public void testCalculateDurationEndDateBeforeStartDate() {
        Date startDate = new Date(2024 - 1900, 11, 28); // Dec 28, 2024
        Date endDate = new Date(2024 - 1900, 11, 23); // Dec 23, 2024
        int duration = ForumActivity.calculateDuration(startDate, endDate);
        assertEquals(-1, duration);
    }

    @Test
    public void testCalculateDurationBothDatesNull() {
        int duration = ForumActivity.calculateDuration(null, null);
        assertEquals(-1, duration);
    }

    @Test
    public void testCalculateDurationBothDatesValid() {
        Date startDate = new Date(2024 - 1900, 11, 20); // Dec 20, 2024
        Date endDate = new Date(2024 - 1900, 11, 25);   // Dec 25, 2024
        int duration = ForumActivity.calculateDuration(startDate, endDate);
        assertEquals(5, duration); // Expected duration is 5 days
    }

    @Test
    public void testValidateNullAccommodation() {
        String result = forumViewModel.validateTripInput(
                new Date(),   new Date(),             "Destination",
                null, "dining"
        );
        assertEquals("Accommodations cannot be empty!", result);
    }

    @Test
    public void testValidateNullDestination() {
        String result = forumViewModel.validateTripInput(
                new Date(),   new Date(),             null,
                "accommodation", "dining"
        );
        assertEquals("Destination cannot be empty!", result);
    }

    @Test
    public void testValidateNullDining() {
        String result = forumViewModel.validateTripInput(
                new Date(),   new Date(),             "Destination",
                "accommodation", null
        );
        assertEquals("Dining Reservations cannot be empty!", result);
    }
    @Test
    public void testValidateNullCheckOut() {
        String result = forumViewModel.validateTripInput(
                new Date(),  null,             "Destination",
                "accommodation", "dining"
        );
        assertEquals("Check-out date must be selected!", result);
    }
    @Test
    public void testValidateNullCheckIn() {
        String result = forumViewModel.validateTripInput(
                null,   new Date(),             "Destination",
                "accommodation", "dining"
        );
        assertEquals( "Check-in date must be selected!", result);
    }


}
