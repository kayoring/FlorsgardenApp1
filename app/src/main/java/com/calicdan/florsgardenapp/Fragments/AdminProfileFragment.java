package com.calicdan.florsgardenapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.Adapter.AdminPurchasesAdapter;
import com.calicdan.florsgardenapp.Adapter.PurchasesAdapter;
import com.calicdan.florsgardenapp.ChatbotActivity;
import com.calicdan.florsgardenapp.Domain.PurchasesDomain;
import com.calicdan.florsgardenapp.ForumActivity;
import com.calicdan.florsgardenapp.Home;
import com.calicdan.florsgardenapp.ImageRecognitionHome;
import com.calicdan.florsgardenapp.Login;
import com.calicdan.florsgardenapp.Model.User;
import com.calicdan.florsgardenapp.ProfileActivity;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.StoreActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileFragment extends Fragment{

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewPurchasesList;

    CircleImageView profile_image;
    TextView username, email, contact, password, address;
    Button logoutBtn;

    DatabaseReference reference,ref;
    FirebaseUser fuser,firebaseUser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    if (getActivity() !=null) {
                        Glide.with(getContext()).load(user.getImageURL()).into(profile_image);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        profile_image = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        contact = view.findViewById(R.id.contact);
        //password = view.findViewById(R.id.passW);
        address = view.findViewById(R.id.address);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPurchasesList=view.findViewById(R.id.recycleViewPurchases);
        recyclerViewPurchasesList.setLayoutManager(linearLayoutManager);

        String userType = getArguments().getString("userType1");

        ArrayList<PurchasesDomain> purchase = new ArrayList<>();
            purchase.add(new PurchasesDomain("Pending", "pay"));
            purchase.add(new PurchasesDomain("Paid", "ship"));
            purchase.add(new PurchasesDomain("To Ship", "receive"));
            purchase.add(new PurchasesDomain("Completed", "finished"));

        adapter=new AdminPurchasesAdapter(purchase);
        recyclerViewPurchasesList.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(isAdded()) {
                    User user = dataSnapshot.getValue(User.class);
                    String pass = user.getPassword();
                    char[] ch = new char[pass.length()];
                    for (int i = 0; i < pass.length(); i++) {
                        ch[i] = pass.charAt(i);
                    }

                    username.setText(user.getFullName());
                    email.setText(user.getEmail());
                    contact.setText(user.getContact());
                    //password.setText("Password: " + ch[0]+ch[1]+"******");

                    if (user.getAddress() == null) {
                        address.setText("N/A");
                    } else {
                        address.setText(user.getAddress());
                    }

                    if (user.getImageURL().equals("default")) {
                        profile_image.setImageResource(R.drawable.ic_default_profile);
                    } else {
                        Glide.with(getContext()).load(user.getImageURL()).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        return view;
    }
    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homebtn:
                startActivity(new Intent(getActivity(), Home.class));
                break;
            case R.id.forumbtn:
                startActivity(new Intent(getActivity(), ForumActivity.class));
                break;
            case R.id.storebtn:
                startActivity(new Intent(getActivity(), StoreActivity.class));
                break;
            case R.id.notificationbtn:
                startActivity(new Intent(getActivity(), StoreActivity.class));
                break;
            case R.id.chatbtn:
                startActivity(new Intent(getActivity(), ChatbotActivity.class));
                break;
            case R.id.imageViewProfile:
                startActivity(new Intent(getActivity(), ProfileActivity.class));
                break;
            case R.id.imageRecog:
                startActivity(new Intent(getActivity(), ImageRecognitionHome.class));
                break;
        }
    }

 */
}