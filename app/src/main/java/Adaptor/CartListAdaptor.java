package Adaptor;

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

import Domain.ProductsDomain;
import Helper.ManagementCart;
import Interface.ChangeNumberProductsListener;

public class CartListAdaptor extends RecyclerView.Adapter<CartListAdaptor.ViewHolder> {
    private ArrayList<ProductsDomain> productsDomain;
    private ManagementCart managementCart;
    private ChangeNumberProductsListener changeNumberProductsListener;

    public CartListAdaptor(ArrayList<ProductsDomain> productsDomain, ManagementCart managementCart, ChangeNumberProductsListener changeNumberProductsListener) {
        this.productsDomain = productsDomain;
        this.managementCart = managementCart;
        this.changeNumberProductsListener = changeNumberProductsListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(productsDomain.get(position).getTitle());
        holder.feeEachProduct.setText(String.valueOf(productsDomain.get(position)));
        holder.totalEachProduct.setText(String.valueOf(Math.round((productsDomain.get(position).getNumberInCart()*productsDomain.get(position).getFee())*100)/100));
        holder.num.setText(String.valueOf(productsDomain.get(position).getNumberInCart()));

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(productsDomain.get(position).getPic(),"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);

        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsDomain.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, feeEachProduct;
        ImageView pic, plusItem, minusItem;
        TextView totalEachProduct, num;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            feeEachProduct = itemView.findViewById(R.id.feeEachProduct);
            pic = itemView.findViewById(R.id.picCart);
            totalEachProduct = itemView.findViewById(R.id.totalEachProduct);
            num = itemView.findViewById(R.id.numberProductTxt);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);

        }
    }
}
