package com.example.buzzrank;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EventDetailActivity extends AppCompatActivity {

    private TextView textViewEventName, textViewParticipent;
    private RecyclerView recyclerViewParticipants;
    private FirebaseFirestore db;
    private ParticipantAdapter participantAdapter;
    private List<Participant> participantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        textViewEventName = findViewById(R.id.textViewEventName);
        textViewParticipent = findViewById(R.id.textViewParticipents);
        recyclerViewParticipants = findViewById(R.id.recyclerViewParticipants);
        db = FirebaseFirestore.getInstance();
        participantsList = new ArrayList<>();
        participantAdapter = new ParticipantAdapter(participantsList);

        // Set up RecyclerView
        recyclerViewParticipants.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewParticipants.setAdapter(participantAdapter);

        // Get event ID passed from EventListActivity
        String eventId = getIntent().getStringExtra("eventId");

        // Fetch event details from Firestore
        if (eventId != null) {
            db.collection("events").document(eventId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Event event = document.toObject(Event.class);
                                textViewEventName.setText(event.getName());

                                // Fetch participants list and their timestamps
                                db.collection("events").document(eventId)
                                        .collection("participants")
                                        .orderBy("timestamp") // Assuming the timestamp field is stored in Firestore
                                        .get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                QuerySnapshot querySnapshot = task1.getResult();
                                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                    for (DocumentSnapshot participantDoc : querySnapshot) {
                                                        Participant participant = participantDoc.toObject(Participant.class);
                                                        participantsList.add(participant);
                                                    }
                                                    participantAdapter.notifyDataSetChanged();
                                                } else {
                                                    Log.d("EventDetailActivity", "No participants found");
                                                    Toast.makeText(EventDetailActivity.this, "No participants found", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Log.e("EventDetailActivity", "Error fetching participants: " + task1.getException());
                                                Toast.makeText(EventDetailActivity.this, "Error fetching participants", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(EventDetailActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EventDetailActivity.this, "Error fetching event", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
