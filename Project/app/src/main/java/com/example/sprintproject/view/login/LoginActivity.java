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
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.Traveler;
import com.example.sprintproject.view.logistics.LogisticsActivity;
import com.example.sprintproject.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextUser;
    private TextInputEditText editTextPassword;
    private ProgressBar progressBar;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        editTextUser = findViewById(R.id.user);
        editTextPassword = findViewById(R.id.password);
        Button logButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        TextView textView = findViewById(R.id.register_now);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        logButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String user = String.valueOf(editTextUser.getText());
            String password = String.valueOf(editTextPassword.getText());

            Traveler loginTraveler = new Traveler(user, password);
            loginViewModel.login(loginTraveler);
        });

        loginViewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(LoginActivity.this,
                        "Successfully logged In!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
            progressBar.setVisibility(View.GONE);
        });

        loginViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}

