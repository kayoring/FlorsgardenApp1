package com.calicdan.florsgardenapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;




public class Worms extends AppCompatActivity {
    RecyclerView recyclerView;
    HomeAdapter HomeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms);


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