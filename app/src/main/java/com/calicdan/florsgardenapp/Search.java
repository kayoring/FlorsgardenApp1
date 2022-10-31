package com.calicdan.florsgardenapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Search extends AppCompatActivity {
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");


        recyclerView = findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Worms"), HomeModel.class)
                        .build();
        searchAdapter = new SearchAdapter(options);
        recyclerView.setAdapter(searchAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        searchAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        searchAdapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchview = (SearchView)item.getActionView();

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Worms").orderByChild("name").startAt(str).endAt(str + "~"), HomeModel.class)
                        .build();

        searchAdapter = new SearchAdapter(options);
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter);

    }
}








