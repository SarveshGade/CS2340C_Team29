package com.example.sprintproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sprintproject.R;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forum);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });





        ImageButton logisticsButton = findViewById(R.id.logisticsButton);
        ImageButton locationButton = findViewById(R.id.locationButton);
        ImageButton diningButton = findViewById(R.id.diningButton);
        ImageButton accommodationsButton = findViewById(R.id.accommodationsButton);
        ImageButton forumButton = findViewById(R.id.forumButton);

        logisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, LogisticsActivity.class);
                startActivity(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        diningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, DiningActivity.class);
                startActivity(intent);
            }
        });

        accommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, AccommodationsActivity.class);
                startActivity(intent);
            }
        });

        forumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForumActivity.this, ForumActivity.class);
                startActivity(intent);
            }
        });
    }
}