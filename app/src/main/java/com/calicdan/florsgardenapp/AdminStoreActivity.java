package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.calicdan.florsgardenapp.Adapter.AdminProductsAdapter;
import com.calicdan.florsgardenapp.Domain.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminStoreActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView adminProductsView;
    View addProductBtn;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference productReference = db.getReference().child("Products");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_store_activity);

        initView();
        initList();
        AdminProductsView();
    }

    private void initView(){
        addProductBtn = findViewById(R.id.addProductBtn);

    }

    private void initList(){
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminStoreActivity.this,AddProduct.class));
            }
        });
    }

    private void AdminProductsView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adminProductsView = findViewById(R.id.adminProductsView);
        adminProductsView.setLayoutManager(linearLayoutManager);
        ArrayList<Product> productArrayList = new ArrayList<>();
        adapter = new AdminProductsAdapter(productArrayList);
        adminProductsView.setAdapter(adapter);

        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Product prod = dataSnapshot.getValue(Product.class);
                    productArrayList.add(prod);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}