package com.example.buzzrank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewEvents;
    private ArrayList<Event> eventList;
    private EventsAdapter eventsAdapter;
    private FirebaseFirestore db;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        // Get the isAdmin flag from Intent
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        eventList = new ArrayList<>();

        // Pass the isAdmin flag to the adapter
        eventsAdapter = new EventsAdapter(eventList, this::onEventClick, isAdmin);

        recyclerViewEvents.setAdapter(eventsAdapter);

        fetchEvents();
    }

    private void fetchEvents() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        eventList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);
                            eventList.add(event);
                        }
                        eventsAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(EventListActivity.this, "Error fetching events", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onEventClick(Event event) {
        Intent intent;
        if (isAdmin) {
            // If admin, navigate to EventDetailActivity
            intent = new Intent(EventListActivity.this, EventDetailActivity.class);
            intent.putExtra("eventId", event.getId());
        } else {
            // If user, navigate to MainActivity
            intent = new Intent(EventListActivity.this, MainActivity.class);
        }
        startActivity(intent);
    }
}