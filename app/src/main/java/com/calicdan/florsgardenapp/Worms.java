package com.calicdan.florsgardenapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Worms extends AppCompatActivity {
    RecyclerView recyclerView;
    WormsAdapter wormsAdapater;
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

        wormsAdapater = new WormsAdapter(options);
        recyclerView.setAdapter(wormsAdapater);



    }

    @Override
    protected void onStart() {
        super.onStart();
        wormsAdapater.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        wormsAdapater.stopListening();
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Worms").orderByChild("name").startAt(str + "~"), HomeModel.class)
                        .build();

        wormsAdapater = new WormsAdapter(options);
        wormsAdapater.startListening();
        recyclerView.setAdapter(wormsAdapater);



    }
        }



