package com.example.moneysaverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    Button loginbtn;
    TextView signUp;
    FirebaseAuth mauth;
    ProgressDialog progress;
    private FirebaseAuth.AuthStateListener authStateListener;


    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user  =mauth.getCurrentUser();
                if (user!=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginbtn = findViewById(R.id.loginbtn);
        signUp = findViewById(R.id.signUp);
        mauth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                if (TextUtils.isEmpty(emailString)) {
                    email.setError("Email is required");
                } else if (TextUtils.isEmpty(passwordString)) {
                    password.setError("Password is required");
                } else {
                    progress.setTitle("Login");
                    progress.setMessage("Login in progress");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    mauth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                                startActivity(intent);
                                finish();
                                progress.dismiss();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mauth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mauth.removeAuthStateListener(authStateListener);
    }
}