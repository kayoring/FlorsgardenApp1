package com.calicdan.florsgardenapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.Domain.PurchasesDomain;
import com.calicdan.florsgardenapp.R;

import java.util.ArrayList;

public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.ViewHolder> {
    ArrayList<PurchasesDomain> purchasesDomains;

    public PurchasesAdapter(ArrayList<PurchasesDomain> purchasesDomains){
        this.purchasesDomains = purchasesDomains;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_purchases,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasesAdapter.ViewHolder holder, int position) {
        holder.purchasesName.setText(purchasesDomains.get(position).getTitle());
        String picUrl="";
        switch(position){
            case 0:{
                picUrl="pay";
                holder.purchasesLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.purchasesbg));
                break;
            }
            case 1:{
                picUrl="ship";
                holder.purchasesLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.purchasesbg));
                break;
            }
            case 2:{
                picUrl="receive";
                holder.purchasesLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.purchasesbg));
                break;
            }
            case 3:{
                picUrl="finished";
                holder.purchasesLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.purchasesbg));
                break;
            }
        }
        int drawableResourceId=holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.purchasesPic);
    }

    @Override
    public int getItemCount() {
        return purchasesDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView purchasesName;
        ImageView purchasesPic;
        ConstraintLayout purchasesLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            purchasesName = itemView.findViewById(R.id.purchasesName);
            purchasesPic = itemView.findViewById(R.id.purchasesPic);
            purchasesLayout = itemView.findViewById(R.id.purchasesLayout);
        }
    }



}
