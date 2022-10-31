package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageRecognitionHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recognition_home);


        ImageView imgOrganicWaste, imgWorms;

        imgOrganicWaste = findViewById(R.id.imgOrganicWaste);
        imgWorms = findViewById(R.id.imgWorms);

        imgOrganicWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageRecognitionHome.this, ImageRecognitionOrganicWaste.class));


            }
        });

        imgWorms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ImageRecognitionHome.this, ImageRecognitionWorm.class));
            }
        });

    }
}