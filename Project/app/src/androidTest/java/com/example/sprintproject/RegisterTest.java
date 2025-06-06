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

    @Test
    public void testValidateEmptyPassword() {
        String result = registerViewModel.validatePassword("");
        assertEquals("Password cannot be empty", result);
    }

    @Test
    public void testValidateValidPassword() {
        String result = registerViewModel.validatePassword("validPassword12");
        assertNull(result);
    }

    @Test
    public void testValidateInvalidPasswordNoDigits() {
        String result = registerViewModel.validatePassword("validPassword");
        assertEquals("Doesn't contain digit", result);
    }
}