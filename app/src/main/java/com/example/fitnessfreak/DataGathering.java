package com.example.fitnessfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static java.lang.System.*;
import static java.lang.System.currentTimeMillis;

public class DataGathering extends AppCompatActivity {

    private EditText CalorieGoal;
    private String Email;
    private EditText FirstName;
    private EditText Surname;
    private EditText GoalWeight;
    private EditText CurrentWeight;
    private EditText Height;
    private Spinner Sex;
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    String userID;

    private Button ButtonMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_gathering);

        ButtonMainActivity = findViewById(R.id.BtnGatherData);
        CalorieGoal = findViewById(R.id.ETDataCalorieGoal);
        FirstName = findViewById(R.id.ETDataFirstName);
        Surname = findViewById(R.id.ETDataSurname);
        GoalWeight = findViewById(R.id.ETDataGoalWeight);
        CurrentWeight = findViewById(R.id.ETDataCurrentWeight);
        Height = findViewById(R.id.ETDataHeight);

        Sex = findViewById(R.id.SpinSex);
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

        ButtonMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String calorieGoal = CalorieGoal.getText().toString();
                String firstName = FirstName.getText().toString();
                String surname = Surname.getText().toString();
                String goalWeight = GoalWeight.getText().toString();
                String currentWeight = CurrentWeight.getText().toString();
                String gender = Sex.getSelectedItem().toString();
                String height = Height.getText().toString();

                if (TextUtils.isEmpty(calorieGoal) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(goalWeight) || TextUtils.isEmpty(currentWeight) || TextUtils.isEmpty(gender) || TextUtils.isEmpty(height))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Values need to be entered for all the fields above to continue!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    DocumentReference documentReference = fStore.collection("Users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Calorie Goal", calorieGoal);
                    user.put("Email", Email = fAuth.getCurrentUser().getEmail());
                    user.put("First Name", firstName);
                    user.put("Surname", surname);
                    user.put("Goal Weight", goalWeight);
                    user.put("Height", height);
                    user.put("Sex", gender);
                    user.put("Weight", currentWeight);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Data has been gathered and stored!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                    DocumentReference docRef = fStore.collection("Logs").document(userID);
                    Map<String, Object> map = new HashMap<>();
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date);
                    Map<String, Object> arr = new HashMap<>();
                    arr.put("Date", timestamp);
                    arr.put("Weight", currentWeight);
                    map.put("LogArray", Arrays.asList(arr));

                    docRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast toast = Toast.makeText(getApplicationContext(), "First Log has been Captured!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
                startActivity(new Intent(getApplicationContext(), MainMenu.class));
                finish();
            }
        });
    }
}