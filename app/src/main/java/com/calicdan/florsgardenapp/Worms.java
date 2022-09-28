package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;




public class Worms extends AppCompatActivity {
    RecyclerView recyclerView;
    HomeAdapter HomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms);
        ImageView imageViewHome = (ImageView) findViewById(R.id. imageViewHome);

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, Home.class));

            }
        });
        ImageView imageViewStore = (ImageView)findViewById(R.id.imageViewStore);

        imageViewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, StoreActivity.class));

            }
        });
        FloatingActionButton imageRecog = (FloatingActionButton)findViewById(R.id.imageRecog);

        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(Worms.this, ImageRecognition.class));
            }


        }
        );
        recyclerView = (RecyclerView)findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Worms"), HomeModel.class)
                        .build();

        HomeAdapter = new HomeAdapter(options);
        recyclerView.setAdapter(HomeAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        HomeAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        HomeAdapter.stopListening();

    }
}