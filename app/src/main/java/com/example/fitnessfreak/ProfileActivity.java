package com.example.fitnessfreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private TextView Current;
    private TextView Goal;
    private String userID;
    private TextView Heading;
    private LinearLayout LinearLay;
    private Button EditProfile;
    private Button BtnOpenUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Current = findViewById(R.id.TVCurrentWeight);
        Goal = findViewById(R.id.TVGoalWeight);
        Heading = findViewById(R.id.TVProfileHeading);
        LinearLay = findViewById(R.id.LinearLog);
        EditProfile = findViewById(R.id.BtnEditProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        try {
            EditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Opening a new activity where they can edit their profile
                    openEditProfileActivity();
                }
            });

            //Setting the text on the profile to the values stored for the current user
            DocumentReference documentReference = fStore.collection("Users").document(userID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    Current.setText("Current Weight: " + documentSnapshot.getString("Weight"));
                    Goal.setText("Goal Weight: " + documentSnapshot.getString("Goal Weight"));
                    Heading.setText("User Profile:\n " + documentSnapshot.getString("Email") + "!");
                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "ERROR: " +ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            //Getting the weight logs from a map within an array in fire store by getting the collection
            //then getting the document which is the current users user id
            //then searching for the array called log array
            //then searching for the values within that log array and displaying that to the user as those are the previously stored weight logs
            FirebaseFirestore.getInstance().collection("Logs").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Map<String, Object> map = documentSnapshot.getData();
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            if (entry.getKey().equals("LogArray")) {
                                TextView textView = new TextView(getApplicationContext());
                                textView.setText(entry.getValue().toString());
                                textView.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                                LinearLay.addView(textView);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Shows the user a text view if they don't have any stored weight logs
                    TextView textView = new TextView(getApplicationContext());
                    textView.setText("No Weight Logs");
                    textView.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    LinearLay.addView(textView);
                    Toast.makeText(ProfileActivity.this, "User has no logs!", Toast.LENGTH_SHORT).show();
                }
            });

            BtnOpenUp = findViewById(R.id.BtnOpenUpload);
            BtnOpenUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Starting a new activity
                    openUploadActivity();
                }
            });
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "ERROR: " +ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openEditProfileActivity()
    {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
        finish();
    }

    public void openUploadActivity()
    {
        Intent intent = new Intent(this, UploadImage.class);
        startActivity(intent);
        finish();
    }
}