package com.calicdan.florsgardenapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.ArrayList;

import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.calicdan.florsgardenapp.Helper.ManagementCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Interface.ChangeNumberProductsListener;

public class CartListAdaptor extends RecyclerView.Adapter<CartListAdaptor.ViewHolder> {
    private ArrayList<FoodDomain> foodDomain;
    private ManagementCart managementCart;
    private ChangeNumberProductsListener changeNumberProductsListener;
    String temp,retProductName;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference productsref = fdb.getReference("Products");

    public CartListAdaptor(ArrayList<FoodDomain> foodDomain, Context context, ChangeNumberProductsListener changeNumberProductsListener) {
        this.foodDomain = foodDomain;
        this.managementCart = new ManagementCart(context);
        this.changeNumberProductsListener = changeNumberProductsListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdaptor.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(foodDomain.get(position).getProductName());
        holder.feeEachProduct.setText(String.valueOf(foodDomain.get(position).getProductPrice()));
        holder.totalEachProduct.setText(String.valueOf(Math.round((foodDomain.get(position).getNumberInCart()* foodDomain.get(position).getProductPrice())*100)/100));
        holder.num.setText(String.valueOf(foodDomain.get(position).getNumberInCart()));

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

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managementCart.plusNumberProduct(foodDomain, position, new ChangeNumberProductsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberProductsListener.changed();
                    }
                });

            }
        });

        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managementCart.minusNumberProduct(foodDomain, position, new ChangeNumberProductsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNumberProductsListener.changed();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodDomain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, feeEachProduct;
        ImageView pic, plusItem, minusItem;
        TextView totalEachProduct, num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            feeEachProduct = itemView.findViewById(R.id.totalFee);
            pic = itemView.findViewById(R.id.picCart);
            totalEachProduct = itemView.findViewById(R.id.totalEachProduct);
            num = itemView.findViewById(R.id.numberProductTxt);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);

        }
    }
}
