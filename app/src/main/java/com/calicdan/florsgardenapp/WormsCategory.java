package com.calicdan.florsgardenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.calicdan.florsgardenapp.Adapter.ProductsAdaptor;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WormsCategory extends AppCompatActivity {
    private RecyclerView recyclerViewWorms;
    private RecyclerView.Adapter adapter1;
    TextView introUser2;
    String name, email,uid;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference fdb = db.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worms_category);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
        }
        introUser2 = findViewById(R.id.introUser2);
        introUser2.setText("Hi " + email);
        recyclerViewProducts();
        buttons();
    }
    private void buttons() {
        View homebtn = findViewById(R.id.homebtn);
        View forumbtn = findViewById(R.id.forumbtn);
        View storebtn = findViewById(R.id.storebtn);

        FloatingActionButton imageRecog = (FloatingActionButton) findViewById(R.id.imageRecog);
        View notificationbtn = findViewById(R.id.notificationbtn);
        View chatbtn = findViewById(R.id.chatbtn);
        View profilebtn = findViewById(R.id.profilebtn);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WormsCategory.this, Home.class));
            }
        });
        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WormsCategory.this, StoreActivity.class));
            }
        });
        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WormsCategory.this, StoreActivity.class));
            }
        });
        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WormsCategory.this, ImageRecognitionOrganicWaste.class));
            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WormsCategory.this, StoreActivity.class));

            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WormsCategory.this, ProfileActivity.class));

            }
        });

    }

    private void recyclerViewProducts() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewWorms = findViewById(R.id.recyclerViewWorms);
        recyclerViewWorms.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> productsList = new ArrayList<>();
        productsList.add(new FoodDomain("Red Worms", "worms1", "worms number 1", 100.00, 0));
        productsList.add(new FoodDomain("Grey Worms", "worm2", "worms number 2", 149.99, 0));
        productsList.add(new FoodDomain("Another Grey Worms", "worm2", "worms number 3", 89.99, 0));
        productsList.add(new FoodDomain("Another Red Worms", "worms1", "worms number 4", 149.99, 0));

        adapter1 = new ProductsAdaptor(productsList);
        recyclerViewWorms.setAdapter(adapter1);
    }
}