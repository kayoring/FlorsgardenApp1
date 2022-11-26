package com.calicdan.florsgardenapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calicdan.florsgardenapp.ForumActivity;
import com.calicdan.florsgardenapp.Model.Inquiries;
import com.calicdan.florsgardenapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminEditFragment extends Fragment {

    FirebaseUser fuser;
    private RecyclerView editList;
    private DatabaseReference refs, Questionref;
    FirebaseRecyclerAdapter<Inquiries, PostsViewHolder> PostsAdapater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.admin_edit_frament, container, false);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        editList = (RecyclerView) rootview.findViewById(R.id.editList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        editList.setHasFixedSize(true);
        editList.setLayoutManager(linearLayoutManager);

        refs = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("Posts");

        DisplayOwnPosts();

        return rootview;

    }

    private void DisplayOwnPosts() {
        FirebaseRecyclerOptions<Inquiries> postsOptions =
                new FirebaseRecyclerOptions.Builder<Inquiries>()
                        .setQuery(refs, Inquiries.class)
                        .build();

        PostsAdapater = new FirebaseRecyclerAdapter<Inquiries, PostsViewHolder>(postsOptions) {

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_posts, parent, false);
                return new PostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder postsViewHolder, int position, @NonNull Inquiries inquiries) {

                postsViewHolder.getItemViewType();
                postsViewHolder.setQuestion(inquiries.getQuestion());
                postsViewHolder.setTime(inquiries.getTime());
                postsViewHolder.setDate(inquiries.getDate());
                postsViewHolder.setUsernamee(inquiries.getUsernamee());
                postsViewHolder.setUserType(inquiries.getUserType());

                if (inquiries.getImageURL().equals("default")){
                    postsViewHolder.post_que_user_image.setImageResource(R.drawable.profile);
                } else {
                    postsViewHolder.setProfileImage(getContext(), inquiries.getImageURL());
                }

                postsViewHolder.deleteBtn.setOnClickListener(new View   .OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(postsViewHolder.itemView.getContext())
                                .setTitle("Delete Content")
                                .setMessage("Would you like to delete this inquiry?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Questionref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");

                                        Questionref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                    String id = snapshot.child("id").getValue().toString();
                                                    String quest = snapshot.child("question").getValue().toString();

                                                    if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && inquiries.getQuestion().equals(quest)) {
                                                        int pos = postsViewHolder.getAdapterPosition();
                                                        PostsAdapater.getRef(pos).removeValue();
                                                        //database
                                                        snapshot.getRef().removeValue();
                                                        Toast.makeText(getActivity(), "Inquiry removed successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

                postsViewHolder.editBtns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText edittext = new EditText(getContext());
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                        alert.setTitle("Edit post");
                        alert.setMessage("Enter new text to edit your post");

                        alert.setView(edittext);

                        edittext.setText(inquiries.getQuestion());
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Questionref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");
                                Questionref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            String id = snapshot.child("id").getValue().toString();
                                            String quest = snapshot.child("question").getValue().toString();

                                            if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && inquiries.getQuestion().equals(quest)) {

                                                snapshot.getRef().child("question").setValue(edittext.getText().toString());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                refs = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("Posts");
                                refs.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            String id = snapshot.child("id").getValue().toString();
                                            String quest = snapshot.child("question").getValue().toString();

                                            if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && inquiries.getQuestion().equals(quest)) {
                                                snapshot.getRef().child("question").setValue(edittext.getText().toString());
                                                Toast.makeText(getContext(), "Edited post successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getContext(), ForumActivity.class));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", null);
                        alert.show();
                    }
                });

            }
        };
        editList.setAdapter(PostsAdapater);
    }

    @Override
    public void onStart() {
        super.onStart();
        PostsAdapater.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        PostsAdapater.stopListening();
    }


    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView post_que_user_image;
        ImageButton deleteBtn, editBtns;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            editBtns = (ImageButton) mView.findViewById(R.id.editBtns);
            deleteBtn = (ImageButton) mView.findViewById(R.id.deleteBtn);
            post_que_user_image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
        }

        public void setUsernamee(String username) {
            TextView u = (TextView) mView.findViewById(R.id.post_que_user_name);
            u.setText(username);
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