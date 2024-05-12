package com.example.motiontrack;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DataCollectorViewInterface {

    ArrayList<ActivityTypeModel> activities = new ArrayList<>();

    int[] activitiesImages = {R.drawable.walking, R.drawable.running, R.drawable.sitting, R.drawable.laying, R.drawable.falling};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.activitiesRecyclerView);

        setActivities();

        DataCollectorViewAdapter adapter = new DataCollectorViewAdapter(this, this.activities, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setActivities(){
        String[] activitiesDescription = getResources().getStringArray(R.array.activities);

        for(int i=0; i<activitiesDescription.length; i++){
            this.activities.add(new ActivityTypeModel(activitiesDescription[i],
                    this.activitiesImages[i]));
        }

    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(MainActivity.this, SensorActivity.class);
        intent.putExtra("Activity", this.activities.get(position).description);
        startActivity(intent);

    }
}