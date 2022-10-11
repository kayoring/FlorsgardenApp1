package com.calicdan.florsgardenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class checkOutActivity extends AppCompatActivity {
    TextView backToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        backToStore = findViewById(R.id.backToStore);
        backToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(checkOutActivity.this,StoreActivity.class);
                checkOutActivity.this.startActivity(i);
            }
        });
    }
}