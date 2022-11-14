package com.calicdan.florsgardenapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.calicdan.florsgardenapp.Helper.TinyDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.calicdan.florsgardenapp.Adapter.CartListAdaptor;
import com.calicdan.florsgardenapp.Helper.ManagementCart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Interface.ChangeNumberProductsListener;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    TextView totalFeeTxt, taxTxt, totalTxt,emptyTxt,checkOutBtn;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference fdb = db.getReference().child("Orders");
    String uid,email,productID,phone;
    private double tax, itemTotal, total;
    private ScrollView scrollView;
    private TinyDB tinyDB;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        managementCart = new ManagementCart(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            email = user.getEmail();
        }

        initView();
        initList();
        CalculateCart();
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
                startActivity(new Intent(CartListActivity.this, Home.class));
            }
        });
        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this, StoreActivity.class));
            }
        });
        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this, StoreActivity.class));
            }
        });
        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartListActivity.this, ImageRecognitionOrganicWaste.class));
            }
        });
        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this, StoreActivity.class));

            }
        });
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this, ProfileActivity.class));

            }
        });

    }

    private void initView() {
        recyclerViewList=findViewById(R.id.recycleViewWorms);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        totalTxt = findViewById(R.id.totalTxt);
        emptyTxt = findViewById(R.id.emptyTxt);
        scrollView = findViewById(R.id.scrollView3);
        recyclerViewList = findViewById(R.id.cartView);
        checkOutBtn = findViewById(R.id.checkOutBtn);

    }
    private void initList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdaptor(managementCart.getListCart(),this, new ChangeNumberProductsListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if(managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);

        }else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("MMddyyyyHH");
                date = dateFormat.format(calendar.getTime());
                fdb.child(uid).child("status").setValue("Pending");
                fdb.child(uid).child("itemTotal").setValue(itemTotal);
                fdb.child(uid).child("tax").setValue(tax);
                fdb.child(uid).child("total").setValue(total);
                fdb.child(uid).child("date").setValue(date);
                fdb.child(uid).child("uid").setValue(uid);

                Intent i = new Intent(CartListActivity.this,checkOutActivity.class);
                CartListActivity.this.startActivity(i);
            }
        });
    }
    public ArrayList<FoodDomain> getListCart(){
        return tinyDB.getListObject("CartList");
    }
    private void CalculateCart(){
        double percentTax = 0.12;
        tax = Math.round((managementCart.getTotalfee()*percentTax) * 100)/100;
        total = Math.round((managementCart.getTotalfee()+tax)*100)/100;
        itemTotal = Math.round(managementCart.getTotalfee()*100)/100;

        totalFeeTxt.setText("₱"+itemTotal);
        taxTxt.setText("₱"+tax);
        totalTxt.setText("₱"+total);
    }
}