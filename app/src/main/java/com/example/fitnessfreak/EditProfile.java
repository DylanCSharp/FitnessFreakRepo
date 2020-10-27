package com.example.fitnessfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class EditProfile extends AppCompatActivity {

    private Spinner Sex;
    private EditText FirstName;
    private EditText Surname;
    private EditText CalorieGoal;
    private EditText CurrentWeight;
    private EditText GoalWeight;
    private EditText Height;
    private Button BtnSave;
    private String userID;

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Sex = findViewById(R.id.EditSpinSex);
        List<String> gender = new ArrayList<>();
        String male = "Male";
        String female = "Female";
        gender.add(male);
        gender.add(female);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sex.setAdapter(adapter);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        FirstName = findViewById(R.id.ETEditFirstName);
        Surname = findViewById(R.id.ETEditSurname);
        CalorieGoal = findViewById(R.id.ETEditCalorieGoal);
        //Add to logs as well
        CurrentWeight = findViewById(R.id.ETEditCurrentWeight);
        GoalWeight = findViewById(R.id.ETEditGoalWeight);
        Height = findViewById(R.id.ETEditHeight);
        BtnSave = findViewById(R.id.BtnSaveEdit);

        DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                FirstName.setText(documentSnapshot.getString("First Name"));
                Surname.setText(documentSnapshot.getString("Surname"));
                CalorieGoal.setText(documentSnapshot.getString("Calorie Goal"));
                CurrentWeight.setText(documentSnapshot.getString("Weight"));
                GoalWeight.setText(documentSnapshot.getString("Goal Weight"));
                Height.setText(documentSnapshot.getString("Height"));

                if (documentSnapshot.getString("Sex").equals("Male")) {
                    Sex.setSelection(0);
                } else {
                    Sex.setSelection(1);
                }
            }
        });


        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(FirstName.getText()) || TextUtils.isEmpty(Surname.getText()) || TextUtils.isEmpty(CalorieGoal.getText()) || TextUtils.isEmpty(CurrentWeight.getText()) || TextUtils.isEmpty(GoalWeight.getText()) || TextUtils.isEmpty(Height.getText())) {
                    Toast.makeText(getApplicationContext(), "Enter values for all the items above", Toast.LENGTH_SHORT).show();
                }
                else {
                    DocumentReference documentReference = fStore.collection("Users").document(userID);
                    documentReference.update("First Name", FirstName.getText().toString());
                    documentReference.update("Surname", Surname.getText().toString());
                    documentReference.update("Calorie Goal", CalorieGoal.getText().toString());
                    documentReference.update("Weight", CurrentWeight.getText().toString());
                    documentReference.update("Goal Weight", GoalWeight.getText().toString());
                    documentReference.update("Height", Height.getText().toString());
                    documentReference.update("Sex", Sex.getSelectedItem().toString());

                    Toast.makeText(getApplicationContext(), "Values have been edited and updated!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                }
            }
        });

    }
}