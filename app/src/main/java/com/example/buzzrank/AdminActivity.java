package com.example.buzzrank;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    private Button btnEvents, btnNewEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnEvents = findViewById(R.id.btnEvents);
        btnNewEvents = findViewById(R.id.btnNewEvents);

        // Navigate to EventListActivity when "Events" button is clicked
        btnEvents.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, EventListActivity.class);
            intent.putExtra("isAdmin", true);
            startActivity(intent);
        });

        // Navigate to create a new event when "New Events" button is clicked
        btnNewEvents.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, CreateEventActivity.class);
            startActivity(intent);
        });
    }
}