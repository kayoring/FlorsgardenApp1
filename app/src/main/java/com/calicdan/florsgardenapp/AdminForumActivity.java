package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.calicdan.florsgardenapp.Fragments.AdminEditFragment;
import com.calicdan.florsgardenapp.Fragments.AdminEditRepliesFragment;
import com.calicdan.florsgardenapp.Fragments.EditFragment;
import com.calicdan.florsgardenapp.Fragments.ForumFragment;
import com.calicdan.florsgardenapp.Fragments.PostReqFragment;
import com.calicdan.florsgardenapp.Fragments.RepliesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AdminForumActivity extends AppCompatActivity implements View.OnClickListener {

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Discussion Board");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminForumActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        homebtn = findViewById(R.id.homebtn);
        forumbtn = findViewById(R.id.forumbtn);
        storebtn = findViewById(R.id.storebtn);
        notificationbtn = findViewById(R.id.notificationbtn);
        chatbtn = findViewById(R.id.chatbtn);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        imageRecog = findViewById(R.id.imageRecog);

        homebtn.setOnClickListener(this);
        forumbtn.setOnClickListener(this);
        storebtn.setOnClickListener(this);
        notificationbtn.setOnClickListener(this);
        chatbtn.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
        imageRecog.setOnClickListener(this);

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp, new AdminForumFragment()).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homebtn:
                startActivity(new Intent(AdminForumActivity.this, Home.class));
                break;
            case R.id.forumbtn:
                startActivity(new Intent(AdminForumActivity.this, AdminForumActivity.class));
                break;
            case R.id.storebtn:
                startActivity(new Intent(AdminForumActivity.this, AdminStoreActivity.class));
                break;
            case R.id.notificationbtn:
                Toast.makeText(this, "Not yet available!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chatbtn:
                startActivity(new Intent(AdminForumActivity.this, AdminChatbotActivity.class));
                break;
            case R.id.imageViewProfile:
                startActivity(new Intent(AdminForumActivity.this, AdminProfileActivity.class));
                break;
            case R.id.imageRecog:
                startActivity(new Intent(AdminForumActivity.this, ImageRecognitionHome.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  R.id.home:
                startActivity(new Intent(AdminForumActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
            case  R.id.addQuestion:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp,new AddInquiryActivity()).commit();
                break;
            case  R.id.editPost:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp, new EditFragment()).commit();
                break;
            case  R.id.editRep:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp, new RepliesFragment()).commit();
                break;
            case  R.id.postRequest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp,new PostReqFragment()).commit();
                break;
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        menu.findItem(R.id.forums).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}