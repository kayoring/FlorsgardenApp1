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
import com.calicdan.florsgardenapp.AdminDetailsActivity;
import com.calicdan.florsgardenapp.Domain.Product;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.ShowDetailsActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.ViewHolder> {
    ArrayList<Product> productDomain;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");
    String temp,retProductName;

    public AdminProductsAdapter(ArrayList<Product> productDomains) { this.productDomain = productDomains;}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_adminproducts,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productTitle.setText(productDomain.get(position).getProductName());
        holder.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), AdminDetailsActivity.class);
                intent.putExtra("name", productDomain.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
    });
        temp = (productDomain.get(position).getProductPic());
        retProductName = "products/" + temp;
        imageref = storageReference.child(retProductName);
        imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.productPic.getContext())
                        .load(uri)
                        .into(holder.productPic);
                
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDomain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productTitle,EditBtn;
        ImageView productPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPic = itemView.findViewById(R.id.productPic);
            EditBtn = itemView.findViewById(R.id.EditBtn);
        }
    }

}
