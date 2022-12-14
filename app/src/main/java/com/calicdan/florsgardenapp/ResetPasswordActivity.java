package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText send_email;
    Button resetBtn;

    FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseUser fuser;
    DatabaseReference ref;

    String userType = "customer";

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {


            fuser = FirebaseAuth.getInstance().getCurrentUser();
            ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ResetPasswordActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    if (userType.equals("customer")) {
                        startActivity(new Intent(ResetPasswordActivity.this, ProfileActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(ResetPasswordActivity.this, AdminProfileActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(ResetPasswordActivity.this, Login.class));
                    finish();
                }
            }
        });

        send_email = findViewById(R.id.send_email);

        resetBtn = findViewById(R.id.resetBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = send_email.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(ResetPasswordActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Please check you Email!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this, Login.class));
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

}
