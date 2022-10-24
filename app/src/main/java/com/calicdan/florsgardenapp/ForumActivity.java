package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.calicdan.florsgardenapp.Fragments.ForumFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp, new ForumFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.bforum:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp,new ForumFragment()).commit();
                    break;

                case R.id.bhome:
                    Intent intent=new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.badd:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragDisp,new AddInquiryActivity()).commit();
                    break;

            }
            return true;
        }
    };
}