package com.calicdan.florsgardenapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

    DatabaseReference reference;
    FirebaseUser fuser;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String userType = "customer";

    private static int SPLASH_TIMER = 400000000;

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

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

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
                startActivity(new Intent(Home.this, ImageRecognitionHome.class));
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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Would you like to logout?");
        builder.setTitle("Logout?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(Home.this);
            builder2.setMessage("Are you sure? pressing yes will sign you out and return to login page.");
            builder2.setTitle("Logout?");
            builder2.setCancelable(false);

            builder2.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog2, which2) -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Login.class));
                finish();
            });

            builder2.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog2, which2) -> {
                dialog2.cancel();
            });

            AlertDialog alertDialog = builder2.create();
            alertDialog.show();
        });

        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return;
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
                startActivity(new Intent(Home.this, Worms.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
