package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    TextView txtSeeAll;
    View homebtn, forumbtn, storebtn, notificationbtn, chatbtn, imageViewProfile;
    FloatingActionButton imageRecog;
    Button btnWorms, btnVermicast, btnVermiculture, btnOrganic;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String userType = "customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        btnVermiculture = findViewById(R.id.btnVermiculture);
        btnOrganic = findViewById(R.id.btnOrganic);
        homebtn = findViewById(R.id.homebtn);
        forumbtn = findViewById(R.id.forumbtn);
        storebtn = findViewById(R.id.storebtn);
        imageRecog = findViewById(R.id.imageRecog);
        notificationbtn = findViewById(R.id.notificationbtn);
        chatbtn = findViewById(R.id.chatbtn);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        btnWorms = findViewById(R.id.btnWorms);

        txtSeeAll = findViewById(R.id.txtSeeAll);
        //admin checker
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userTypeReference = reference.child("Users").child(uid).child("usertype");
        userTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userType = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userType = "customer";
            }
        });

        txtSeeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, GuidesCategories.class));

            }
        });
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Home.class));

            }
        });

        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ForumActivity.class));

            }
        });

        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userType.equals("admin")) {
                    startActivity(new Intent(Home.this, AdminStoreActivity.class));
                }
                if (userType.equals("customer")) {
                    startActivity(new Intent(Home.this, StoreActivity.class));
                }

                if (userType.equals("admin")) {
                    startActivity(new Intent(Home.this, AdminStoreActivity.class));
                }
                if (userType.equals("customer")) {
                    startActivity(new Intent(Home.this, StoreActivity.class));
                }

            }
        });

        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ImageRecognitionOrganicWaste.class));
            }
        });
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ChatbotActivity.class));

            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });

        btnWorms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Worms.class));
            }
        });
        Button btnFlorsGarden = (Button) findViewById(R.id.btnFlorsGarden);

        btnFlorsGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_button("https://florsgarden.com");
            }
        });
        btnOrganic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, OrganicWaste.class));
            }
        });
        btnVermiculture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Vermiculture.class));
            }
        });

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, ProfileActivity.class));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(Home.this, ChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    public void clicked_button(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

    }


}
