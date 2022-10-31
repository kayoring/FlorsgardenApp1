package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OrganicWaste extends AppCompatActivity {
    RecyclerView recyclerView;
    OrganicWasteAdapter organicWasteAdapter;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms);


        recyclerView = findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddGuidesOrganicWaste.class));
            }
        });


        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/OrganicWaste"), HomeModel.class)
                        .build();

        organicWasteAdapter = new OrganicWasteAdapter(options);
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

        organicWasteAdapter = new OrganicWasteAdapter(options);
        organicWasteAdapter.startListening();
        recyclerView.setAdapter(organicWasteAdapter);



    }
}
