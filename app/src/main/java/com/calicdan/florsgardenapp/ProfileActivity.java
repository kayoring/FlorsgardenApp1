package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.Adaptor.CategoryAdaptor;
import com.calicdan.florsgardenapp.Adaptor.PurchasesAdapter;
import com.calicdan.florsgardenapp.Domain.CategoryDomain;
import com.calicdan.florsgardenapp.Domain.PurchasesDomain;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewPurchasesList;
    View settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        recyclerViewPurchases();
        settings = findViewById(R.id.settings);
        settings .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileSettings.class));

            }
        });

    }
    private void recyclerViewPurchases(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPurchasesList=findViewById(R.id.recycleViewPurchases);
        recyclerViewPurchasesList.setLayoutManager(linearLayoutManager);

        ArrayList<PurchasesDomain> purchase = new ArrayList<>();
        purchase.add(new PurchasesDomain("To Pay","pay"));
        purchase.add(new PurchasesDomain("To Ship","ship"));
        purchase.add(new PurchasesDomain("To Receive","receive"));
        purchase.add(new PurchasesDomain("Completed","finished"));

        adapter=new PurchasesAdapter(purchase);
        recyclerViewPurchasesList.setAdapter(adapter);
    }
}