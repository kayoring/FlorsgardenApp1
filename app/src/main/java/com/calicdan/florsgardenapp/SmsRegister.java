package com.calicdan.florsgardenapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SmsRegister extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser, txtPrivacy, loginA, smsReg;
    private EditText editTextFullName, editTextEmail, editTextPassword, editTextConfirmPass;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private CheckBox check_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_register_);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        final EditText inputMobile = findViewById(R.id.contacNo);
        final Button buttonGetOtp = findViewById(R.id.btnsendotp);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        buttonGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputMobile.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SmsRegister.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (check_box.isChecked()) {
                    progressBar.setVisibility(View.VISIBLE);
                    buttonGetOtp.setVisibility(View.INVISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+63" + inputMobile.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            SmsRegister.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonGetOtp.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonGetOtp.setVisibility(View.VISIBLE);
                                    Toast.makeText(SmsRegister.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    buttonGetOtp.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent(getApplicationContext(), VerifyOtp.class);
                                    intent.putExtra("mobile", inputMobile.getText().toString());
                                    intent.putExtra("verificationId", verificationId);
                                    startActivity(intent);

                                }
                            }
                    );
                } else {
                    Toast.makeText(SmsRegister.this, "Please agree to the privacy policy", Toast.LENGTH_SHORT).show();
                }

            }
        });

        banner = (TextView) findViewById(R.id.textView6);
        banner.setOnClickListener(this);
        loginA = (TextView) findViewById(R.id.loginA);
        loginA.setOnClickListener(this);
        smsReg = (TextView) findViewById(R.id.smsReg);
        smsReg.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.name);
        editTextEmail = (EditText) findViewById(R.id.emailA);
        editTextPassword = (EditText) findViewById(R.id.passwordA);
        editTextConfirmPass = (EditText) findViewById(R.id.confirmPass);
        check_box = findViewById(R.id.check_box);
        txtPrivacy = (TextView) findViewById(R.id.txtPrivacy);

        Dialog dialog = new Dialog(SmsRegister.this);
        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.privacy_policy_popup);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes();
                dialog.show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginA:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.smsReg:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.register:
                Toast.makeText(this, "OTP sent", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void registerUser(final String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            //User user = new User(fullName, contact, email);
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", fullName);
                            hashMap.put("contact", "null");
                            hashMap.put("email", email);
                            hashMap.put("password", password);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", fullName.toLowerCase());
                            hashMap.put("usertype", "customer");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(SmsRegister.this, "User has been registered successfully!, Check email to verify your account then proceed to login", Toast.LENGTH_LONG).show();
                                    firebaseUser.sendEmailVerification();
                                }
                            });
                        } else {
                            Toast.makeText(SmsRegister.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}