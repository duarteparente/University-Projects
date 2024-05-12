package com.example.falldetective;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ConfigsFragment extends Fragment {

    private Button editProfile, editContacts, changePassword, logout;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configs, container, false);

        editProfile = view.findViewById(R.id.editProfile);
        editContacts = view.findViewById(R.id.editContacts);
        changePassword = view.findViewById(R.id.changePassword);
        logout = view.findViewById(R.id.logout);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            });

            editProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                }
            });

            editContacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), EditContactsActivity.class));
                }
            });

            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
                }
            });

        }
        else{
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        return view;
    }
}