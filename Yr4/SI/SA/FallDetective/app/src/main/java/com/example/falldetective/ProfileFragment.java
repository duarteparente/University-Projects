package com.example.falldetective;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment implements ContactListViewInterface {


    List<ContactInfoModel> contacts = new ArrayList<>();
    ContactListViewAdapter adapter;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private TextView profileName, profileEmail, profileGender, profileBirthDate;
    private RecyclerView recyclerContacts;
    private ProgressBar loadingProgressBar;
    private View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Check if the view is already inflated
        if (view == null) {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_profile, container, false);

            profileName = view.findViewById(R.id.profileName);
            profileEmail = view.findViewById(R.id.profileEmail);
            profileGender = view.findViewById(R.id.profileGender);
            profileBirthDate = view.findViewById(R.id.profileBirthDate);
            recyclerContacts = view.findViewById(R.id.contactsRecyclerView);
            loadingProgressBar = view.findViewById(R.id.loadingProgressBar);

            auth = FirebaseAuth.getInstance();
            database = FirebaseDatabase.getInstance();

            loadingProgressBar.setVisibility(View.VISIBLE);

            if (auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();

                database.getReference("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            if (dataSnapshot.exists()) {
                                UserModel user = dataSnapshot.getValue(UserModel.class);
                                if (user != null) {
                                    profileName.setText(user.getFullName());
                                    profileName.setVisibility(View.VISIBLE);
                                    profileEmail.setText(user.getEmail());
                                    profileEmail.setVisibility(View.VISIBLE);
                                    profileGender.setText(user.getGender());
                                    profileGender.setVisibility(View.VISIBLE);
                                    profileBirthDate.setText(user.getBirthDate());
                                    profileBirthDate.setVisibility(View.VISIBLE);
                                    adapter.setItems(user.getContacts());
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                // User data not found
                            }
                        } else {
                            // Handle unsuccessful task
                        }

                        loadingProgressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize RecyclerView and adapter
        adapter = new ContactListViewAdapter(this, getContext(), contacts);
        recyclerContacts.setAdapter(adapter);
        recyclerContacts.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(int position) {

    }
}