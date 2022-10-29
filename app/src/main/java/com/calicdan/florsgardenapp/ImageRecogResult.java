package com.calicdan.florsgardenapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ImageRecogResult extends ImageRecognitionOrganicWaste {
    RecyclerView recyclerView;
    WormsAdapter wormsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recog_result);
        TextView tv = (TextView) findViewById(R.id.txtDesc);
        tv.setText(getIntent().getExtras().getString("result"));


        recyclerView = findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/OrganicWaste/" + tv), HomeModel.class)
                        .build();

        wormsAdapter = new WormsAdapter(options);
        recyclerView.setAdapter(wormsAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        wormsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wormsAdapter.stopListening();
    }

    }

