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

import com.calicdan.florsgardenapp.Fragments.EditFragment;
import com.calicdan.florsgardenapp.Fragments.ForumFragment;
import com.calicdan.florsgardenapp.Fragments.RepliesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditActivity.this, ForumActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp, new EditFragment()).commit();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homebtn:
                startActivity(new Intent(EditActivity.this, HomeUser.class));
                break;
            case R.id.forumbtn:
                startActivity(new Intent(EditActivity.this, ForumActivity.class));
                break;
            case R.id.storebtn:
                startActivity(new Intent(EditActivity.this, StoreActivity.class));
                break;
            case R.id.notificationbtn:
                Toast.makeText(this, "Not yet available!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chatbtn:
                startActivity(new Intent(EditActivity.this, ChatbotActivity.class));
                break;
            case R.id.imageViewProfile:
                startActivity(new Intent(EditActivity.this, ProfileActivity.class));
                break;
            case R.id.imageRecog:
                startActivity(new Intent(EditActivity.this, ImageRecognitionHome.class));
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
                startActivity(new Intent(EditActivity.this, HomeUser.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                return true;
            case  R.id.addQuestion:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp,new AddInquiryActivity()).commit();
                break;
            case  R.id.forums:
                startActivity(new Intent(EditActivity.this, ForumActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            /*
            case  R.id.editPost:
                startActivity(new Intent(EditActivity.this, EditActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
            case  R.id.search:
                Toast.makeText(this, "Not yet available!", Toast.LENGTH_SHORT).show();
                break;

             */
        }

        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        menu.findItem(R.id.home).setVisible(true);
        menu.findItem(R.id.addQuestion).setVisible(true);
        menu.findItem(R.id.editPost).setVisible(false);
        menu.findItem(R.id.editRep).setVisible(false);
        //menu.findItem(R.id.search).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

}