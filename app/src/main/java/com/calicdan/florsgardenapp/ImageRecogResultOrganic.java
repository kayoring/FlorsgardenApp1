package com.calicdan.florsgardenapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ImageRecogResultOrganic extends ImageRecognitionOrganicWaste {
    RecyclerView recyclerView;
    WormsAdapter wormsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_recog_result);
        //TextView txtDesc1 = (TextView) findViewById(R.id.txtDesc1);
        //txtDesc1.setText(getIntent().getExtras().getString("result"));
        TextView txtDesc1 = (TextView) findViewById(R.id.txtDesc1);

        String s = getIntent().getStringExtra("result");

        txtDesc1.setText(s);

        String res = txtDesc1.getText().toString();

        recyclerView = findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/OrganicWaste/"), HomeModel.class)
                        .build();

        wormsAdapter = new WormsAdapter(options);
        wormsAdapter.startListening();
        recyclerView.setAdapter(wormsAdapter);

    }

    }

