package com.calicdan.florsgardenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class InstructionHowToPay extends AppCompatActivity {
    TextView backToStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_how_to_pay);

        backToStore = findViewById(R.id.backToStore2);
        backToStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InstructionHowToPay.this,StoreActivity.class);
                InstructionHowToPay.this.startActivity(i);
            }
        });
        SpannableString ss1 = new SpannableString("Step 1. Send your payment through Gcash to 09772349477");
        ss1.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 43, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = (TextView) findViewById(R.id.step1);
        textView.setText(ss1);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);

        SpannableString ss2 = new SpannableString("Step 2. Take a screenshot of your finished payment. Click HERE for an example.");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(InstructionHowToPay.this, StoreActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss2.setSpan(clickableSpan, 58, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new UnderlineSpan(),58, 62,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss2.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 58, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView1 = (TextView) findViewById(R.id.step2);
        textView1.setText(ss2);
        textView1.setMovementMethod(LinkMovementMethod.getInstance());
        textView1.setHighlightColor(Color.TRANSPARENT);

        SpannableString ss3 = new SpannableString("Step 3. Send it to an Administrator HERE and wait for the confirmation and tracking number of your order from the Admin.");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(InstructionHowToPay.this, ChatActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss3.setSpan(clickableSpan1, 36, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new UnderlineSpan(),36, 40,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss3.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 36, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView2 = (TextView) findViewById(R.id.step3);
        textView2.setText(ss3);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setHighlightColor(Color.TRANSPARENT);

        //

        SpannableString ss4 = new SpannableString("Step 4. Use tracking number given by the admin to track your order at https://www.ordertracker.com/couriers/lalamove.");

        ss4.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 71, 117, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView3 = (TextView) findViewById(R.id.step4);
        textView3.setText(ss4);
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
        textView3.setHighlightColor(Color.TRANSPARENT);
    }
}