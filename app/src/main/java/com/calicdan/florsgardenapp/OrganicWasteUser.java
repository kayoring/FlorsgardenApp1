package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OrganicWasteUser extends AppCompatActivity  {
    RecyclerView recyclerView;
    OrganicWasteAdapterUser organicWasteAdapter;
            //Button btnAdd;
            ImageView imgViewBack4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms_user);


        recyclerView = findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //btnAdd = findViewById(R.id.btnAdd);

        imgViewBack4 = findViewById(R.id.imgViewBack4);
        /*btnAdd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), AddGuidesOrganicWaste.class));
        }
        });
*/
        imgViewBack4.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(OrganicWasteUser.this, HomeUser.class));
        }
        });

        FirebaseRecyclerOptions<HomeModel> options =
        new FirebaseRecyclerOptions.Builder<HomeModel>()
        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/OrganicWaste"), HomeModel.class)
        .build();

        organicWasteAdapter = new OrganicWasteAdapterUser(options);
        recyclerView.setAdapter(organicWasteAdapter);

        }


    @Override
    protected void onStart() {
        super.onStart();
        organicWasteAdapter.startListening();
        }

    @Override
    protected void onStop() {
        super.onStop();
        organicWasteAdapter.stopListening();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchview = (SearchView) item.getActionView();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
    @Override
    public boolean onQueryTextSubmit(String query){
        txtSearch(query);
        return false;
        }
    @Override
    public boolean onQueryTextChange(String query){
        txtSearch(query);
        return false;
        }
        });

        return super.onCreateOptionsMenu(menu);
        }


    private void txtSearch(String str) {
        FirebaseRecyclerOptions<HomeModel> options =
        new FirebaseRecyclerOptions.Builder<HomeModel>()
        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/OrganicWaste").orderByChild("name").startAt(str + "~"), HomeModel.class)
        .build();

        organicWasteAdapter = new OrganicWasteAdapterUser(options);
        organicWasteAdapter.startListening();
        recyclerView.setAdapter(organicWasteAdapter);

        }
        }
