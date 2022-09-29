package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText editTextFullName, editTextEmail, editContactNumber, editTextPassword, editTextConfirmPass;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.textView6);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.emailA);
        editContactNumber = (EditText) findViewById(R.id.contacNo);
        editTextPassword = (EditText) findViewById(R.id.passwordA);
        editTextConfirmPass = (EditText) findViewById(R.id.confirmPass);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginA:
                startActivity(new Intent(this, Login.class));
                break;

            case R.id.register:
                String email= editTextEmail.getText().toString().trim();
                String password= editTextPassword.getText().toString().trim();
                String fullName= editTextFullName.getText().toString().trim();
                String contact= editContactNumber.getText().toString().trim();
                String confirmPass= editTextConfirmPass.getText().toString().trim();

                if (fullName.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
                    if (fullName.isEmpty()) {
                        editTextFullName.requestFocus();
                    } else if (contact.isEmpty()) {
                        editContactNumber.requestFocus();
                    } else if (email.isEmpty()) {
                        editTextEmail.requestFocus();
                    } else if (password.isEmpty()) {
                        editTextPassword.requestFocus();
                    } else if (confirmPass.isEmpty()) {
                        editTextConfirmPass.requestFocus();
                    }
                    Toast.makeText(RegisterUser.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6 ){
                    Toast.makeText(RegisterUser.this, "Minimum password length is 6 characters!", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterUser.this, "Please provide valid email!", Toast.LENGTH_SHORT).show();
                } else if (!confirmPass.equals(password)){
                    Toast.makeText(RegisterUser.this, "Password do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(fullName, email, password, contact);
                }
                break;
        }
    }

    private void registerUser(final String fullName, String email, String password, String contact) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            //User user = new User(fullName, contact, email);
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", fullName);
                            hashMap.put("contact", contact);
                            hashMap.put("email", email);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(RegisterUser.this, Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this,"Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}