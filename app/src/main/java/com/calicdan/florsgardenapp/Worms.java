package com.calicdan.florsgardenapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Worms extends AppCompatActivity {
    RecyclerView recyclerView;
    HomeAdapter HomeAdapter;
    DatabaseReference database;
    //HomeAdapter wormAdapter;
    ArrayList<HomeModel> wormList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms);

        recyclerView = findViewById(R.id.recycleViewWorms);
        database = FirebaseDatabase.getInstance().getReference("Worms");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        wormList = new ArrayList<>();

        HomeAdapter = new HomeAdapter(this, wormList);
        recyclerView.setAdapter(HomeAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    HomeModel HomeModel = dataSnapshot.getValue(HomeModel.class);
                    wormList.add(HomeModel);
                }
                HomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
