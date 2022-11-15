package com.calicdan.florsgardenapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.calicdan.florsgardenapp.Fragments.AdminProfileFragment;
import com.calicdan.florsgardenapp.Fragments.AdminSettingsFragment;
import com.calicdan.florsgardenapp.Fragments.ProfileFragment;
import com.calicdan.florsgardenapp.Fragments.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfileActivity extends AppCompatActivity implements View.OnClickListener {

    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,imageViewProfile;
    FloatingActionButton imageRecog;

    CircleImageView profile_image;
    TextView username;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        String userType = "";

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

        //buttons();
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.view_pager);

        userType = getIntent().getStringExtra("userType");
        Log.i(TAG,"--------------------------------"+userType);
        AdminProfileFragment fragment = new AdminProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userType1", userType);
        fragment.setArguments(bundle);

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AdminProfileActivity.ViewPagerAdapter viewPagerAdapter = new AdminProfileActivity.ViewPagerAdapter(getSupportFragmentManager());

                viewPagerAdapter.addFragment(fragment, "User Details");
                viewPagerAdapter.addFragment(new AdminSettingsFragment(), "Settings");

                viewPager.setAdapter(viewPagerAdapter);

                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homebtn:
                startActivity(new Intent(AdminProfileActivity.this, Home.class));
                break;
            case R.id.forumbtn:
                startActivity(new Intent(AdminProfileActivity.this, AdminForumActivity.class));
                break;
            case R.id.storebtn:
                startActivity(new Intent(AdminProfileActivity.this, AdminStoreActivity.class));
                break;
            case R.id.notificationbtn:
                Toast.makeText(this, "Not yet available!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.chatbtn:
                startActivity(new Intent(AdminProfileActivity.this, AdminChatbotActivity.class));
                break;
            case R.id.imageViewProfile:
                startActivity(new Intent(AdminProfileActivity.this, AdminProfileActivity.class));
                break;
            case R.id.imageRecog:
                startActivity(new Intent(AdminProfileActivity.this, ImageRecognitionHome.class));
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        // Ctrl + O

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}