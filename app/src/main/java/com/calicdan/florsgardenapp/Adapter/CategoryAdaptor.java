package com.calicdan.florsgardenapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.R;

import java.util.ArrayList;

import com.calicdan.florsgardenapp.Domain.CategoryDomain;
import com.calicdan.florsgardenapp.ShowDetailsActivity;
import com.calicdan.florsgardenapp.WormsCategory;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.ViewHolder> {
    ArrayList<CategoryDomain> categoryDomains;

    public CategoryAdaptor(ArrayList<CategoryDomain> categoryDomains) {
        this.categoryDomains = categoryDomains;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_category,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.categoryName.setText(categoryDomains.get(position).getTitle());
    String picUrl="";
    switch(position){
        case 0:{
            picUrl="worm";
            holder.storeLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.wormbg));
            break;
        }
        case 1:{
            picUrl="fertilizer";
            holder.storeLayout.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.wormbg1));
            break;
        }
     }
     int drawableResourceId=holder.itemView.getContext().getResources().getIdentifier(picUrl,"drawable",holder.itemView.getContext().getPackageName());

    Glide.with(holder.itemView.getContext())
            .load(drawableResourceId)
            .into(holder.categoryPic);

        String finalPicUrl = picUrl;
        holder.storeLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(holder.itemView.getContext(), WormsCategory.class);
            holder.itemView.getContext().startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return categoryDomains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout storeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName=itemView.findViewById(R.id.purchasesName);
            categoryPic=itemView.findViewById(R.id.purchasesPic);
            storeLayout=itemView.findViewById(R.id.purchasesLayout);
        }
    }
}
