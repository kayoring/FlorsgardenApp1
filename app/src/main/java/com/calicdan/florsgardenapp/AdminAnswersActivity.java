package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.calicdan.florsgardenapp.Model.Answers;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminAnswersActivity extends AppCompatActivity {

    private RecyclerView AnsList;
    private ImageButton PostAnsButton;
    private EditText AnswerInputText;

    private String Post_Key,current_user_id;
    private FirebaseAuth mAuth;

    private DatabaseReference fuser,Questionref;
    FirebaseRecyclerAdapter<Answers, AnswersActivity.AnswersViewHolder> answersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answers);
        Post_Key = getIntent().getExtras().get("Postkey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        fuser = FirebaseDatabase.getInstance().getReference().child("Users");
        Questionref = FirebaseDatabase.getInstance().getReference().child("Forums").child("Questions").child(Post_Key).child("answers");

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Discussion Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminAnswersActivity.this, AdminForumActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        AnsList = (RecyclerView) findViewById(R.id.ans_list);
        AnsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        AnsList.setLayoutManager(linearLayoutManager);



        AnswerInputText = (EditText) findViewById(R.id.answersInput);
        PostAnsButton = (ImageButton) findViewById(R.id.post_ans_button);


        PostAnsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fuser.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String userName = dataSnapshot.child("username").getValue().toString();
                            String profileImg = dataSnapshot.child("imageURL").getValue().toString();
                            ValidateAnswer(userName,profileImg);
                            AnswerInputText.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions answerOptions = new FirebaseRecyclerOptions.Builder<Answers>().setQuery(Questionref, Answers.class).build();

        answersAdapter = new FirebaseRecyclerAdapter<Answers, AnswersActivity.AnswersViewHolder>(answerOptions) {

            @NonNull
            @Override
            public AnswersActivity.AnswersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_all_answers, parent, false);
                return new AnswersActivity.AnswersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AnswersActivity.AnswersViewHolder answersViewHolder, @SuppressLint("RecyclerView") int position, @NonNull Answers answers) {
                answersViewHolder.setUsername(answers.getUsername());
                answersViewHolder.setProfileimage(getApplicationContext(),answers.getProfileimage());
                answersViewHolder.setAnswer(answers.getAnswer());
                answersViewHolder.setDate(answers.getDate());
                answersViewHolder.setTime(answers.getTime());

                answersViewHolder.delBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new android.app.AlertDialog.Builder(answersViewHolder.itemView.getContext())
                                .setTitle("Delete Content")
                                .setMessage("Would you like to delete this answer/reply?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        answersAdapter.getRef(position).removeValue();
                                        Toast.makeText(AdminAnswersActivity.this, "Answer/reply removed successfully", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });
            }
        };
        AnsList.setAdapter(answersAdapter);

        super.onStart();
        answersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        answersAdapter.stopListening();
    }

    public static class AnswersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageButton delBtn;


        public AnswersViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            delBtn = (ImageButton) mView.findViewById(R.id.deleteBtn);
        }
        public void setUsername(String username){
            TextView myuserName = (TextView) mView.findViewById(R.id.ans_username);
            myuserName.setText(username);
        }
        public void setAnswer(String answer){
            TextView myanswer = (TextView) mView.findViewById(R.id.Answer_text);
            myanswer.setText(answer);
        }
        public void setDate(String date){
            TextView mydate = (TextView) mView.findViewById(R.id.ans_date);
            mydate.setText(date);

        }
        public void setTime(String time) {
            TextView mytime = (TextView) mView.findViewById(R.id.ans_time);
            mytime.setText(time);
        }
        public void setProfileimage(Context ctx, String profileImg){
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_ans_img);
            Picasso.with(ctx).load(profileImg).into(image);
        }
    }

    private void ValidateAnswer(String userName,String profileImg) {
        String answerText = AnswerInputText.getText().toString();

        String key = Questionref.getKey();

        if (TextUtils.isEmpty(answerText)){
            Toast.makeText(this,"Please Write answer to post!",Toast.LENGTH_SHORT).show();
        }

        else {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yy");
            final String saveCurrentDate = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calForDate.getTime());

            final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime;

            HashMap answersMap = new HashMap();
            answersMap.put("uid", current_user_id);
            answersMap.put("answer", answerText);
            answersMap.put("date", saveCurrentDate);
            answersMap.put("time", saveCurrentTime);
            answersMap.put("username", userName);
            answersMap.put("profileimage", profileImg);

            Questionref.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Questionref.push().updateChildren(answersMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AdminAnswersActivity.this, "Your answer submitted Successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AdminAnswersActivity.this, "Error occured, try again....", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}