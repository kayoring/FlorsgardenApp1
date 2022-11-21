package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class ChangeAddressActivity extends AppCompatActivity {

    Button changeAddress;
    ProgressDialog pdialog;
    String userType = "customer";

    FirebaseUser fuser;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.logpass);
        pdialog = new ProgressDialog(this);
        changeAddress = findViewById(R.id.changeAddress);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChangeAddressActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        email.setText(fuser.getEmail());
        password.requestFocus();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("customer")) {
                    startActivity(new Intent(ChangeAddressActivity.this, ProfileActivity.class));
                } else {
                    startActivity(new Intent(ChangeAddressActivity.this, AdminProfileActivity.class));
                }

            }
        });

        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername(email.getText().toString(), password.getText().toString());
                pdialog.setTitle("Change address");
                pdialog.setMessage("Please wait for the system to change your address...");
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
            }
        });


    }

    private void changeUsername(String email, final String password) {
        EditText nAddress = findViewById(R.id.nAddress);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());

                    Map<String, Object> map = new HashMap<>();
                    map.put("address", nAddress.getText().toString());

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
                                        Toast.makeText(ChangeAddressActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                if (userType.equals("customer")){
                                    startActivity(new Intent(ChangeAddressActivity.this, ProfileActivity.class));
                                } else {
                                    startActivity(new Intent(ChangeAddressActivity.this, AdminProfileActivity.class));
                                }

                                Toast.makeText(ChangeAddressActivity.this, "Address Changed" + " Current address is " + nAddress.getText().toString(), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(ChangeAddressActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    pdialog.dismiss();
                } else {
                    EditText password = findViewById(R.id.logpass);
                    password.setText("");
                    password.requestFocus();
                    pdialog.dismiss();
                    Toast.makeText(ChangeAddressActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}