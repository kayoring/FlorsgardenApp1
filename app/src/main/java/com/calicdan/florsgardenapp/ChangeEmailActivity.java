package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailActivity extends AppCompatActivity {

    Button changeemail;
    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change email");

        EditText email = findViewById(R.id.email);
        EditText password = findViewById(R.id.logpass);

        pdialog  = new ProgressDialog(this);

        changeemail = findViewById(R.id.changeemail);

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeemail(email.getText().toString(), password.getText().toString());
                pdialog.setTitle("Change email");
                pdialog.setMessage("Please wait for the system to change your email...");
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.show();
            }
        });

    }

    EditText change;

    private void changeemail(String email, final String password) {

        change = findViewById(R.id.change);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("value", "User re-authenticated.");
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.updateEmail(change.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ChangeEmailActivity.this, "Email Changed" + " Current Email is " + change.getText().toString(), Toast.LENGTH_LONG).show();
                                pdialog.dismiss();
                            } else {
                                Toast.makeText(ChangeEmailActivity.this, "Task failed successfully!", Toast.LENGTH_SHORT).show();
                                pdialog.dismiss();
                            }
                        }
                    });
                } else {
                    EditText email = findViewById(R.id.email);
                    EditText password = findViewById(R.id.logpass);
                    EditText change = findViewById(R.id.change);;
                    email.setText("");
                    password.setText("");
                    change.setText("");
                    email.requestFocus();
                    pdialog.dismiss();
                    Toast.makeText(ChangeEmailActivity.this, "Entered credentials is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}