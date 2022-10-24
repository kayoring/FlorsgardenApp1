package com.calicdan.florsgardenapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.ShowDetailsActivity;

import java.util.ArrayList;

import com.calicdan.florsgardenapp.Domain.FoodDomain;

public class ProductsAdaptor extends RecyclerView.Adapter<ProductsAdaptor.ViewHolder> {
    ArrayList<FoodDomain> foodDomain;

    public ProductsAdaptor(ArrayList<FoodDomain> foodDomains) {
        this.foodDomain = foodDomains;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_products,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsAdaptor.ViewHolder holder, int position) {
    holder.title.setText(foodDomain.get(position).getTitle());
    holder.fee.setText(String.valueOf(foodDomain.get(position).getFee()));

     int drawableResourceId=holder.itemView.getContext().getResources().getIdentifier(foodDomain.get(position).getPic(),"drawable",holder.itemView.getContext().getPackageName());

    Glide.with(holder.itemView.getContext())
            .load(drawableResourceId)
            .into(holder.pic);

    holder.addBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(holder.itemView.getContext(), ShowDetailsActivity.class);
            intent.putExtra("object", foodDomain.get(position));
            holder.itemView.getContext().startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return foodDomain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,fee;
        ImageView pic;
        TextView addBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.productTitle);
            fee = itemView.findViewById(R.id.fee);
            pic = itemView.findViewById(R.id.productPic);
            addBtn = itemView.findViewById(R.id.EditBtn);
        }
    }
}