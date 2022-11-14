package com.calicdan.florsgardenapp.Adapter;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductsAdaptor extends RecyclerView.Adapter<ProductsAdaptor.ViewHolder> {
    ArrayList<FoodDomain> foodDomain;
    String temp,retProductName;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");


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
    holder.title.setText(foodDomain.get(position).getProductName());
    holder.fee.setText(String.valueOf(foodDomain.get(position).getProductPrice()));
    holder.addBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(holder.itemView.getContext(), ShowDetailsActivity.class);
            intent.putExtra("object", foodDomain.get(position));
            holder.itemView.getContext().startActivity(intent);
        }
    });
    temp = (foodDomain.get(position).getProductPic());
    retProductName = "products/" + temp;
    imageref = storageReference.child((retProductName));
    imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            Glide.with(holder.pic.getContext())
                    .load(uri)
                    .into(holder.pic);
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
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
