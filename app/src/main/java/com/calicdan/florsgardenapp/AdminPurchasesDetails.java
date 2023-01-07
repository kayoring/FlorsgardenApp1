package com.calicdan.florsgardenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdminPurchasesDetails extends AppCompatActivity {
    private TextView actionbutton;
    String changeStat;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");
    DatabaseReference ordersRef = fdb.getReference("Orders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_purchases_details);
        String order_Status = getIntent().getStringExtra("STATUS");
        String UID = getIntent().getStringExtra("UID");

        DatabaseReference orderStatusRef = ordersRef.child("UID").child("status");
        actionbutton = findViewById(R.id.actionButton);
        switch(order_Status){
            case "Pending":
                actionbutton.setText("Accept Order");
                changeStat="ToPay";
                actionbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderStatusRef.setValue(changeStat);
                    }
                });
                break;
            case "Paid":
                actionbutton.setText("Ship Order");
                changeStat="ToShip";
                actionbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        orderStatusRef.setValue(changeStat);
                    }
                });
                break;
            case "Shipped":
                actionbutton.setText("Confirm shipment");
                changeStat="ToReceive";
                actionbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        orderStatusRef.setValue(changeStat);
                    }
                });
                break;
            case "Completed":
                actionbutton.setText("Completed Order");

                break;

            default:
                actionbutton.setText("");
                break;
        }
    }
}