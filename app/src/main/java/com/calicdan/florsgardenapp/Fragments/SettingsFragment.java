package com.calicdan.florsgardenapp.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.calicdan.florsgardenapp.ChangeAddressActivity;
import com.calicdan.florsgardenapp.ChangeEmailActivity;
import com.calicdan.florsgardenapp.ChangeUsernameActivity;
import com.calicdan.florsgardenapp.ContactNoActivity;
import com.calicdan.florsgardenapp.ForumActivity;
import com.calicdan.florsgardenapp.Home;
import com.calicdan.florsgardenapp.Login;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.RegisterAdminActivity;
import com.calicdan.florsgardenapp.ResetPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    TextView cPass, cUser, cContact, cAddress, addAdmin, notifs, allowNotifs, cEmail;
    Button logoutBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        cPass = view.findViewById(R.id.cPass);
        cEmail = view.findViewById(R.id.cEmail);
        cUser = view.findViewById(R.id.cUser);
        cContact = view.findViewById(R.id.cContact);
        cAddress = view.findViewById(R.id.cAddress);

          /*
        notifs = view.findViewById(R.id.cNotifs);
        allowNotifs = view.findViewById(R.id.cAllowNotifs);
         */

        logoutBtn = view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to log out?");
                builder.setTitle("Logout");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    getActivity().finish();
                });

                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
        cPass.setOnClickListener(this);
        //addAdmin.setOnClickListener(this);
        cEmail.setOnClickListener(this);

        cUser.setOnClickListener(this);
        cContact.setOnClickListener(this);
        cAddress.setOnClickListener(this);
           /*
        notifs.setOnClickListener(this);
        allowNotifs.setOnClickListener(this);
       */

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cPass:
                startActivity(new Intent(getActivity(), ResetPasswordActivity.class));
                break;

            case R.id.cEmail:
                startActivity(new Intent(getActivity(), ChangeEmailActivity.class));
                break;


            case R.id.cUser:
                startActivity(new Intent(getActivity(), ChangeUsernameActivity.class));
                break;

            case R.id.cContact:
                startActivity(new Intent(getActivity(), ContactNoActivity.class));
                break;

            case R.id.cAddress:
                startActivity(new Intent(getActivity(), ChangeAddressActivity.class));
                break;

             /*
            case R.id.cNotifs:
                startActivity(new Intent(getActivity(), Home.class));
                break;

            case R.id.cAllowNotifs:
                startActivity(new Intent(getActivity(), Home.class));
                break;
            case R.id.addAdmin:
                startActivity(new Intent(getActivity(), RegisterAdminActivity.class));
                break;

 */
        }
    }
}