package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.calicdan.florsgardenapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ImageRecognitionHome extends AppCompatActivity {

    FirebaseUser fuser;
    DatabaseReference ref;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recognition_home);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("usertype");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView imgOrganicWaste, imgWorms, imgViewBack2;

        imgOrganicWaste = findViewById(R.id.imgOrganicWaste);
        imgWorms = findViewById(R.id.imgWorms);
        imgViewBack2 = findViewById(R.id.imgViewBack2);

        imgViewBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("admin")) {
                    startActivity(new Intent(ImageRecognitionHome.this, Home.class));
                } else {
                    startActivity(new Intent(ImageRecognitionHome.this, HomeUser.class));
                }
            }
        });

        imgOrganicWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgOrganicWaste.animate().rotation(360f).setDuration(1000).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ImageRecognitionHome.this, ImageRecognitionOrganicWaste.class));
                    }
                }, 1000);



            }
        });

        imgWorms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgWorms.animate().rotation(360f).setDuration(1000).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                startActivity(new Intent(ImageRecognitionHome.this, ImageRecognitionWorm.class));
                    }
                }, 1000);


            }
        });

    }
}