package com.example.falldetective;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class FallRecordsFragment extends Fragment implements ContactListViewInterface{

    List<FallRecordModel> falls = new ArrayList<>();
    FallRecordsViewAdapter adapter;
    private RecyclerView recyclerFalls;
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fall_records, container, false);

        recyclerFalls = view.findViewById(R.id.recyclerFalls);

        adapter = new FallRecordsViewAdapter(this, getContext(), falls);
        recyclerFalls.setAdapter(adapter);
        recyclerFalls.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();

            database.getReference("falls").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                FallRecordModel fall = child.getValue(FallRecordModel.class);
                                falls.add(fall);
                            }

                            adapter.setItems(falls);
                            adapter.notifyDataSetChanged();


                        } else {
                            // User data not found
                        }
                    } else {
                        // Handle unsuccessful task
                    }

                }
            });
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }

        return view;
    }

    @Override
    public void onItemClick(int position) {

    }
}