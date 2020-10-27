package com.example.fitnessfreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class RecipeActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private TextView Breakfast;
    private TextView Lunch;
    private TextView Dinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Breakfast = findViewById(R.id.TVBreakfast);
        Lunch = findViewById(R.id.TVLunch);
        Dinner = findViewById(R.id.TVDinner);

        fStore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = fStore.collection("Recipes").document("Week 1");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Breakfast.setText(documentSnapshot.getString("Breakfast"));
                Lunch.setText(documentSnapshot.getString("Lunch"));
                Dinner.setText(documentSnapshot.getString("Dinner"));
            }
        });




    }
}