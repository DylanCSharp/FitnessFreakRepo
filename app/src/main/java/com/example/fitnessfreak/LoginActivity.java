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

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    //Declaring variables
    EditText mEmail, mPassword;
    Button ButtonLogin;
    ProgressBar progressBar;
    private TextView RegisterTV;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acitvity);

        //Getting the firebase authentication instance
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(), MainMenu.class));
            finish();
        }

        //Instantiating variables
        ButtonLogin = (Button) findViewById(R.id.BtnLogin);
        RegisterTV = (TextView) findViewById(R.id.TVRegister);
        mEmail = findViewById(R.id.TxbEmail);
        mPassword = findViewById(R.id.TxbPassword);
        progressBar = findViewById(R.id.LoginProgress);

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openActivityMainMenu();
            }
        });

        RegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
    }

    public void openActivityMainMenu() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        try {
            //Checking to see if the user has entered and email address
            if (TextUtils.isEmpty(email) || email == "") {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //Signing the user in with a built in firebase auth method
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainMenu.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), "ERROR: " +ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openRegisterActivity()
    {
        //Starting a new activity depending on the email and password
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}