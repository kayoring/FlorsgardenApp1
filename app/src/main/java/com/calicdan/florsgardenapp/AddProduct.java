package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.calicdan.florsgardenapp.Domain.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddProduct extends AppCompatActivity {

    EditText inpProductName,inpProductPrice,inpProductDescription,inpProductQuantity;
    TextView addProductDbBTN;
    ImageView inpProductPhoto;
    Uri imageUri;
    String productName, productDescription;
    int productQuantity;
    float productPrice;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference ref = fdb.getReference("Products");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initList();
    }

    private void initView(){
        setContentView(R.layout.activity_add_product);
        inpProductName = findViewById(R.id.inpProductName);
        inpProductPrice = findViewById(R.id.inpProductPrice);
        inpProductDescription = findViewById(R.id.inpProductDescription);
        inpProductQuantity = findViewById(R.id.inpProductQuantity);
        addProductDbBTN = findViewById(R.id.addProductDbBTN);
        inpProductPhoto = findViewById(R.id.inpProductPhoto);
    }

    private void initList(){
        inpProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        addProductDbBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName = inpProductName.getText().toString();
                productPrice = Float.parseFloat(String.valueOf(inpProductPrice.getText()));
                productDescription = inpProductDescription.getText().toString();
                productQuantity = Integer.parseInt(String.valueOf(inpProductQuantity.getText()));
                Product prod = new Product(productName,productDescription,productQuantity,productPrice);
                ref.child(productName).setValue(prod);
                Toast.makeText(getApplicationContext(),productName + " Added!",Toast.LENGTH_SHORT).show();
                uploadPicture();
                startActivity(new Intent(AddProduct.this,AdminStoreActivity.class));
            }
        });
    }

    private void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            inpProductPhoto.setImageURI(imageUri);
        }
    }
    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        final String imageKey = productName.replaceAll(" ","");
        StorageReference productsRef = storageReference.child("products/" + imageKey);
        productsRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(android.R.id.content),"Image Failed.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }
}