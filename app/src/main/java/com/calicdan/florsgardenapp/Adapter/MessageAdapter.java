package com.calicdan.florsgardenapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.calicdan.florsgardenapp.Model.Chat;
import com.calicdan.florsgardenapp.Model.User;
import com.calicdan.florsgardenapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;

    public ImageView senderImageview, receiverImageview;

    private FirebaseAuth mAuth;
    FirebaseUser fuser;

    DatabaseReference reference,ref;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //senderImageview = itemView.findViewById(R.id.senderImageview);
        //receiverImageview = itemView.findViewById(R.id.receiverImageview);

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            mAuth = FirebaseAuth.getInstance();
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            mAuth = FirebaseAuth.getInstance();
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int i) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        Chat chat = mChat.get(i);
        String fromUserID = chat.getSender();
        String fromMessageType = chat.getType();



        if(fromMessageType.equals("text")){
            if (fromUserID.equals(messageSenderId)) {

                //getting profile picture of the sender
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getImageURL().equals("default")) {
                            holder.profile_image.setImageResource(R.drawable.ic_default_profile);
                        } else {
                            Picasso.with(mContext).load(user.getImageURL()).into(holder.profile_image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.show_message.setText(chat.getMessage());

            } else {
                //getting profile picture of the receiver
                if (imageurl.equals("default")){
                    holder.profile_image.setImageResource(R.drawable.ic_default_profile);
                } else {
                    Picasso.with(mContext).load(imageurl).into(holder.profile_image);
                }
                holder.show_message.setText(chat.getMessage());
            }
        }

        else if (fromMessageType.equals("image")) {
            if (fromUserID.equals(messageSenderId)) {

                //getting profile picture of the sender
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                ref = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user.getImageURL().equals("default")){
                            holder.profile_image.setImageResource(R.drawable.ic_default_profile);
                        } else {
                            Picasso.with(mContext).load(user.getImageURL()).into(holder.profile_image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                holder.show_message.setVisibility(View.GONE);
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.senderImageview.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(chat.getMessage()).into(holder.senderImageview);

            } else {

                //getting profile picture of the receiver
                if (imageurl.equals("default")){
                    holder.profile_image.setImageResource(R.drawable.ic_default_profile);
                } else {
                    Picasso.with(mContext).load(imageurl).into(holder.profile_image);
                }
                holder.show_message.setVisibility(View.GONE);
                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.receiverImageview.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(chat.getMessage()).into(holder.receiverImageview);
            }
        }

        if (i == mChat.size()-1){
            if (chat.isIsseen()){
                holder.txt_seen.setText("Seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image, senderImageview, receiverImageview;
        public TextView txt_seen;
        public RelativeLayout receiverLayout, senderLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);

            senderImageview = itemView.findViewById(R.id.senderImageview);
            receiverImageview = itemView.findViewById(R.id.receiverImageview);
            receiverLayout = itemView.findViewById(R.id.receiverLayout);
            senderLayout = itemView.findViewById(R.id.senderLayout);

        }
    }

    @Override
    public int getItemViewType(int i) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(i).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}

