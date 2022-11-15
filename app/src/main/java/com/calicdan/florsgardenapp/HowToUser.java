package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HowToUser extends AppCompatActivity {

    RecyclerView recyclerView;
    HowToAdapterUser wormsAdapter;
    //Button btnAdd;
    ImageView imgViewBack4, imageRecog, imageViewHome, imageViewForum, imageViewStore, imageViewChat, imageViewProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_user);

        recyclerView = (RecyclerView)findViewById(R.id.recycleViewWorms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HomeModel> options =
                new FirebaseRecyclerOptions.Builder<HomeModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/How"), HomeModel.class)
                        .build();

        wormsAdapter = new HowToAdapterUser(options);
        recyclerView.setAdapter(wormsAdapter);

        //btnAdd = findViewById(R.id.btnAdd);

        imgViewBack4 = findViewById(R.id.imgViewBack4);
        imageRecog = findViewById(R.id.imageRecog);
        imageViewHome = findViewById(R.id.imageViewHome);
        imageViewForum = findViewById(R.id.imageViewForum);
        imageViewStore = findViewById(R.id.imageViewStore);
        imageViewChat = findViewById(R.id.imageViewChat);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowToUser.this, ProfileActivity.class));
            }
        });

        imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowToUser.this, ChatbotActivity.class));
            }
        });

        imageViewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowToUser.this, StoreActivity.class));
            }
        });

        imageViewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowToUser.this, ForumActivity.class));
            }
        });

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowToUser.this, HomeUser.class));
            }
        });

        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HowToUser.this, ImageRecognitionHome.class));
            }
        });

        /*
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HowTo.this, AddGuidesHow.class));
            }
        });
*/

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
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Guides/How").orderByChild("name").startAt(str).endAt(str + "~"), HomeModel.class)
                        .build();

        wormsAdapter = new HowToAdapterUser(options);
        wormsAdapter.startListening();
        recyclerView.setAdapter(wormsAdapter);



    }
}
