package com.calicdan.florsgardenapp.Helper;

import android.content.Context;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.calicdan.florsgardenapp.Domain.FoodDomain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Interface.ChangeNumberProductsListener;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference fdb = db.getReference().child("Orders");
    String uid,email,productID;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);

    }
    public void insertProduct(FoodDomain item){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            email = user.getEmail();
        }
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMddyyyyHH");
        date = dateFormat.format(calendar.getTime());

        ArrayList<FoodDomain> listProducts = getListCart();
        boolean existAlready = false;
        int n=0;
        for (int i = 0; i <listProducts.size(); i ++){
            if(listProducts.get(i).getProductName().equals(item.getProductName())){
                existAlready = true;
                n = i;
                break;
            }
        }

        if(existAlready){
            listProducts.get(n).setNumberInCart(item.getNumberInCart());
        } else {
            listProducts.add(item);
        }
        fdb.child(uid).child(item.getProductName()).setValue(item);
        tinyDB.putListObject("CartList",listProducts);
        Toast.makeText(context,"Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodDomain> getListCart(){
        return tinyDB.getListObject("CartList");
    }

    public void plusNumberProduct(ArrayList<FoodDomain>listProduct, int position, ChangeNumberProductsListener changeNumberProductsListener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            email = user.getEmail();
        }
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMddyyyyHH");
        date = dateFormat.format(calendar.getTime());
        listProduct.get(position).setNumberInCart(listProduct.get(position).getNumberInCart()+1);
        fdb.child(uid).child(listProduct.get(position).getProductName()).child("numberInCart").setValue(listProduct.get(position).getNumberInCart());
        tinyDB.putListObject("CartList",listProduct);
        changeNumberProductsListener.changed();
    }

    public void minusNumberProduct(ArrayList<FoodDomain>listProduct, int position, ChangeNumberProductsListener changeNumberProductsListener){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            email = user.getEmail();
        }
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MMddyyyyHH");
        date = dateFormat.format(calendar.getTime());
        if(listProduct.get(position).getNumberInCart()==1){
            fdb.child(uid).child(listProduct.get(position).getProductName()).removeValue();
            listProduct.remove(position);
        }else{
            listProduct.get(position).setNumberInCart(listProduct.get(position).getNumberInCart()-1);
            fdb.child(uid).child(listProduct.get(position).getProductName()).child("numberInCart").setValue(listProduct.get(position).getNumberInCart());
        }
        tinyDB.putListObject("CartList", listProduct);
        changeNumberProductsListener.changed();
    }

    public Double getTotalfee(){
        ArrayList<FoodDomain> listorder = getListCart();
        double fee = 0;
        for (int i = 0; i < listorder.size(); i ++){
            fee = fee + (listorder.get(i).getProductPrice() * listorder.get(i).getNumberInCart());
        }
        return fee;
    }
}
