package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

    public class ImageRecogResultWorm extends ImageRecognitionWorm {
        RecyclerView recyclerView;
        ImageRecogResultAdapter wormsAdapter;
       ImageView imgViewBack3;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_image_recog_result);
            //TextView txtDesc1 = (TextView) findViewById(R.id.txtDesc1);
            //txtDesc1.setText(getIntent().getExtras().getString("result"));
            TextView txtDesc1 = (TextView) findViewById(R.id.txtDesc1);

            String s = getIntent().getStringExtra("result");

            txtDesc1.setText(s);

            //String res = txtDesc1.getText().toString();

            recyclerView = findViewById(R.id.recycleViewWorms);
            imgViewBack3 = findViewById(R.id.imgViewBack3);

            imgViewBack3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ImageRecogResultWorm.this, Home.class));
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            FirebaseRecyclerOptions<HomeModel> options =
                    new FirebaseRecyclerOptions.Builder<HomeModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Worms").orderByChild("name").equalTo(s), HomeModel.class)
                            .build();

            wormsAdapter = new ImageRecogResultAdapter(options);
            wormsAdapter.startListening();
            recyclerView.setAdapter(wormsAdapter);

        }

    }

