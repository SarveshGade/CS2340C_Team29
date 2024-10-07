package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sprintproject.R;
import com.example.sprintproject.model.User;
import com.example.sprintproject.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextUser, editTextPassword;
    private Button logButton;
    private ProgressBar progressBar;
    private TextView textView;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        editTextUser = findViewById(R.id.user);
        editTextPassword = findViewById(R.id.password);
        logButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progress_bar);
        textView = findViewById(R.id.register_now);

        textView.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        logButton.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String user = String.valueOf(editTextUser.getText());
            String password = String.valueOf(editTextPassword.getText());

            if (user.isEmpty()) {
                editTextUser.setError("Email cannot be empty");
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (password.isEmpty()) {
                editTextPassword.setError("Password cannot be empty");
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                editTextUser.setError("Not a valid email");
                progressBar.setVisibility(View.GONE);
                return;
            }

            User loginUser = new User(user, password);
            loginViewModel.login(loginUser);
        });

        loginViewModel.getLoginSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(LoginActivity.this, "Successfully logged In!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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