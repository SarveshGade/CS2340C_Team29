package com.example.sprintproject;
import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sprintproject.viewmodel.LoginViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginTest {

    private LoginViewModel loginViewModel;

    @Before
    public void setUp() {
        loginViewModel = new LoginViewModel(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void testValidateEmptyEmail() {
        String result = loginViewModel.validateEmail("");
        assertEquals("Email cannot be empty", result);
    }

    @Test
    public void testValidateInvalidEmail() {
        String result = loginViewModel.validateEmail("invalidEmail");
        assertEquals("Not a valid email", result);
    }

    @Test
    public void testValidateEmptyPassword() {
        String result = loginViewModel.validatePassword("");
        assertEquals("Password cannot be empty", result);
    }

    @Test
    public void testValidateValidPassword() {
        String result = loginViewModel.validatePassword("validPassword");
        assertNull(result);
    }


}