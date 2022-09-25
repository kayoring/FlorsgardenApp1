package com.calicdan.florsgardenapp;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
<<<<<<< Updated upstream
=======
import android.widget.Button;

>>>>>>> Stashed changes
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends AppCompatActivity {
<<<<<<< Updated upstream
    View homebtn,forumbtn,storebtn,notificationbtn,chatbtn,profilebtn;
    FloatingActionButton imageRecog;
=======

    View storebtn;
    Button btnWorms;
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttons();
    }

    private void buttons() {
        homebtn = findViewById(R.id.homebtn);
        forumbtn = findViewById(R.id.forumbtn);
        storebtn = findViewById(R.id.storebtn);
<<<<<<< Updated upstream
        imageRecog = (FloatingActionButton) findViewById(R.id.imageRecog);
        notificationbtn = findViewById(R.id.notificationbtn);
        chatbtn = findViewById(R.id.chatbtn);
        profilebtn = findViewById(R.id.profilebtn);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Home.class));

            }
        });

        forumbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });

        storebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });

=======
        btnWorms = findViewById(R.id.btnWorms);
>>>>>>> Stashed changes
        imageRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ImageRecognition.class));
            }
        });

        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });
        btnWorms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Worms.class));
            }
        });
        Button btnFlorsGarden = (Button)findViewById(R.id.btnFlorsGarden);

        btnFlorsGarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked_button("https://florsgarden.com");

        }

        });
    }
        public void clicked_button(String url){


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);

        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, StoreActivity.class));

            }
        });
    }
}
