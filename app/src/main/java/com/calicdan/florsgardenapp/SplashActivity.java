package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIMER1 = 1000;
    private static int SPLASH_TIMER = 3000;
    private static int SPLASH_TIMER0 = 0000;
    ImageView imgSplash;
    TextView txtSplash;
    Animation sideAnim, bottomAnim;
    private FirebaseUser firebaseUser;
    String uid;

    FirebaseUser fuser;
    DatabaseReference ref;
    String userType= "";

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            uid = firebaseUser.getUid();

            fuser = FirebaseAuth.getInstance().getCurrentUser();
            ref = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userType = snapshot.getValue(String.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SplashActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(userType.equals("admin")){
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                        finish();
                    }else if(userType.equals("customer")){
                        Intent intent = new Intent(getApplicationContext(),HomeUser.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_TIMER1);
        } else if (firebaseUser == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIMER);
        } else if (firebaseUser == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(userType.equals("admin")){
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                        finish();
                    }else if(userType.equals("customer")){
                        Intent intent = new Intent(getApplicationContext(),HomeUser.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_TIMER0);
        }
        else {
            Toast.makeText(this, "Something went wrong! try again...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgSplash = findViewById(R.id.imgSplash);

        sideAnim = AnimationUtils.loadAnimation(this,R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_anim);

        imgSplash.setAnimation(sideAnim);

    }
}