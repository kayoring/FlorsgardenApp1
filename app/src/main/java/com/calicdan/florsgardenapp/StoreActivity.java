package com.calicdan.florsgardenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.sql.Array;
import java.util.ArrayList;

import Adaptor.CategoryAdaptor;
import Adaptor.ProductsAdaptor;
import Domain.CategoryDomain;
import Domain.ProductsDomain;

public class StoreActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter,adapter1;
    private RecyclerView recyclerViewCategoryList,recyclerViewProductsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        recyclerViewCategory();
        recyclerViewProducts();
    }

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList=findViewById(R.id.recyclerView);
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

        ArrayList<ProductsDomain> productsList = new ArrayList<>();
        productsList.add(new ProductsDomain("Worm 1","worms1","worms number 1",100.00));
        productsList.add(new ProductsDomain("Worm 2","worm2","worms number 2",149.99));
        productsList.add(new ProductsDomain("Worm 3","worm2","worms number 3",89.99));
        productsList.add(new ProductsDomain("Worm 4","worms1","worms number 4",149.99));

        adapter1=new ProductsAdaptor(productsList);
        recyclerViewProductsList.setAdapter(adapter1);
    }
}