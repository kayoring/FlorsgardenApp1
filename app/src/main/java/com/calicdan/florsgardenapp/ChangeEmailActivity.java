package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.calicdan.florsgardenapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailActivity extends AppCompatActivity {

    Button changeemail;
    ProgressDialog pdialog;
    String userType = "customer";

    FirebaseUser fuser;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChangeEmailActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userType.equals("customer")){
                    startActivity(new Intent(ChangeEmailActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(ChangeEmailActivity.this, AdminProfileActivity.class));
                }

            }
        });

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.logpass);

        email.setText(fuser.getEmail());
        password.requestFocus();
        
        pdialog  = new ProgressDialog(this);

        changeemail = findViewById(R.id.changeemail);

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeemail(email.getText().toString(), password.getText().toString());
                pdialog.setTitle("Change email");
                pdialog.setMessage("Please wait for the system to change your email...");
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
            }
        });

    }

    EditText change;

    private void changeemail(String email, final String password) {

        change = findViewById(R.id.change);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("value", "User re-authenticated.");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());

                                Map<String, Object> map = new HashMap<>();
                                map.put("email", change.getText().toString());

                                ref.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");

                                            ref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    userType = dataSnapshot.getValue(String.class);
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    Toast.makeText(ChangeEmailActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            if (userType.equals("customer")){
                                                startActivity(new Intent(ChangeEmailActivity.this, ProfileActivity.class));
                                            } else {
                                                startActivity(new Intent(ChangeEmailActivity.this, AdminProfileActivity.class));
                                            }

                                            Toast.makeText(ChangeEmailActivity.this, "Email Changed" + " Current Email is " + change.getText().toString(), Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(ChangeEmailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                pdialog.dismiss();
                            } else {
                                Toast.makeText(ChangeEmailActivity.this, "Task failed successfully!", Toast.LENGTH_SHORT).show();
                                pdialog.dismiss();
                            }
                        }
                    });
                } else {
                    EditText password = findViewById(R.id.logpass);
                    password.setText("");
                    password.requestFocus();
                    pdialog.dismiss();
                    Toast.makeText(ChangeEmailActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}