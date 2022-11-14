package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Worms extends AppCompatActivity {
    RecyclerView recyclerView;
    WormsAndOrganicAdapter wormsAdapter;
    Button btnAdd;
    ImageView imgViewBack4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms);


        recyclerView = (RecyclerView)findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Worms"), HomeModel.class)
                        .build();

        wormsAdapter = new WormsAndOrganicAdapter(options);
        recyclerView.setAdapter(wormsAdapter);

        btnAdd = findViewById(R.id.btnAdd);
        imgViewBack4 = findViewById(R.id.imgViewBack4);

        imgViewBack4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Worms.this, Home.class));
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddGuidesWorms.class));
            }
        });


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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Worms").orderByChild("name").startAt(str).endAt(str + "~"), HomeModel.class)
                        .build();

        wormsAdapter = new WormsAndOrganicAdapter(options);
        wormsAdapter.startListening();
        recyclerView.setAdapter(wormsAdapter);



    }
        }



