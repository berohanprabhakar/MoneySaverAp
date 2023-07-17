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

public class SignUpActivity extends AppCompatActivity {
    private EditText email,password;
    private Button signUpbtn;
    private TextView login;
    FirebaseAuth mauth;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signUpbtn=findViewById(R.id.signUpbtn);
        login=findViewById(R.id.login);
        mauth=FirebaseAuth.getInstance();
        progress=new ProgressDialog(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String emailString=email.getText().toString();
                String passwordString=password.getText().toString();
                if(TextUtils.isEmpty(emailString))
                {
                    email.setError("Email is required");
                }
                else  if(TextUtils.isEmpty(passwordString))
                {
                    password.setError("Password is required");
                }
                else
                {
                    progress.setTitle("Signing up");
                    progress.setMessage("Registration in progress");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                    mauth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent=new Intent(SignUpActivity.this,MainActivity2.class);
                                startActivity(intent);
                                finish();
                                progress.dismiss();
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        }
                    });
                }
            }
        });


    }
}