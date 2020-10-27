package com.example.fitnessfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadImage extends AppCompatActivity {

    private String userID;
    private FirebaseAuth fAuth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button BtnChooseUpload;
    private Button BtnUploadImage;
    private TextView ShowUploads;
    private EditText FileName;
    private ImageView Image;
    private android.widget.ProgressBar ProgressBar;
    Uri imageUri;

    private StorageReference fStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        BtnChooseUpload = findViewById(R.id.BtnChooseImage);
        BtnUploadImage = findViewById(R.id.BtnUploadImage);
        ShowUploads = findViewById(R.id.BtnShowUploads);
        FileName = findViewById(R.id.ETFileNme);
        Image = findViewById(R.id.ImageView);
        ProgressBar = findViewById(R.id.ProgressUpload);

        userID = FirebaseAuth.getInstance().getUid();
        fStorage = FirebaseStorage.getInstance().getReference(userID);

        BtnChooseUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        BtnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUploader();
            }
        });

        ShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void openFileChooser()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    public void ImageUploader()
    {
        if (TextUtils.isEmpty(FileName.getText())){
            FileName.setText("NoName" + System.currentTimeMillis());
            StorageReference ref = fStorage.child(FileName.getText() + "." + getExtension(imageUri));
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Image Uploaded Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        else {
            StorageReference ref = fStorage.child(FileName.getText() + "." + getExtension(imageUri));
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "Image Uploaded Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
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