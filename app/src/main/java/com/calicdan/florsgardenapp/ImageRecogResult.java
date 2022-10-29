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
        //TextView txtDesc1 = (TextView) findViewById(R.id.txtDesc1);
        //txtDesc1.setText(getIntent().getExtras().getString("result"));
        TextView txtDesc1 = (TextView)findViewById(R.id.txtDesc1);

        String s = getIntent().getStringExtra("result");
        txtDesc1.setText(s);
        
        recyclerView = findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/OrganicWaste/" + s), HomeModel.class)
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

