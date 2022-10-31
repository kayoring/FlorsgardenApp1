package com.calicdan.florsgardenapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddGuidesWorms extends AppCompatActivity {

    EditText name, description, Surl;
    Button btnBack, btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_guides);


        name = findViewById(R.id.txtName);
        description = findViewById(R.id.txtDesc);
        Surl = findViewById(R.id.txtSurl);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void insertData() {

        Map<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("description", description.getText().toString());
        map.put("Surl", Surl.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Guides/Worms").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddGuidesWorms.this, "Data Inserted Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddGuidesWorms.this, "Error! Cannot Insert Data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}