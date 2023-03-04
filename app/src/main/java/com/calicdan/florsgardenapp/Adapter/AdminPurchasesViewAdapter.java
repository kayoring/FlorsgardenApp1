package com.calicdan.florsgardenapp.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.AdminPurchasesDetails;
import com.calicdan.florsgardenapp.AdminPurchasesView;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.WormsCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminPurchasesViewAdapter extends RecyclerView.Adapter<AdminPurchasesViewAdapter.ViewHolder> {

    ArrayList<FoodDomain> foodDomain;
    String temp,retProductName,changeStat,userAddress,UID,username;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");
    DatabaseReference ordersRef = fdb.getReference("Orders");


    public AdminPurchasesViewAdapter(ArrayList<FoodDomain> foodDomains) { this.foodDomain = foodDomains;}

    @NonNull
    @Override
    public AdminPurchasesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_purchasesview,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPurchasesViewAdapter.ViewHolder holder, int position) {

        holder.totalFee.setText(Double.toString((foodDomain.get(position).getTotal())));
        holder.order_status.setText(foodDomain.get(position).getStatus());
        DatabaseReference orderStatusRef = ordersRef.child(foodDomain.get(position).getUid()).child("status");
        UID = foodDomain.get(position).getUid();
        DatabaseReference addressREF = fdb.getReference("Users").child(UID).child("address");
        addressREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAddress = dataSnapshot.getValue(String.class);
                holder.userID.setText(userAddress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.i(TAG,"------------------------DID NOT GET ADDRESS");
            }
        });
        Log.i(TAG,"hello-----------------" + userAddress);

        DatabaseReference usernameREF = fdb.getReference("Users").child(UID).child("username");
        usernameREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                holder.titleTxt.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        switch(foodDomain.get(position).getStatus()){
            case "Pending":
                holder.purchasesButton.setText("Accept Order");
                changeStat="ToPay";
                holder.purchasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderStatusRef.setValue(changeStat);
                    }
                });
                break;
            case "Paid":
                holder.purchasesButton.setText("Ship Order");
                changeStat="ToShip";
                holder.purchasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        orderStatusRef.setValue(changeStat);
                    }
                });
                break;
            case "ToShip":
                holder.purchasesButton.setText("Confirm shipment");
                changeStat="ToReceive";
                holder.purchasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        orderStatusRef.setValue(changeStat);
                    }
                });
                break;
            case "Completed":
                holder.purchasesButton.setText("Completed Order");

                break;

            default:
                holder.purchasesButton.setText("");
                break;
        }

        holder.purchasesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), AdminPurchasesDetails.class);
                intent.putExtra("STATUS", foodDomain.get(position).getStatus());
                intent.putExtra("UID",foodDomain.get(position).getUid());
                holder.itemView.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return foodDomain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, totalFee, userID,order_status,purchasesButton;
        View purchasesView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            totalFee = itemView.findViewById(R.id.totalFee);
            userID = itemView.findViewById(R.id.userID);
            order_status = itemView.findViewById(R.id.order_status);
            purchasesView = itemView.findViewById(R.id.purchasesView);
            purchasesButton = itemView.findViewById(R.id.purchasesButton);
        }
    }
}
