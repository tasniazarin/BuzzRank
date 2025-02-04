package com.example.buzzrank;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Button buzzerButton;
    private String username;
    private boolean hasPressedBuzzer = false; // Track if user has pressed during this session

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();
        buzzerButton = findViewById(R.id.buzzer_button);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        username = getIntent().getStringExtra("username");

        buzzerButton.setOnClickListener(v -> {
            if (hasPressedBuzzer) {
                // Change button text to "Already Pressed" instead of showing Toast
                buzzerButton.setText("Already Pressed");
                return;
            }

            playBuzzerEffects();
            logBuzzerPressToFirebase(currentUser);
        });
    }

    private void logBuzzerPressToFirebase(FirebaseUser user) {
        if (user == null) {
            Toast.makeText(this, "User authentication error!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> buzzerData = new HashMap<>();
        buzzerData.put("name", username);
        buzzerData.put("email", user.getEmail());
        buzzerData.put("userId", user.getUid());
        buzzerData.put("timestamp", System.currentTimeMillis());

        firestore.collection("Buzzers").add(buzzerData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Buzzer", "Buzzer pressed successfully!");
                    hasPressedBuzzer = true; // Now user cannot press again
                    Toast.makeText(MainActivity.this, "Buzzer Pressed!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("Buzzer", "Error logging buzzer press", e));
    }

    private void playBuzzerEffects() {
        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.buzzer_sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                buzzerButton,
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f)
        );
        scaleDown.setDuration(300);
        scaleDown.setRepeatCount(1);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();
    }
}
