package com.example.sprintproject;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.viewmodel.RegisterViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterTest {

    private RegisterViewModel registerViewModel;

    @Before
    public void setUp() {
        registerViewModel = new RegisterViewModel(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void testValidateEmptyEmail() {
        String result = registerViewModel.validateEmail("");
        assertEquals("Email cannot be empty", result);
    }

    @Test
    public void testValidateInvalidEmail() {
        String result = registerViewModel.validateEmail("invalidEmail");
        assertEquals("Not a valid email", result);
    }
}