package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.Adapter.MessageAdapter;
import com.calicdan.florsgardenapp.Model.Chat;
import com.calicdan.florsgardenapp.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference, rootref;

    //ImageButton btn_send, sendImageButton;

    EditText text_send;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;

    ValueEventListener seenListener;

    String userid, myUrl = "", checker="";
    private StorageTask uploadTask;
    private Uri fileUri;
    String receiver_name, receiver_uid,sender_uid, url;

    Uri uri;
    private static final int PICK_IMAGE = 1;

    private FloatingActionButton btn_send, sendImageButton;

    private ProgressDialog loadingbar;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userTypeReference = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");
        userTypeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userType.equals("customer")) {
                    startActivity(new Intent(MessageActivity.this, ChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    startActivity(new Intent(MessageActivity.this, AdminChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadingbar = new ProgressDialog(this);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        sendImageButton = findViewById(R.id.sendImageButton);
        text_send = findViewById(R.id.text_send);

        intent = getIntent();
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //notify = true;
                Chat chat = new Chat();
                String msg = text_send.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(),userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(MessageActivity.this)
                        .setMessage("Would you like to send an image?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent, "select image"), 438);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.ic_default_profile);
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

                readMesagges(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, "An error has occurred please try again!", Toast.LENGTH_SHORT).show();
            }
        });

        seenMessage(userid);
    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Chat chat = new Chat();

        rootref = FirebaseDatabase.getInstance().getReference("Chats");

        if (requestCode==438 && resultCode==RESULT_OK && data!=null && data.getData()!= null) {

            loadingbar.setTitle("Sending Image");
            loadingbar.setMessage("Please wait while the image is sending...");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            fileUri = data.getData();
            checker = "image";

            if (checker.equals("image")) {

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Sent Image Files");

                DatabaseReference userMessageKeyRef = rootref.push();

                final String messageChatsId = userMessageKeyRef.getKey();

                String messageSenderId = String.valueOf(userMessageKeyRef.child(messageChatsId).child("sender").setValue(chat.getSender()));
                String messageReceiverId = String.valueOf(userMessageKeyRef.child(messageChatsId).child("receiver").setValue(chat.getReceiver()));

                StorageReference filepath = storageReference.child(messageChatsId + "." + "jpg");

                uploadTask = filepath.putFile(fileUri);

                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            HashMap<String, Object> messagesImage = new HashMap<>();
                            messagesImage.put("type", "image");
                            messagesImage.put("sender", fuser.getUid());
                            messagesImage.put("receiver", userid);
                            messagesImage.put("message", myUrl);
                            messagesImage.put("isseen", false);

                            rootref.child(messageChatsId).setValue(messagesImage).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()) {
                                        loadingbar.dismiss();
                                        //sendMessage(fuser.getUid(), userid, myUrl);
                                        Toast.makeText(MessageActivity.this, "Message Sent Succesfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loadingbar.dismiss();
                                        Toast.makeText(MessageActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                            text_send.setText("");
                        }
                    }
                });
            } else {
                loadingbar.dismiss();
                Toast.makeText(this, "Nothing is selected", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void sendMessage(String sender, String receiver, String message) {
        //id of the receiver
        String userid = intent.getStringExtra("userid");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "text");
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference.child("Chats").push().setValue(hashMap);

        // add user or sender to chat fragment of the current user
        final DatabaseReference chatSenderRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid()).child(userid);
        chatSenderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //adds receiver user to chats tab
                    chatSenderRef.child("id").setValue(userid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // add user or receiver to chat fragment of the current user
        final DatabaseReference chatReceiverRef = FirebaseDatabase.getInstance().getReference("Chatlist").child(userid).child(fuser.getUid());
        chatReceiverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //adds receiver user to chats tab
                    chatReceiverRef.child("id").setValue(fuser.getUid());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void readMesagges(String myid, String userid, String imageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
}
