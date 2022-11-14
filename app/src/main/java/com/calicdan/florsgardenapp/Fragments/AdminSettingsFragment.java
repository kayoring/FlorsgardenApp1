package com.calicdan.florsgardenapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.calicdan.florsgardenapp.ChangeEmailActivity;
import com.calicdan.florsgardenapp.R;
import com.calicdan.florsgardenapp.RegisterAdminActivity;
import com.calicdan.florsgardenapp.ResetPasswordActivity;

public class AdminSettingsFragment extends Fragment implements View.OnClickListener{

    TextView cPass, cUser, cContact, cAddress, addAdmin, notifs, allowNotifs, cEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        cPass = view.findViewById(R.id.cPass);
        addAdmin = view.findViewById(R.id.addAdmin);
        cEmail = view.findViewById(R.id.cEmail);
        /*
        cUser = view.findViewById(R.id.cUser);
        cContact = view.findViewById(R.id.cContact);
        cAddress = view.findViewById(R.id.cAddress);
        notifs = view.findViewById(R.id.cNotifs);
        allowNotifs = view.findViewById(R.id.cAllowNotifs);
         */

        cPass.setOnClickListener(this);
        addAdmin.setOnClickListener(this);
        cEmail.setOnClickListener(this);
        /*
        cUser.setOnClickListener(this);
        cContact.setOnClickListener(this);
        cAddress.setOnClickListener(this);
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
            case R.id.addAdmin:
                startActivity(new Intent(getActivity(), RegisterAdminActivity.class));
                break;
/*
            case R.id.cUser:
                startActivity(new Intent(getActivity(), Home.class));
                break;

            case R.id.cContact:
                startActivity(new Intent(getActivity(), Home.class));
                break;

            case R.id.cAddress:
                startActivity(new Intent(getActivity(), Home.class));
                break;


            case R.id.cNotifs:
                startActivity(new Intent(getActivity(), Home.class));
                break;

            case R.id.cAllowNotifs:
                startActivity(new Intent(getActivity(), Home.class));
                break;

 */
        }
    }
}