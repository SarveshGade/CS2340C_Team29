package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.view.forum.ForumActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class ForumTest {

    private ForumActivity forumActivity;

    @Before
    public void setUp() {
        ActivityScenario<ForumActivity> scenario = ActivityScenario.launch(ForumActivity.class);
        scenario.onActivity(activity -> forumActivity = activity);
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






}
