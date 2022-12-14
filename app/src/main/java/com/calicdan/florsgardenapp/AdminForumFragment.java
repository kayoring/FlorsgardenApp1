package com.calicdan.florsgardenapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.calicdan.florsgardenapp.Fragments.ForumFragment;
import com.calicdan.florsgardenapp.Model.Inquiries;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminForumFragment extends Fragment {
    FirebaseAuth mAuth;
    private RecyclerView queList;
    private DatabaseReference Questionref, Likesref;
    Boolean LikeChecker = false;
    String currentuserID;
    FirebaseRecyclerAdapter<Inquiries, InquiriesViewHolder> InquiriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_forum, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentuserID = mAuth.getCurrentUser().getUid();

        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
        }

        queList = (RecyclerView) rootview.findViewById(R.id.all_que_list);

        queList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        queList.setLayoutManager(linearLayoutManager);
        Questionref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");
        Likesref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Likes");

        queList.hasFixedSize();
        queList.setLayoutManager(linearLayoutManager);
        //queList.setLayoutManager(new LinearLayoutManager(getActivity()));

        DisplayAllQuestion();

        return rootview;
    }

    private void DisplayAllQuestion() {
        FirebaseRecyclerOptions<Inquiries> inquiriesOptions =
                new FirebaseRecyclerOptions.Builder<Inquiries>()
                        .setQuery(Questionref, Inquiries.class)
                        .build();

        InquiriesAdapter = new FirebaseRecyclerAdapter<Inquiries, InquiriesViewHolder>(inquiriesOptions) {

            @NonNull
            @Override
            public InquiriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_all_inquiries, parent, false);
                return new InquiriesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull InquiriesViewHolder inquiriesViewHolder, @SuppressLint("RecyclerView") int position, @NonNull Inquiries inquiries) {

                final String PostKey = getRef(position).getKey();

                inquiriesViewHolder.getItemViewType();
                inquiriesViewHolder.setQuestion(inquiries.getQuestion());
                inquiriesViewHolder.setProfileImage(getContext(), inquiries.getImageURL());
                inquiriesViewHolder.setUsernamee(inquiries.getUsernamee());
                inquiriesViewHolder.setTime(inquiries.getTime());
                inquiriesViewHolder.setDate(inquiries.getDate());
                inquiriesViewHolder.setUserType(inquiries.getUserType());

                if (inquiries.getImageURL().equals("default")){
                    inquiriesViewHolder.post_que_user_image.setImageResource(R.drawable.profile);
                } else {
                    inquiriesViewHolder.setProfileImage(getContext(), inquiries.getImageURL());
                }


                inquiriesViewHolder.setLikeButtonStatus(PostKey);

                inquiriesViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new android.app.AlertDialog.Builder(inquiriesViewHolder.itemView.getContext())
                                .setTitle("Delete Content")
                                .setMessage("Would you like to delete this inquiry?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int pos = inquiriesViewHolder.getAdapterPosition();
                                        InquiriesAdapter.getRef(pos).removeValue();
                                        Toast.makeText(getActivity(), "Inquiry removed successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

                inquiriesViewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent commentsIntent = new Intent(getActivity(), AdminAnswersActivity.class);
                        //commentsIntent.putExtra("Que",)
                        commentsIntent.putExtra("Postkey", PostKey);
                        startActivity(commentsIntent);
                    }
                });

                inquiriesViewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LikeChecker = true;
                        Likesref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (LikeChecker.equals(true)) {
                                    if (dataSnapshot.child(PostKey).hasChild(currentuserID)) {
                                        Likesref.child(PostKey).child(currentuserID).removeValue();
                                        LikeChecker = false;
                                    } else {
                                        Likesref.child(PostKey).child(currentuserID).setValue(true);
                                        LikeChecker = false;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }};
        queList.setAdapter(InquiriesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        InquiriesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        InquiriesAdapter.stopListening();
    }

    public static class InquiriesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageButton LikePostButton, CommentPostButton, deleteBtn;
        TextView DisplaynoOfLikes;
        int countLikes;
        String currentUserId;
        DatabaseReference Likesref;
        CircleImageView post_que_user_image;
        public InquiriesViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            LikePostButton = (ImageButton) mView.findViewById(R.id.like_button);
            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
            deleteBtn = (ImageButton) mView.findViewById(R.id.deleteBtn);
            DisplaynoOfLikes = (TextView) mView.findViewById(R.id.no_likes);
            post_que_user_image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
            Likesref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setLikeButtonStatus(final String PostKey) {
            Likesref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(PostKey).hasChild(currentUserId)) {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.likess);
                        DisplaynoOfLikes.setText((Integer.toString(countLikes) + (" Likes")));
                    } else {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.dislike);
                        DisplaynoOfLikes.setText((Integer.toString(countLikes) + (" Likes")));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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