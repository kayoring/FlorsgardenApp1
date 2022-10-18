package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.calicdan.florsgardenapp.Adapter.CategoryAdaptor;
import com.calicdan.florsgardenapp.Adapter.ProductsAdaptor;
import com.calicdan.florsgardenapp.Domain.CategoryDomain;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoreActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter,adapter1;
    private RecyclerView recyclerViewCategoryList,recyclerViewProductsList;
    TextView introUser;
    ScrollView storeScrollView;
    String name, email,uid;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference fdb = db.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
        }
        introUser = findViewById(R.id.introUser);
        introUser.setText("Hi " + email);

        recyclerViewCategory();
        recyclerViewProducts();
        navigation();
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
                startActivity(new Intent(StoreActivity.this, Home.class));
            }
        });
        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, StoreActivity.class));
            }
        });
        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, StoreActivity.class));
            }
        });
        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreActivity.this, ImageRecognition.class));
            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, StoreActivity.class));

            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, ProfileActivity.class));

            }
        });
    }
    private void navigation(){
        View cartbtn = findViewById(R.id.cartBtn);
        cartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StoreActivity.this,CartListActivity.class));

            }
        });

    }

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList=findViewById(R.id.recycleViewWorms);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Vermikits","worm"));
        category.add(new CategoryDomain("Vermicast","fertilizer"));

        adapter=new CategoryAdaptor(category);
        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void recyclerViewProducts(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewProductsList = findViewById(R.id.recyclerView2);
        recyclerViewProductsList.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> productsList = new ArrayList<>();
        productsList.add(new FoodDomain("Red Worms","worms1","worms number 1",100.00, 0));
        productsList.add(new FoodDomain("Grey Worms","worm2","worms number 2",149.99, 0));
        productsList.add(new FoodDomain("Another Grey Worms","worm2","worms number 3",89.99, 0));
        productsList.add(new FoodDomain("Another Red Worms","worms1","worms number 4",149.99, 0));

        adapter1=new ProductsAdaptor(productsList);
        recyclerViewProductsList.setAdapter(adapter1);


        ImageView imageViewHome = (ImageView) findViewById(R.id. imageViewHome);

        imageViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoreActivity.this, Home.class));

            }
        });

        FloatingActionButton imageRecog = (FloatingActionButton)findViewById(R.id.imageRecog);

        imageRecog.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                startActivity(new Intent(StoreActivity.this, ImageRecognition.class));
            }
        });
    }
}