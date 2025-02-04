package com.example.buzzrank;


import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CreateEventActivity extends AppCompatActivity {

    private EditText editTextEventName;
    private Button btnCreateEvent;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();
        editTextEventName = findViewById(R.id.editTextEventName);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(v -> {
            String eventName = editTextEventName.getText().toString().trim();
            if (!eventName.isEmpty()) {
                Event newEvent = new Event(eventName, UUID.randomUUID().toString());
                db.collection("events").document(newEvent.getId()).set(newEvent)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(CreateEventActivity.this, "Event created", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(CreateEventActivity.this, "Error creating event", Toast.LENGTH_SHORT).show());
            }
        });
    }
}