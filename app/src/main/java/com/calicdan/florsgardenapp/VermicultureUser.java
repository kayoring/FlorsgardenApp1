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

public class VermicultureUser extends AppCompatActivity {
    RecyclerView recyclerView;
    VermicultureAdapterUser vermicultureAdapter;
    //Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms_user);


        recyclerView = (RecyclerView)findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Vermiculture"), HomeModel.class)
                        .build();

        vermicultureAdapter = new VermicultureAdapterUser(options);
        recyclerView.setAdapter(vermicultureAdapter);

        /* btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddGuidesVermiculture.class));
            }
        });

*/
    }



    @Override
    protected void onStart() {
        super.onStart();
        vermicultureAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        vermicultureAdapter.stopListening();
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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/Vermiculture").orderByChild("name").startAt(str).endAt(str + "~"), HomeModel.class)
                        .build();

        vermicultureAdapter = new VermicultureAdapterUser(options);
        vermicultureAdapter.startListening();
        recyclerView.setAdapter(vermicultureAdapter);

    }
}

