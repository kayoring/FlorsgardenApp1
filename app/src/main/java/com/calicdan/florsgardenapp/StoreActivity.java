package com.calicdan.florsgardenapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.Model.Chat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.calicdan.florsgardenapp.Adapter.CategoryAdaptor;
import com.calicdan.florsgardenapp.Adapter.ProductsAdaptor;
import com.calicdan.florsgardenapp.Domain.CategoryDomain;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoreActivity extends AppCompatActivity implements View.OnClickListener{

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;

    private RecyclerView.Adapter adapter,adapter1;
    private RecyclerView recyclerViewCategoryList,recyclerViewProductsList;
    TextView introUser;
    ScrollView storeScrollView;
    String name, email;
    //String userType = "customer";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    private DatabaseReference productsRef = db.getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        homebtn = findViewById(R.id.homebtn);
        forumbtn = findViewById(R.id.forumbtn);
        storebtn = findViewById(R.id.storebtn);
        notificationbtn = findViewById(R.id.notificationbtn);
        chatbtn = findViewById(R.id.chatbtn);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageRecog = findViewById(R.id.imageRecog);

        homebtn.setOnClickListener(this);
        forumbtn.setOnClickListener(this);
        storebtn.setOnClickListener(this);
        notificationbtn.setOnClickListener(this);
        chatbtn.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
        imageRecog.setOnClickListener(this);
/*
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userTypeReference = reference.child("Users").child(uid).child("usertype");
        userTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userType = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userType = "customer";
            }
        });

 */

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
        adapter1 = new ProductsAdaptor(productsList);
        recyclerViewProductsList.setAdapter(adapter1);

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productsList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    FoodDomain foodDomain = postSnapshot.getValue(FoodDomain.class);
                    productsList.add(foodDomain);

                }
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homebtn:
                startActivity(new Intent(StoreActivity.this, HomeUser.class));
                break;
            case R.id.forumbtn:
                startActivity(new Intent(StoreActivity.this, ForumActivity.class));
                break;
            case R.id.storebtn:
                startActivity(new Intent(StoreActivity.this, StoreActivity.class));
                break;
            case R.id.notificationbtn:
                Toast.makeText(this, "Not yet available!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chatbtn:
                startActivity(new Intent(StoreActivity.this, ChatbotActivity.class));
                break;
            case R.id.imageViewProfile:
                startActivity(new Intent(StoreActivity.this, ProfileActivity.class));
                break;
            case R.id.imageRecog:
                startActivity(new Intent(StoreActivity.this, ImageRecognitionHome.class));
                break;
        }
    }
}