package com.example.fitnessfreak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText mRegisterEmail, mRegisterPassword;
    Button mRegisterButton;
    TextView mSignIn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterEmail = findViewById(R.id.TxbRegisterEmail);
        mRegisterPassword = findViewById(R.id.TxbRegisterPassword);
        mRegisterButton = findViewById(R.id.BtnRegister);
        mSignIn = findViewById(R.id.TVSignIN);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.RegisterProgress);

        if (fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mRegisterEmail.getText().toString().trim();
                String password = mRegisterPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    mRegisterEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mRegisterPassword.setError("Password is required");
                    return;
                }
                if (password.length() < 6)
                {
                    mRegisterPassword.setError("Password must be more than 6 characters");
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "Welcome! User has been Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), DataGathering.class));
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }
}