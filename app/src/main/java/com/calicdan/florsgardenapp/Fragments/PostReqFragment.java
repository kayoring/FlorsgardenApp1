package com.calicdan.florsgardenapp.Fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.calicdan.florsgardenapp.AdminForumActivity;
import com.calicdan.florsgardenapp.ForumActivity;
import com.calicdan.florsgardenapp.Model.Inquiries;
import com.calicdan.florsgardenapp.Model.User;
import com.calicdan.florsgardenapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostReqFragment extends Fragment {

    FirebaseUser fuser;
    private RecyclerView editList;
    DatabaseReference refs1 ,PostReqRef, UsersReference, refs, fromPath, toPath, fromPath2, toPath2;
    FirebaseRecyclerAdapter<Inquiries, PostReqViewHolder> PostReqAdapater;
    String username, profileImage,userType,currentUserID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_post_req, container, false);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        editList = (RecyclerView) rootview.findViewById(R.id.editList);

        init();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        editList.setHasFixedSize(true);
        editList.setLayoutManager(linearLayoutManager);

        //Create database for data to be retrieved from
        PostReqRef = FirebaseDatabase.getInstance().getReference().child("Forums").child("Post Requests");

        //Check if user or admin
        refs1 = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");
        refs1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DisplayAllPostReq();

        return rootview;
    }

    public void init() {

        FirebaseUser fusers = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users").child(fusers.getUid());

        ref.child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sn1) {
                username = sn1.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ref.child("imageURL").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sn2) {
                profileImage = sn2.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ref.child("usertype").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sn3) {
                userType = sn3.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        ref.child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot sn4) {
                currentUserID = sn4.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void moveRecord(DatabaseReference here, final DatabaseReference there) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    there.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {
                            Log.d(TAG, "Success!");
                            here.removeValue();
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "Copy failed!");
                            Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage()); //Don't ignore potential errors!
            }
        };
        here.addListenerForSingleValueEvent(valueEventListener);
    }

    private void DisplayAllPostReq() {
        FirebaseRecyclerOptions<Inquiries> postReqOptions =
                new FirebaseRecyclerOptions.Builder<Inquiries>()
                        .setQuery(PostReqRef, Inquiries.class)
                        .build();

        PostReqAdapater = new FirebaseRecyclerAdapter<Inquiries, PostReqViewHolder>(postReqOptions) {

            @NonNull
            @Override
            public PostReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_approve_all_inquiries, parent, false);
                return new PostReqViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostReqViewHolder postReqViewHolder, int position, @NonNull Inquiries inquiries) {

                postReqViewHolder.getItemViewType();
                postReqViewHolder.setQuestion(inquiries.getQuestion());
                postReqViewHolder.setProfileImage(getContext(), inquiries.getImageURL());
                postReqViewHolder.setUsernamee(inquiries.getUsernamee());
                postReqViewHolder.setTime(inquiries.getTime());
                postReqViewHolder.setDate(inquiries.getDate());
                postReqViewHolder.setUserType(inquiries.getUserType());

                if (inquiries.getImageURL().equals("default")){
                    postReqViewHolder.post_que_user_image.setImageResource(R.drawable.profile);
                } else {
                    postReqViewHolder.setProfileImage(getContext(), inquiries.getImageURL());
                }

                postReqViewHolder.approveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(postReqViewHolder.itemView.getContext())
                                .setTitle("Approve post")
                                .setMessage("Would you like to approve this inquiry? After approving the post will be displayed in the forums section.")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //DB changes
                                        fromPath = FirebaseDatabase.getInstance().getReference().child("Forums").child("Post Requests");
                                        //String pushIDs = fromPath.push().getKey();
                                        //fromPath2 = FirebaseDatabase.getInstance().getReference().child("Forums").child("Post Requests").child(pushIDs);

                                        toPath = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");
                                        //String pushID2 = toPath.push().getKey();
                                        //toPath2 = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions").child(pushID2);

                                        moveRecord(fromPath, toPath);

                                        refs = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");


                                        refs.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    moveRecord(fromPath, refs);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        /*
                                        refs.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            //pointed at Users data
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                //check every user IDs id it has a node posts
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String id = snapshot.child("id").getValue(String.class);

                                                    UsersReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                                    UsersReference.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                                String ids = snapshot.child("id").getValue(String.class);

                                                                UsersReference = FirebaseDatabase.getInstance().getReference().child("Users");

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                         */

                                        /*
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        UsersReference = database.getReference().child("Forums").child("Questions");
                                        refs = database.getReference().child("Users").child(fuser.getUid()).child("Posts");
                                        String pushID = UsersReference.push().getKey();

                                        HashMap userMap=new HashMap();
                                        userMap.put("id", getId());
                                        userMap.put("username", inquiries.getUsernamee());
                                        userMap.put("question", inquiries.getQuestion());
                                        userMap.put("date", inquiries.getDate());
                                        userMap.put("time", inquiries.getTime());
                                        userMap.put("imageURL", inquiries.getImageURL());
                                        userMap.put("userType", inquiries.getUserType());
                                        //Add data to forums section DB and displayed
                                        UsersReference.child(pushID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if(task.isSuccessful()) {
                                                    //if it was added successfully on forums section DB then also add it to user to edit their post
                                                    refs.push().updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if(task.isSuccessful()) {
                                                                int pos = postReqViewHolder.getAdapterPosition();
                                                                PostReqAdapater.getRef(pos).removeValue();
                                                                Toast.makeText(getActivity(), "Inquiry is now viewable in the forums section.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                         */
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

                postReqViewHolder.declineBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(postReqViewHolder.itemView.getContext())
                                .setTitle("Decline post")
                                .setMessage("Would you like to decline this inquiry? Declining will remove this post request.")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //DB changes
                                        int pos = postReqViewHolder.getAdapterPosition();
                                        PostReqAdapater.getRef(pos).removeValue();
                                        Toast.makeText(getActivity(), "Inquiry is now remove from the post requests.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

            }
        };
        editList.setAdapter(PostReqAdapater);

    }



    @Override
    public void onStart() {
        super.onStart();
        PostReqAdapater.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        PostReqAdapater.stopListening();
    }

    public static class PostReqViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView post_que_user_image;
        Button approveBtn, declineBtn;

        public PostReqViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            post_que_user_image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
            approveBtn = (Button) mView.findViewById(R.id.approveBtn);
            declineBtn = (Button) mView.findViewById(R.id.declineBtn);
        }

        public void setUsernamee(String usernamee) {
            TextView u = (TextView) mView.findViewById(R.id.post_que_user_name);
            u.setText(usernamee);
        }

        public void setProfileImage(Context ctx, String profileImage) {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
            Picasso.with(ctx).load(profileImage).into(image);
        }

        public void setQuestion(String question) {
            TextView userq = (TextView) mView.findViewById(R.id.user_que);
            userq.setText(question);
        }

        public void setTime(String time) {
            TextView ptime = (TextView) mView.findViewById(R.id.post_time);
            ptime.setText("  " + time);
        }

        public void setDate(String date) {
            TextView pdate = (TextView) mView.findViewById(R.id.post_date);
            pdate.setText("  " + date);
        }


        public void setUserType(String type) {
            TextView usertype = (TextView) mView.findViewById(R.id.userclass);
            usertype.setText(type);
        }

    }

}