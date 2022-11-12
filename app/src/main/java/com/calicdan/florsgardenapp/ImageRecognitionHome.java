package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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