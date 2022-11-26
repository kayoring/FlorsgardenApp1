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

import com.calicdan.florsgardenapp.Model.Inquiries;
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

public class ChangeUsernameActivity extends AppCompatActivity {

    Button changeUser;
    ProgressDialog pdialog;
    String userType = "customer";

    FirebaseUser fuser;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.logpass);
        pdialog = new ProgressDialog(this);
        changeUser = findViewById(R.id.changeUser);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userType = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChangeUsernameActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        email.setText(fuser.getEmail());
        password.requestFocus();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("customer")) {
                    startActivity(new Intent(ChangeUsernameActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(ChangeUsernameActivity.this, AdminProfileActivity.class));
                }

            }
        });

        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername(email.getText().toString(), password.getText().toString());
                pdialog.setTitle("Change email");
                pdialog.setMessage("Please wait for the system to change your email...");
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
            }
        });
    }

    private void changeUsername(String email, final String password) {
        EditText nUser = findViewById(R.id.nUser);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);



        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());

                    Map<String, Object> mapster = new HashMap<>();
                    mapster.put("username", nUser.getText().toString());
                    mapster.put("search", nUser.getText().toString().toLowerCase());

                    ref.updateChildren(mapster).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        userType = dataSnapshot.getValue(String.class);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(ChangeUsernameActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                                if (userType.equals("customer")){
                                    startActivity(new Intent(ChangeUsernameActivity.this, ProfileActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(ChangeUsernameActivity.this, AdminProfileActivity.class));
                                    finish();
                                }

                                Toast.makeText(ChangeUsernameActivity.this, "Username Changed" + " Current username is " + nUser.getText().toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ChangeUsernameActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    DatabaseReference qref = FirebaseDatabase.getInstance().getReference("Forums").child("Questions");

                    qref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        //pointed at questions
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // get push id;s
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //get value inside the nodes child named id and store it in a string.
                                String id = snapshot.child("id").getValue().toString();
                                //String username = snapshot.child("username").getValue().toString();
                                //check if it is the same as the current user id
                                if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    //HashMap<String, Object> hashMap = new HashMap<>();
                                    //hashMap.put("username", con);
                                    //snapshot.getRef() == push id's
                                    snapshot.getRef().child("username").setValue(nUser.getText().toString());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(ChangeUsernameActivity.this, "fail", Toast.LENGTH_SHORT).show();
                        }
                    });

                    pdialog.dismiss();
                } else {
                    EditText password = findViewById(R.id.logpass);
                    password.setText("");
                    password.requestFocus();
                    pdialog.dismiss();
                    Toast.makeText(ChangeUsernameActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}