package com.example.fitnessfreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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


    //IMAGE VARIABLES START HERE
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button BtnChooseUpload;
    private Button BtnUploadImage;
    private TextView ShowUploads;
    private EditText FileName;
    private ImageView Image;
    private ProgressBar ProgressBar;
    private Uri imageUri;

    private StorageReference fStorage;


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

        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfileActivity();
            }
        });

        DocumentReference documentReference = fStore.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Current.setText("Current Weight: " + documentSnapshot.getString("Weight"));
                Goal.setText("Goal Weight: " + documentSnapshot.getString("Goal Weight"));
                Heading.setText("User Profile:\n " + documentSnapshot.getString("Email") +"!");
            }
        });

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
                            LinearLay.addView(textView);
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "User has no logs!", Toast.LENGTH_SHORT).show();
            }
        });


        //IMAGE STUFF STARTS HERE
        BtnChooseUpload = findViewById(R.id.BtnChooseImage);
        BtnUploadImage = findViewById(R.id.BtnUploadImage);
        ShowUploads = findViewById(R.id.BtnShowUploads);
        FileName = findViewById(R.id.ETFileNme);
        Image = findViewById(R.id.ImageView);
        ProgressBar = findViewById(R.id.ProgressUpload);

        BtnChooseUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        BtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void openEditProfileActivity()
    {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
        finish();
    }

    public void openFileChooser()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(Image);
        }
    }
}