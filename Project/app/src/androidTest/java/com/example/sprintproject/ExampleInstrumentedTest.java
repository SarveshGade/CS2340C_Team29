package com.example.sprintproject;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.sprintproject.viewmodel.LoginViewModel;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.sprintproject", appContext.getPackageName());
    }

    public static class LoginTest {

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
        public void testValidateValidEmail() {
            String result = loginViewModel.validateEmail("validUser@example.com");
            assertNull(result);
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
}