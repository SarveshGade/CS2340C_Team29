package com.example.sprintproject.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Traveler;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.viewmodel.RegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private ProgressBar progressBar;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        Button regButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progress_bar);
        TextView textView = findViewById(R.id.login_now);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        regButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());
            String emailError = registerViewModel.validateEmail(email);

            if (emailError != null) {
                editTextEmail.setError(emailError);
                progressBar.setVisibility(View.GONE);
                return;
            }
            String passwordError = registerViewModel.validatePassword(password);
            if (passwordError != null) {
                editTextPassword.setError(passwordError);
                progressBar.setVisibility(View.GONE);
                return;
            }

            Traveler traveler = new Traveler(email, password);
            registerViewModel.register(traveler);
        });

        registerViewModel.getRegistrationSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(RegisterActivity.this, "Account created successfully!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
            progressBar.setVisibility(View.GONE);
        });

        registerViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}