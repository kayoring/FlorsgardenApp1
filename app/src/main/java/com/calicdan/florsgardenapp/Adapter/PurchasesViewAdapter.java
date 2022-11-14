package com.calicdan.florsgardenapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.calicdan.florsgardenapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PurchasesViewAdapter extends RecyclerView.Adapter<PurchasesViewAdapter.ViewHolder> {

    ArrayList<FoodDomain> foodDomain;
    String temp,retProductName,changeStat;
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
    public void onBindViewHolder(@NonNull PurchasesViewAdapter.ViewHolder holder, int position) {
        holder.titleTxt.setText(foodDomain.get(position).getDate());
        holder.totalFee.setText(Double.toString((foodDomain.get(position).getTotal())));
        holder.userID.setText(foodDomain.get(position).getUid());
        holder.order_status.setText(foodDomain.get(position).getStatus());
        DatabaseReference orderStatusRef = ordersRef.child(foodDomain.get(position).getUid()).child("status");

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
                break;
            case "Shipped":
                holder.purchasesButton.setText("Confirm shipment");
                changeStat="ToRecieve";
                break;
            case "Completed":
                holder.purchasesButton.setText("Completed Order");
                break;

            case "ToPay":
                holder.purchasesButton.setText("Pay for Order");
                changeStat="Paid";
                break;
            case "ToShip":
                holder.purchasesButton.setText("Confirm Shipment");
                changeStat="Shipped";
                break;
            case "ToReceive":
                holder.purchasesButton.setText("Received Order");
                changeStat="Completed";
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
