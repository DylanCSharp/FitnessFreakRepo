package com.example.fitnessfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class MainMenu extends AppCompatActivity {

    private Button BtnRecipeLoad;
    private Button BtnAddItem;
    private Button BtnWorkoutLoad;
    private Button BtnProfileLoad;
    private TextView Heading;
    private String userID;

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Heading = findViewById(R.id.TVHeading);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Heading.setText("" + documentSnapshot.getString("First Name") +",\nWelcome to Fitness Freak!");
            }
        });

        //LOADING THE MAIN RECIPE SCREEN WHEN USER CLICKS TO VIEW RECIPES BUTTON
        BtnRecipeLoad = findViewById(R.id.BtnMainRecipe);
        BtnRecipeLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openActivityRecipeMainMenu();
            }
        });

        //LOADING THE PARENT ADD ITEM FORM WHEN THE USER CLICKS ADD ITEMS BUTTON
        BtnAddItem = findViewById(R.id.BtnMainAdd);
        BtnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAddItem();
            }
        });

        //LOADING THE WORKOUT TAB WHEN THE USER CLICKS ON WORKOUT BUTTON
        BtnWorkoutLoad = findViewById(R.id.BtnMainWorkout);
        BtnWorkoutLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityWorkout();
            }
        });

        BtnProfileLoad = findViewById(R.id.BtnMainProfile);
        BtnProfileLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityProfile();
            }
        });
    }

    public void openActivityRecipeMainMenu()
    {
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }

    public void openActivityAddItem()
    {
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);
    }

    public void openActivityWorkout()
    {
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }

    public void openActivityProfile()
    {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}