package com.calicdan.florsgardenapp.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {

    private String productName;
    private String productPic;
    private String productDescription;
    private Double productPrice;
    private int productQuantity;
    private int numberInCart;
    private double itemTotal, total, tax;
    private String status,uid;
    private String date;

    public FoodDomain(){}
    public FoodDomain(String productName, String productPic, String productDescription, Double productPrice, int productQuantity) {
        this.productName = productName;
        this.productPic = productPic;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }

    public FoodDomain(String productName, String productPic, String productDescription, Double productPrice, int productQuantity, int numberInCart) {
        this.productName = productName;
        this.productPic = productPic;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.numberInCart = numberInCart;
    }

    public FoodDomain(String productName, String productPic, String productDescription, Double productPrice, int productQuantity, int numberInCart, double itemTotal, double total, double tax, String status, String uid, String date) {
        this.productName = productName;
        this.productPic = productPic;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.numberInCart = numberInCart;
        this.itemTotal = itemTotal;
        this.total = total;
        this.tax = tax;
        this.status = status;
        this.uid = uid;
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
    public int getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
