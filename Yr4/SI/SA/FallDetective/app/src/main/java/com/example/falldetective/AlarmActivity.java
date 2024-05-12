package com.example.falldetective;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.ncorti.slidetoact.SlideToActView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private CircularProgressBar timer;
    private SlideToActView cancelSlide, alarmSlide;
    private TextView countdown;
    private int secondsLeft = 60, previousSoundMode;
    private Handler handler;
    private Runnable runnableTimer, runnableVibration;
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Vibrator vibrator;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private double latitude;
    private double longitude;
    private boolean fall;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("falls");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            );
        }

        Date d = new Date();
        date = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(d);

        latitude = -1;
        longitude = -1;

        setContentView(R.layout.activity_alarm);

        cancelSlide = findViewById(R.id.cancelSlide);
        alarmSlide = findViewById(R.id.alarmSlide);

        timer = findViewById(R.id.timer);
        countdown = findViewById(R.id.alarm_counter);
        countdown.setText(String.format("%s s", secondsLeft));

        timer.setProgressWithAnimation(secondsLeft, 500L);
        timer.setProgressMax(secondsLeft);

        handler = new Handler(Looper.getMainLooper());

        startAudioAlarm();

        runnableTimer = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                updateCountdown();
            }
        };

        runnableVibration = new Runnable() {
            @Override
            public void run() {
                startVibration();
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(runnableTimer, 1000);
        handler.postDelayed(runnableVibration, 2000);


        cancelSlide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                fall = false;
                stopAlarm();
            }
        });

        alarmSlide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                fall = true;
                getLocation();
                stopAlarm();
            }
        });

    }

    private void startAudioAlarm() {

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        mediaPlayer = MediaPlayer.create(this, alert);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        audioManager = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);
        previousSoundMode = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
    }

    private void startVibration() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        final VibrationEffect vibrationEffect1;

        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
                vibrator.cancel();
                vibrator.vibrate(vibrationEffect1);
            }
        }
    }

    private void updateCountdown() {
        if (secondsLeft >= 0) {
            countdown.setText(String.format("%s s", secondsLeft));
            timer.setProgress(secondsLeft--);
        } else {
            fall = true;
            getLocation();
            stopAlarm();
        }
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();

            sendSMSWithLocation(latitude, longitude);
        } else {
            sendSMSWithLocation(latitude, longitude);
        }
    }

    private void sendSMSWithLocation(double latitude, double longitude) {

        String message1 = "Aviso! Possível queda de " + getIntent().getStringExtra("Fullname");
        String message2 = "Localização: https://maps.google.com/maps?q=" + latitude + "," + longitude;

        String[] contacts = getIntent().getStringExtra("Contacts").split(";");

        for (String number : contacts){
            SmsManager.getDefault().sendTextMessage(number, null, message1, null, null);

            if(latitude != -1){
                SmsManager.getDefault().sendTextMessage(number, null, message2, null, null);
            }
        }


        Toast.makeText(this, "Os contactos de emergência foram informados", Toast.LENGTH_SHORT).show();
    }


    private void stopAlarm() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.cancel(123);

        FallRecordModel fallRecord = new FallRecordModel(date, fall, latitude, longitude);
        long timestamp = System.currentTimeMillis();
        reference.child(getIntent().getStringExtra("UserID")).child(String.valueOf(timestamp)).setValue(fallRecord);

        audioManager.setRingerMode(previousSoundMode);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
        handler.removeCallbacksAndMessages(null);

        Intent serviceIntent = new Intent(AlarmActivity.this, ForegroundService.class);
        AlarmActivity.this.stopService(serviceIntent);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        finish();
    }
}
