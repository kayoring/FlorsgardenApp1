package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.calicdan.florsgardenapp.Domain.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AdminDetailsActivity extends AppCompatActivity {
    EditText inpProductName,inpProductPrice,inpProductDescription,inpProductQuantity;
    TextView editProductDbBTN;
    ImageView editProductPhoto;
    Uri imageUri;
    String productName, productDescription,dbproductname,dbproductdescription, retProductName,retProductName1,temp,productPic;
    int productQuantity,dbproductquantity;
    float productPrice,dbproductprice;
    private Product editProduct;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    StorageReference imageref;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    DatabaseReference ref = fdb.getReference("Products");


    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_admin_details);

    initView();
    getBundle();
    initList();
    }

    private void initView(){
        inpProductName = findViewById(R.id.inpProductName);
        inpProductPrice = findViewById(R.id.inpProductPrice);
        inpProductDescription = findViewById(R.id.inpProductDescription);
        inpProductQuantity = findViewById(R.id.inpProductQuantity);
        editProductDbBTN = findViewById(R.id.editProductDbBTN);
        editProductPhoto = findViewById(R.id.editProductPhoto);

    }

    private void getBundle(){
        editProduct = (Product) getIntent().getSerializableExtra("name");
        retProductName = editProduct.getProductName();
        temp = retProductName.replaceAll(" ", "");
        retProductName1 = "products/" + temp;
        DatabaseReference productsref = ref.child(retProductName);
        DatabaseReference productnameref = productsref.child("productName");

        productnameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbproductname = dataSnapshot.getValue(String.class);
                inpProductName.setText(dbproductname);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDetailsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference productdescref = productsref.child("productDescription");
        productdescref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbproductdescription = dataSnapshot.getValue(String.class);
                inpProductDescription.setText(dbproductdescription);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDetailsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference productpriceref = productsref.child("productPrice");
        productpriceref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbproductprice = dataSnapshot.getValue(Float.class);
                inpProductPrice.setText(String.valueOf(dbproductprice));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDetailsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference productquantityref = productsref.child("productQuantity");
        productquantityref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbproductquantity = dataSnapshot.getValue(Integer.class);
                inpProductQuantity.setText(String.valueOf(dbproductquantity));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDetailsActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
        imageref = storageReference.child(retProductName1);

        try {
            final File localFile = File.createTempFile(retProductName,"jpeg");
            imageUri = Uri.fromFile(localFile);
            imageref.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.editProductPhoto)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initList(){
        editProductPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        editProductDbBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productName = inpProductName.getText().toString();
                productPrice = Float.parseFloat(String.valueOf(inpProductPrice.getText()));
                productDescription = inpProductDescription.getText().toString();
                productQuantity = Integer.parseInt(String.valueOf(inpProductQuantity.getText()));
                uploadPicture();
                Product prod = new Product(productName,productDescription,productQuantity,productPrice,productPic);
                ref.child(productName).setValue(prod);
                Toast.makeText(getApplicationContext(),productName + " edited!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminDetailsActivity.this,AdminStoreActivity.class));

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
            editProductPhoto.setImageURI(imageUri);
        }
    }
    private void uploadPicture() {
        final String randomKey = UUID.randomUUID().toString();
        final String imageKey = productName.replaceAll(" ","");
        productPic = imageKey;
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
