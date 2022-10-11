package com.calicdan.florsgardenapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class VermicultureAdapter extends RecyclerView.Adapter<VermicultureAdapter.MyViewHolder>{

    Context context;
    ArrayList<HomeModel> wormList;

    public VermicultureAdapter(Context context, ArrayList<HomeModel> wormList) {
        this.context = context;
        this.wormList = wormList;
    }
    @NonNull
    @Override
    public VermicultureAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false);
        return new VermicultureAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VermicultureAdapter.MyViewHolder holder, int position) {

        HomeModel HomeModel = wormList.get(position);
        holder.nameText.setText(HomeModel.getName());
        holder.descriptionText.setText(HomeModel.getDescription());

        Glide.with(holder.itemView.getContext())
                .load(wormList.get(position).getSurl())
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return wormList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descriptionText;
        ImageView profileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.nameText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            profileImage = itemView.findViewById(R.id.profileImage);
        }
    }


}
