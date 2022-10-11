package com.calicdan.florsgardenapp;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;

    Button btnWorms, btnVermicast, btnVermiculture, btnOrganic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Home.class));

            }
        });

        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });

        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });

        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ImageRecognition.class));
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
        Button btnFlorsGarden = (Button)findViewById(R.id.btnFlorsGarden);

        btnFlorsGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_button("https://florsgarden.com");
        }
        });
        btnOrganic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this,OrganicWaste.class));
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
        public void clicked_button(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);


    }
}
