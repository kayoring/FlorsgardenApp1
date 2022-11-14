package com.calicdan.florsgardenapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.Adapter.AdminPurchasesViewAdapter;
import com.calicdan.florsgardenapp.Adapter.PurchasesViewAdapter;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.calicdan.florsgardenapp.Domain.PurchasesDomain;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPurchasesView extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    TextView totalFeeTxt, taxTxt, totalTxt,emptyTxt,purchasesViewTitle;
    private ScrollView scrollView;
    String uid,email;
    private PurchasesDomain object;
    String view;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference fdb = db.getReference().child("Orders");
    private DatabaseReference uidCartRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases_view);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            email = user.getEmail();
        }
        uidCartRef = fdb.child(uid);
        initView();
        initList();
    }
    private void initView(){
        recyclerViewList=findViewById(R.id.recycleViewWorms);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView3);
        recyclerViewList = findViewById(R.id.cartView);
        purchasesViewTitle = findViewById(R.id.purchasesViewTitle);
    }

    private void initList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewList = findViewById(R.id.cartView);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        ArrayList<FoodDomain> purchasesList = new ArrayList<>();
        adapter = new AdminPurchasesViewAdapter(purchasesList);
        recyclerViewList.setAdapter(adapter);
        object = (PurchasesDomain) getIntent().getSerializableExtra("object");
        view = (object.getTitle()).replace(" ","");
        Log.i(TAG,"-----------------------------" + view);
        purchasesViewTitle.setText(object.getTitle());

        fdb.orderByChild("status").equalTo(view).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                purchasesList.clear();
                Log.i(TAG, "--------------------ADDED TO PURCHASESzxczcz---------------------" + uid);
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    FoodDomain foodDomain = postSnapshot.getValue(FoodDomain.class);
                    Log.i(TAG, "--------------------ADDED TO PURCHASES---------------------" + postSnapshot.child("uid").getValue());
                    if (((postSnapshot.child("uid").getValue()).toString()).equals(uid)) {
                        purchasesList.add(foodDomain);
                        Log.i(TAG, "---------------------ADDED TO THE FUCKING VIEW-----------------------");
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                startActivity(new Intent(AdminPurchasesView.this, Home.class));
            }
        });
        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPurchasesView.this, StoreActivity.class));
            }
        });
        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPurchasesView.this, StoreActivity.class));
            }
        });
        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminPurchasesView.this, ImageRecognitionOrganicWaste.class));
            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPurchasesView.this, StoreActivity.class));

            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPurchasesView.this, ProfileActivity.class));

            }
        });

    }
}

