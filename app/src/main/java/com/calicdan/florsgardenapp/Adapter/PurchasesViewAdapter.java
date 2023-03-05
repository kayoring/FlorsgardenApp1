package com.calicdan.florsgardenapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.AdminForumActivity;
import com.calicdan.florsgardenapp.AdminProfileActivity;
import com.calicdan.florsgardenapp.AdminStoreActivity;
import com.calicdan.florsgardenapp.ChatActivity;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.calicdan.florsgardenapp.InstructionHowToPay;
import com.calicdan.florsgardenapp.PurchasesView;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.StoreActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PurchasesViewAdapter extends RecyclerView.Adapter<PurchasesViewAdapter.ViewHolder> {

    ArrayList<FoodDomain> foodDomain;
    String temp,retProductName,changeStat,userAddress,UID,username;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");
    DatabaseReference ordersRef = fdb.getReference("Orders");


    public PurchasesViewAdapter(ArrayList<FoodDomain> foodDomains) { this.foodDomain = foodDomains;}

    @NonNull
    @Override
    public PurchasesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_purchasesview,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

            }
        });

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

            case "ToPay":
                holder.purchasesButton.setText("Pay for Order");
                changeStat="Paid";
                holder.purchasesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderStatusRef.setValue(changeStat);
                        Intent intent = new Intent(holder.purchasesButton.getContext(), ChatActivity.class);
                        holder.purchasesButton.getContext().startActivity(intent);
                    }
                });
                break;
            case "ToShip":
                holder.purchasesButton.setText("Confirm Shipment");
                changeStat="ToReceive";
                break;
            case "ToReceive":
                holder.purchasesButton.setText("Order Received");
                changeStat="Completed";
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
