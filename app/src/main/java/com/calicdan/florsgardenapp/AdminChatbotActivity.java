package com.calicdan.florsgardenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminChatbotActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModal> chatsModalArrayList;
    private ChatRVAdapter chatRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chatbot);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat Bot");

        chatsRV = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.iEdtMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);
        chatsModalArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatsModalArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        chatsRV.setLayoutManager(manager);
        chatsRV.setAdapter(chatRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(AdminChatbotActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());
                chatsRV.scrollToPosition(chatRVAdapter.getItemCount()-1);
                userMsgEdt.setText("");
            }
        });
    }

    private void getResponse(String message){
        chatsModalArrayList.add(new ChatsModal(message,USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "\n" +
                "http://api.brainshop.ai/get?bid=168816&key=hmpdyPp1ebPvclyt&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if(response.isSuccessful()){
                    MsgModal modal = response.body();
                    chatsModalArrayList.add(new ChatsModal(modal.getCnt(),BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                    chatsRV.scrollToPosition(chatRVAdapter.getItemCount()-1);
                }
            }
            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatsModalArrayList.add(new ChatsModal("Please revert your question",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
                chatsRV.scrollToPosition(chatRVAdapter.getItemCount()-1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.usersChat:
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminChatbotActivity.this);
                builder.setMessage("Check customer inquiries?");
                builder.setTitle("Go to chat?");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    startActivity(new Intent(AdminChatbotActivity.this, AdminChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            case R.id.edit:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminChatbotActivity.this);
                builder1.setMessage("Would you like to edit the answers of your chat bot? Clicking yes will redirect you to the chat bot API webpage.");
                builder1.setTitle("Edit chat bot?");
                builder1.setCancelable(false);
                builder1.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    clicked_button("https://brainshop.ai/user");
                });

                builder1.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                return true;
            case R.id.home:
                startActivity(new Intent(AdminChatbotActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        invalidateOptionsMenu();
        menu.findItem(R.id.usersChat).setVisible(true);
        menu.findItem(R.id.edit).setVisible(true);
        menu.findItem(R.id.home).setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    public void clicked_button(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}