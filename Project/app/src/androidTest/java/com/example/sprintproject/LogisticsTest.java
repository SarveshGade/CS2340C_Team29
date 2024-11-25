package com.example.sprintproject;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.viewmodel.LogisticsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LogisticsTest {
    private LogisticsViewModel logisticsViewModel;

    @Before
    public void setUp() {
        logisticsViewModel = new LogisticsViewModel();
    }

    @Test
    public void testInvalidNote() {
        Boolean result = logisticsViewModel.validateNote("");
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testValidNote() {
        Boolean result = logisticsViewModel.validateNote("Valid note");
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testInvalidEmail() {
        Boolean result = logisticsViewModel.validateEmail("");
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testValidEmail() {
        Boolean result = logisticsViewModel.validateEmail("Valid note");
        assertEquals(Boolean.TRUE, result);
    }
}
