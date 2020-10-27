package com.example.fitnessfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class WorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private TextView MorningWork;
    private TextView EveningWork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        MorningWork = findViewById(R.id.TVMorningWorkout);
        EveningWork = findViewById(R.id.TVEveningWorkout);

        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("Workout").document("Week 1");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                MorningWork.setText(documentSnapshot.getString("Morning Workout"));
                EveningWork.setText(documentSnapshot.getString("Evening Workout"));
            }
        });
    }
}