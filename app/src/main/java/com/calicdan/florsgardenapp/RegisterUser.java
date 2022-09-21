package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText editTextFullName, editTextEmail, editContactNumber, editTextPassword;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.textView6);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.emailA);
        editContactNumber = (EditText) findViewById(R.id.contacNo);
        editTextPassword = (EditText) findViewById(R.id.passwordA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView6:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        String email= editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();
        String fullName= editTextFullName.getText().toString().trim();
        String contact= editContactNumber.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Full name is required!");
            editTextFullName.requestFocus();
            return;
        }

        if(contact.isEmpty()){
            editContactNumber.setError("Contact Number is requried!");
            editContactNumber.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editTextEmail.setError("Email is requried!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;

        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is requried!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            User user = new User(fullName, contact, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(RegisterUser.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterUser.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}