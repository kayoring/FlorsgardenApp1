package com.calicdan.florsgardenapp.Fragments;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.calicdan.florsgardenapp.AdminForumActivity;
import com.calicdan.florsgardenapp.ForumActivity;
import com.calicdan.florsgardenapp.Model.Answers;
import com.calicdan.florsgardenapp.Model.Inquiries;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesFragment extends Fragment {

    FirebaseUser fuser;
    private RecyclerView editList;
    private DatabaseReference refs, Questionref, refs1;
    FirebaseRecyclerAdapter<Answers, RepliesViewHolder> RepliesAdapter;
    String userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_replies, container, false);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        editList = (RecyclerView) rootview.findViewById(R.id.editList);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        editList.setHasFixedSize(true);
        editList.setLayoutManager(linearLayoutManager);

        refs = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("Replies");

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

        DisplayOwnReplies();

        return rootview;
    }

    private void DisplayOwnReplies() {
        FirebaseRecyclerOptions<Answers> repliesOptions =
                new FirebaseRecyclerOptions.Builder<Answers>()
                        .setQuery(refs, Answers.class)
                        .build();

        RepliesAdapter = new FirebaseRecyclerAdapter<Answers, RepliesViewHolder>(repliesOptions) {

            @NonNull
            @Override
            public RepliesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_replies, parent, false);
                return new RepliesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RepliesViewHolder repliesViewHolder, int position, @NonNull Answers answers) {

                repliesViewHolder.setUsername(answers.getUsername());
                repliesViewHolder.setAnswer(answers.getAnswer());

                if (answers.getProfileimage().equals("default")){
                    repliesViewHolder.post_que_user_image.setImageResource(R.drawable.profile);
                } else {
                    repliesViewHolder.setProfileimage(getContext(), answers.getProfileimage());
                }

                repliesViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(repliesViewHolder.itemView.getContext())
                                .setTitle("Delete Content")
                                .setMessage("Would you like to delete this comment?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Questionref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");
                                        Questionref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                    if (snapshot.hasChild("answers")) {

                                                        for (DataSnapshot snap : snapshot.child("answers").getChildren()) {
                                                            String uid = snap.child("uid").getValue(String.class);
                                                            String ans = snap.child("answer").getValue(String.class);

                                                            if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && answers.getAnswer().equals(ans)) {

                                                                //Users database
                                                                int pos = repliesViewHolder.getAdapterPosition();
                                                                RepliesAdapter.getRef(pos).removeValue();

                                                                //Forums database
                                                                snap.getRef().removeValue();
                                                                Toast.makeText(getActivity(), "comment removed successfully", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }).setNegativeButton("No",null).show();
                    }
                });

                repliesViewHolder.editBtns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final EditText edittexts = new EditText(getContext());
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(getActivity());

                        alert2.setTitle("Edit comment");
                        alert2.setMessage("Enter new text to edit your comment");

                        alert2.setView(edittexts);

                        edittexts.setText(answers.getAnswer());

                        alert2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Forums database
                                Questionref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions");
                                Questionref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                            if (snapshot.hasChild("answers")) {

                                                for (DataSnapshot snap : snapshot.child("answers").getChildren()) {

                                                    String uid = snap.child("uid").getValue(String.class);
                                                    String ans = snap.child("answer").getValue(String.class);

                                                    if (uid.equals(fuser.getUid()) && answers.getAnswer().equals(ans)) {

                                                        snap.getRef().child("answer").setValue(edittexts.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {

                                                                    refs = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("Replies");
                                                                    refs.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                                                String uid = snapshot.child("uid").getValue(String.class);
                                                                                String ans = snapshot.child("answer").getValue(String.class);

                                                                                if (uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && answers.getAnswer().equals(ans)) {
                                                                                    snapshot.getRef().child("answer").setValue(edittexts.getText().toString());
                                                                                    Toast.makeText(getActivity(), "comment edited successfully", Toast.LENGTH_SHORT).show();

                                                                                    if (userType.equals("customer")) {
                                                                                        startActivity(new Intent(getContext(), ForumActivity.class));
                                                                                    } else {
                                                                                        startActivity(new Intent(getContext(), AdminForumActivity.class));
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                } else  {
                                                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", null);
                        alert2.show();
                    }
                });

            }
        };
        editList.setAdapter(RepliesAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        RepliesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        RepliesAdapter.stopListening();
    }

    public static class RepliesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CircleImageView post_que_user_image;
        ImageButton deleteBtn, editBtns;

        public RepliesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            editBtns = (ImageButton) mView.findViewById(R.id.editBtns);
            deleteBtn = (ImageButton) mView.findViewById(R.id.deleteBtn);
            post_que_user_image = (CircleImageView) mView.findViewById(R.id.post_que_user_image);
        }

        public void setUsername(String username){
            TextView myuserName = (TextView) mView.findViewById(R.id.ans_username);
            myuserName.setText(username);
        }
        public void setAnswer(String answer){
            TextView myanswer = (TextView) mView.findViewById(R.id.Answer_text);
            myanswer.setText(answer);
        }
        public void setProfileimage(Context ctx, String profileImg){
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_ans_img);
            Picasso.with(ctx).load(profileImg).into(image);
        }
    }
}