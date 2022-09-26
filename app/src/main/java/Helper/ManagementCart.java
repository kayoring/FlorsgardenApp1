package Helper;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import Domain.ProductsDomain;
import Interface.ChangeNumberProductsListener;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);

    }
    public void insertProduct(ProductsDomain item){
        ArrayList<ProductsDomain> listProducts= getListCart();
        boolean existAlready = false;
        int n=0;
        for (int i = 0; i <listProducts.size(); i ++){
            if(listProducts.get(i).getTitle().equals(item.getTitle())){
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
        tinyDB.putListObject("CartList",listProducts);
        Toast.makeText(context,"Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<ProductsDomain> getListCart(){
        return tinyDB.getListObject("CartList");
    }

    public void plusNumberProduct(ArrayList<ProductsDomain>listProduct, int position, ChangeNumberProductsListener changeNumberProductsListener){
        listProduct.get(position).setNumberInCart(listProduct.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listProduct);
        changeNumberProductsListener.changed();
    }

    public void minusNumberProduct(ArrayList<ProductsDomain>listProduct, int position, ChangeNumberProductsListener changeNumberProductsListener){
        if(listProduct.get(position).getNumberInCart()==1){
            listProduct.remove(position);
        }else{
            listProduct.get(position).setNumberInCart(listProduct.get(position).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList", listProduct);
        changeNumberProductsListener.changed();
    }

    public Double getTotalfee(){
        ArrayList<ProductsDomain> listorder = getListCart();
        double fee = 0;
        for (int i = 0; i < listorder.size(); i ++){
            fee = fee + (listorder.get(i).getFee() * listorder.get(i).getNumberInCart());
        }
        return fee;
    }
}
