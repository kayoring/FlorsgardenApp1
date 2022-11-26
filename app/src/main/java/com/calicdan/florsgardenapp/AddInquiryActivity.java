package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.calicdan.florsgardenapp.Fragments.ForumFragment;
import com.calicdan.florsgardenapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddInquiryActivity extends Fragment {

    EditText question;
    Button addBtn, backBtn;
    FirebaseUser fuser;
    DatabaseReference UsersReference,UsersRef,refs;
    String username, profileImage,userType,currentUserID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_add_inquiry, container, false);

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        UsersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid());

        question=view.findViewById(R.id.add_question_txt);
        addBtn=view.findViewById(R.id.add_question_btn);
        backBtn=view.findViewById(R.id.backBtn);

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
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fuser = FirebaseAuth.getInstance().getCurrentUser();
                refs = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");

                refs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userType = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                new AlertDialog.Builder(getActivity())
                        .setTitle("Forums")
                        .setMessage("Go back to forums section?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                question.setText("");
                                if (userType.equals("admin")) {
                                    startActivity(new Intent(getContext(), AdminForumActivity.class));
                                } else {
                                    startActivity(new Intent(getContext(), ForumActivity.class));
                                }
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fuser = FirebaseAuth.getInstance().getCurrentUser();
                refs = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("usertype");

                refs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userType = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


                new AlertDialog.Builder(getActivity())
                        .setTitle("Forums")
                        .setMessage("Are you sure you want to upload this discussion?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addNewQuestion();
                                question.setText("");
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
            }

        });

        return view;
    }


    public void addNewQuestion() {
        final String userQuestion=question.getText().toString();

        if(TextUtils.isEmpty(userQuestion)){
            Toast.makeText(getActivity(),"Please write your question.",Toast.LENGTH_LONG).show();
            return;
        }

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yy");
        final String saveCurrentDate = currentDate.format(calForDate.getTime());
        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        final String  saveCurrentTime = currentTime.format(calForDate.getTime());
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        UsersReference=database.getReference().child("Forums").child("Questions");

        refs = database.getReference().child("Users").child(fuser.getUid()).child("Posts");

        String pushID = UsersReference.push().getKey();

        HashMap userMap=new HashMap();
        userMap.put("id",currentUserID);
        userMap.put("username",username);
        userMap.put("question",userQuestion);
        userMap.put("date",saveCurrentDate);
        userMap.put("time",saveCurrentTime);
        userMap.put("imageURL",profileImage);
        userMap.put("userType",userType);

        UsersReference.child(pushID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    refs.push().updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getActivity(),"Question added successfully",Toast.LENGTH_LONG).show();
                                if (userType.equals("admin")) {
                                    startActivity(new Intent(getContext(), AdminForumActivity.class));
                                } else {
                                    startActivity(new Intent(getContext(), ForumActivity.class));
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(),task.getException().toString(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
