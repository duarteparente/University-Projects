package com.example.motiontrack;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private Button start, stop;
    private Chronometer chronometer;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private List<AccelerometerDataModel> accData;
    private String activity;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        start = findViewById(R.id.startAccelerometer);
        stop = findViewById(R.id.stopAccelerometer);
        chronometer = findViewById(R.id.chronometer);
        activity = getIntent().getStringExtra("Activity");
        accData = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference(activity);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        stop.setAlpha(0);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.animate().alpha(1).translationY(-80).setDuration(300).start();
                start.animate().alpha(0).setDuration(300).start();

                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

                sensorManager.registerListener(SensorActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sensorManager.unregisterListener(SensorActivity.this);

                chronometer.stop();

                String db_key = String.format("%s_%d", activity, accData.get(0).timestamp);

                AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);
                builder.setTitle("Registo efetuado!");
                builder.setMessage("Confirmar gravação?");
                builder.setCancelable(false);
                builder.setPositiveButton("Gravar", (DialogInterface.OnClickListener) (dialog, which) -> {
                    for(int i=0; i<accData.size(); i++){
                        reference.child(db_key).child(String.valueOf(accData.get(i).timestamp)).setValue(accData.get(i));
                    }
                    Intent intent = new Intent(SensorActivity.this, SensorActivity.class);
                    intent.putExtra("Activity", activity);
                    startActivity(intent);
                });

                builder.setNegativeButton("Apagar", (DialogInterface.OnClickListener) (dialog, which) -> {
                    Intent intent = new Intent(SensorActivity.this, MainActivity.class);
                    startActivity(intent);
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        long timestamp = System.currentTimeMillis();

        AccelerometerDataModel data = new AccelerometerDataModel(event.values[0],
                event.values[1], event.values[2], event.accuracy, timestamp);

        this.accData.add(data);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
