package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuidesCategoriesUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_categories);

        ImageView imgBack;
        TextView txtVermi, txtWorm, txtFlors, txtVids, txtOrganic, txtHow;

        imgBack = findViewById(R.id.imgBack);
        txtVermi = findViewById(R.id.txtVermi);
        txtWorm = findViewById(R.id.txtWorm);
        txtFlors = findViewById(R.id.txtFlors);
        txtOrganic = findViewById(R.id.txtOrganic);
        txtHow = findViewById(R.id.txtHow);
        txtVids = findViewById(R.id.txtVids);

        txtVids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuidesCategoriesUser.this, VideosActivityUser.class));
            }
        });

        txtHow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuidesCategoriesUser.this, HowToUser.class));
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuidesCategoriesUser.this, HomeUser.class));

            }
        });
        txtVermi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(GuidesCategoriesUser.this, VermicultureUser.class));
            }
        });
        txtWorm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(GuidesCategoriesUser.this, WormsUser.class));
            }
        });
        txtOrganic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(GuidesCategoriesUser.this, OrganicWasteUser.class));
            }
        });



    }
}