package com.calicdan.florsgardenapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.Adapter.PurchasesAdapter;
import com.calicdan.florsgardenapp.Domain.PurchasesDomain;
import com.calicdan.florsgardenapp.Login;
import com.calicdan.florsgardenapp.Model.User;
import com.calicdan.florsgardenapp.R;
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

public class ProfileFragment extends Fragment {

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;


    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewPurchasesList;

    CircleImageView image_profile;
    TextView username, email, contact, password, address, id;
    Button logoutBtn;

    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
/*
        homebtn = view.findViewById(R.id.homebtn);
        forumbtn = view.findViewById(R.id.forumbtn);
        storebtn = view.findViewById(R.id.storebtn);
        notificationbtn = view.findViewById(R.id.notificationbtn);
        chatbtn = view.findViewById(R.id.chatbtn);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        imageRecog = view.findViewById(R.id.imageRecog);

        homebtn.setOnClickListener((View.OnClickListener) getActivity());
        forumbtn.setOnClickListener((View.OnClickListener) getActivity());
        storebtn.setOnClickListener((View.OnClickListener) getActivity());
        notificationbtn.setOnClickListener((View.OnClickListener) getActivity());
        chatbtn.setOnClickListener((View.OnClickListener) getActivity());
        imageViewProfile.setOnClickListener((View.OnClickListener) getActivity());
        imageRecog.setOnClickListener((View.OnClickListener) getActivity());
*/
        logoutBtn = view.findViewById(R.id.logoutBtn);
        //logoutBtn.setOnClickListener((View.OnClickListener) getActivity());
        image_profile = view.findViewById(R.id.profile_image);
        //image_profile.setOnClickListener((View.OnClickListener) getActivity());
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        contact = view.findViewById(R.id.contact);
        password = view.findViewById(R.id.passW);
        address = view.findViewById(R.id.address);
        id = view.findViewById(R.id.id);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPurchasesList=view.findViewById(R.id.recycleViewPurchases);
        recyclerViewPurchasesList.setLayoutManager(linearLayoutManager);

        ArrayList<PurchasesDomain> purchase = new ArrayList<>();
        purchase.add(new PurchasesDomain("To Pay","pay"));
        purchase.add(new PurchasesDomain("To Ship","ship"));
        purchase.add(new PurchasesDomain("To Receive","receive"));
        purchase.add(new PurchasesDomain("Completed","finished"));

        adapter=new PurchasesAdapter(purchase);
        recyclerViewPurchasesList.setAdapter(adapter);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

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
                    username.setText("Username: " + user.getFullName());
                    id.setText("User ID: " + user.getId());
                    email.setText("Email: " + user.getEmail());
                    contact.setText("Contact #: " + user.getContact());
                    password.setText("Password: " + ch[0]+ch[1]+"******");
                    address.setText("Address: " + user.getAddress());

                    if (user.getImageURL().equals("default")) {
                        image_profile.setImageResource(R.drawable.ic_default_profile);
                    } else {
                        Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
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
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}