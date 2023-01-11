package com.calicdan.florsgardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private TextView register, forgotPass;
    private EditText editTextEmail, editTextPassword;
    private Button Login;
    private CheckBox showPass;

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    FirebaseUser fuser;
    DatabaseReference ref;

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //check if user is null


        if (firebaseUser != null){
            if (firebaseUser.isEmailVerified()) {
                Intent intent = new Intent(Login.this, SplashActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Check email to verify your account.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        forgotPass = (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(this);

        Login = (Button) findViewById(R.id.login);
        Login.setOnClickListener(this);

        showPass = (CheckBox) findViewById(R.id.showPass);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(!b) {
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.forgotPass:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.login:
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() ||password.isEmpty()){
                    if (email.isEmpty()) {
                        editTextEmail.requestFocus();
                    } else if (password.isEmpty()) {
                        editTextPassword.requestFocus();
                    }
                    Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6 ){
                    Toast.makeText(Login.this, "Minimum password length is 6 characters!", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Login.this, "Please provide valid email!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if (user.isEmailVerified()) {
                                Intent intent = new Intent(Login.this, SplashActivity.class);
                                startActivity(intent);
                                } else {
                                    user.sendEmailVerification();
                                    Toast.makeText(Login.this, "Check your email to verify your account and proceed to login.", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(com.calicdan.florsgardenapp.Login.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void userLogin(final String email, String password) {

    }
}