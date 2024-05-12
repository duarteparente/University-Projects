package com.example.falldetective;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ForegroundService extends Service implements SensorEventListener {
    private static final int NOTIFICATION_ID = 123;
    private static final String CHANNEL_ID = "1";
    private PowerManager.WakeLock wakeLock;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor proximitySensor;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private String fullname, contacts, userId;
    private ArrayList<Float> yValues = new ArrayList<>(), magnitude = new ArrayList<>();
    private ArrayList<Long> timestamps = new ArrayList<>();
    private long timestamp;
    private boolean alert;

    private boolean accelerometerActive = false, alarmScreen = false;



    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        alert = false;

        userId = auth.getCurrentUser().getUid();

        database.getReference("users").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot dataSnapshot = task.getResult();
                    if (dataSnapshot.exists()) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        fullname = user.getFullName().toUpperCase();

                        StringBuilder stringBuilder = new StringBuilder();
                        List<ContactInfoModel> contactos = user.getContacts();
                        for(int i=0; i<contactos.size()-1; i++){
                            stringBuilder.append(contactos.get(i).number).append(';');
                        }
                        stringBuilder.append(contactos.get(contactos.size()-1).number);
                        contacts = stringBuilder.toString();


                    } else {
                        // User data not found
                        Toast.makeText(ForegroundService.this, "No user data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle unsuccessful task
                    Toast.makeText(ForegroundService.this, "Task bad", Toast.LENGTH_SHORT).show();
                }

            }
        });

        startForeground(NOTIFICATION_ID, buildNotification());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "FallDetective::MyWakelockTag");
        wakeLock.acquire();

        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    private Notification buildNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Fall Detective", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        Intent intent = new Intent(this, MainActivity.class) ;
        PendingIntent contentIntent = PendingIntent. getActivity (this, 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT |  PendingIntent.FLAG_MUTABLE) ;
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Fall Detective")
                .setContentText("Encontra-se protegido")
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
        stopForeground(true);
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float proximityValue = event.values[0];
            if (proximityValue == 0.0) {

                if (!accelerometerActive) {
                    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    accelerometerActive = true;
                }
            } else {

                if (accelerometerActive) {
                    sensorManager.unregisterListener(this, accelerometer);
                    accelerometerActive = false;
                }
            }

            Log.d("FOREGROUND", "[PROXIMITY] [ " + proximityValue + " ]");

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float accelerationMagnitude = (float) Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2]);

            if (accelerationMagnitude >= 20 && !alert){
                alert = true;
                yValues.add(event.values[1]);
                magnitude.add(accelerationMagnitude);
                timestamp = System.currentTimeMillis();
                timestamps.add(timestamp);
            }
            else if (alert && System.currentTimeMillis() - timestamp < 750){
                yValues.add(event.values[1]);
                magnitude.add(accelerationMagnitude);
                timestamps.add(System.currentTimeMillis());
            }
            else if (alert && System.currentTimeMillis() - timestamp < 1000 && accelerationMagnitude >= 20){
                timestamp = System.currentTimeMillis();
                yValues.add(event.values[1]);
                magnitude.add(accelerationMagnitude);
                timestamp = System.currentTimeMillis();
                timestamps.add(timestamp);
            }
            else if (alert && System.currentTimeMillis() - timestamp > 2000 && detectFall() && !alarmScreen) {

                Intent intent = new Intent(this, AlarmActivity.class);
                intent.putExtra("Fullname", fullname);
                intent.putExtra("Contacts", contacts);
                intent.putExtra("UserID", userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                alarmScreen = true;

            }
            Log.d("FOREGROUND", "[ACCELEROMETER] [ " + event.values[0] + "," + event.values[1] + " ]");
        }

    }

    private boolean detectFall() {
        Log.d("TESTE", "estou aqui");
        int maxMagnitudeIndex = 0;
        float maxMagnitude = magnitude.get(maxMagnitudeIndex);
        for(int i=0; i<magnitude.size(); i++){
            if (magnitude.get(i) > maxMagnitude){
                maxMagnitude = magnitude.get(i);
                maxMagnitudeIndex = i;
            }
        }
        int startIndex = maxMagnitudeIndex;

        while (startIndex > 0 && (timestamps.get(maxMagnitudeIndex) - timestamps.get(startIndex)) < 1000) {
            startIndex--;
        }

        int endIndex = maxMagnitudeIndex;
        while (endIndex < timestamps.size() - 1 && (timestamps.get(endIndex) - timestamps.get(maxMagnitudeIndex)) < 1000) {
            endIndex++;
        }

        float totalMagnitude = 0;
        for (float m : magnitude){
            totalMagnitude += m;
        }
        float meanMagnitude = totalMagnitude / magnitude.size();



        float maxAfter = magnitude.get(0);
        float minAfter = magnitude.get(0);
        for(int i=0; i<magnitude.size(); i++){
            if (magnitude.get(i) > maxAfter){
                maxAfter = magnitude.get(i);
            } else if (magnitude.get(i) < minAfter) {
                minAfter = magnitude.get(i);
            }
        }

        float afterFall = maxAfter - minAfter;

        return meanMagnitude >= 9.5;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
