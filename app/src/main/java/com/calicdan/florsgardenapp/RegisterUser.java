package com.calicdan.florsgardenapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

    private TextView banner, registerUser, txtPrivacy, loginA, smsReg;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPass;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private CheckBox check_box;

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
        loginA = (TextView) findViewById(R.id.loginA);
        loginA.setOnClickListener(this);
        smsReg = (TextView) findViewById(R.id.smsReg);
        smsReg.setOnClickListener(this);
        registerUser = (Button) findViewById(R.id.register);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.emailA);
        editTextPassword = (EditText) findViewById(R.id.passwordA);
        editTextConfirmPass = (EditText) findViewById(R.id.confirmPass);
        check_box = findViewById(R.id.check_box);
        txtPrivacy = (TextView) findViewById(R.id.txtPrivacy);

        Dialog dialog = new Dialog(RegisterUser.this);
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.privacy_policy_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes();
                dialog.show();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginA:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.smsReg:
                startActivity(new Intent(this, SmsRegister.class));
                break;
            case R.id.register:
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String fullName = editTextFullName.getText().toString().trim();
                String confirmPass = editTextConfirmPass.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()) {
                    if (fullName.isEmpty()) {
                        editTextFullName.requestFocus();
                    }  else if (email.isEmpty()) {
                        editTextEmail.requestFocus();
                    } else if (password.isEmpty()) {
                        editTextPassword.requestFocus();
                    } else if (confirmPass.isEmpty()) {
                        editTextConfirmPass.requestFocus();
                    }
                    Toast.makeText(RegisterUser.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterUser.this, "Minimum password length is 6 characters!", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterUser.this, "Please provide valid email!", Toast.LENGTH_SHORT).show();
                } else if (!confirmPass.equals(password)) {
                    Toast.makeText(RegisterUser.this, "Password do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    if (check_box.isChecked()) {
                        registerUser(fullName, email, password);
                    } else {
                        Toast.makeText(RegisterUser.this, "Please agree to the Privacy Policy", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
        }
    }

    private void registerUser(final String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            //User user = new User(fullName, contact, email);
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", fullName);
                            hashMap.put("contact", "null");
                            hashMap.put("email", email);
                            hashMap.put("password", password);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", fullName.toLowerCase());
                            hashMap.put("usertype", "customer");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(RegisterUser.this, "User has been registered successfully!, Check email to verify your account then proceed to login", Toast.LENGTH_LONG).show();
                                    firebaseUser.sendEmailVerification();
                                }
                            });
                        } else {
                            Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}