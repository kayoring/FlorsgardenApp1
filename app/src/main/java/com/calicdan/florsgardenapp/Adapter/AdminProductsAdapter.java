package com.calicdan.florsgardenapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.Domain.Product;
import com.calicdan.florsgardenapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminProductsAdapter extends RecyclerView.Adapter<AdminProductsAdapter.ViewHolder> {
    ArrayList<Product> productDomain;

    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");
    public AdminProductsAdapter(ArrayList<Product> productDomains) { this.productDomain = productDomains;}
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_adminproducts,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productTitle.setText(productDomain.get(position).getProductName());

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
