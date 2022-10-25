package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuidesCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_categories);

        ImageView imgBack;
        TextView txtVermi, txtWorm, txtFlors, txtVids, txtOrganic;

        imgBack = findViewById(R.id.imgBack);
        txtVermi = findViewById(R.id.txtVermi);
        txtWorm = findViewById(R.id.txtWorm);
        txtFlors = findViewById(R.id.txtFlors);
        txtOrganic = findViewById(R.id.txtOrganic);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuidesCategories.this, Home.class));

            }
        });
        txtVermi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(GuidesCategories.this, Vermiculture.class));
            }
        });
        txtWorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(GuidesCategories.this, Worms.class));
            }
        });
        txtOrganic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(GuidesCategories.this, OrganicWaste.class));
            }
        });



    }
}