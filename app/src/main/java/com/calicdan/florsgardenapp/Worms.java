package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,profilebtn;
    FloatingActionButton imageRecog;

    Button btnWorms;
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


        homebtn = findViewById(R.id.homebtn);
        forumbtn = findViewById(R.id.forumbtn);
        storebtn = findViewById(R.id.storebtn);

        imageRecog = findViewById(R.id.imageRecog);
        notificationbtn = findViewById(R.id.notificationbtn);
        chatbtn = findViewById(R.id.chatbtn);
        profilebtn = findViewById(R.id.profilebtn);

        btnWorms = findViewById(R.id.btnWorms);

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
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, Home.class));

            }
        });

        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, StoreActivity.class));

            }
        });

        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, StoreActivity.class));

            }
        });

        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Worms.this, ImageRecognition.class));
            }
        });
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, ChatActivity.class));

            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Worms.this, StoreActivity.class));

            }
        });


    }


}
