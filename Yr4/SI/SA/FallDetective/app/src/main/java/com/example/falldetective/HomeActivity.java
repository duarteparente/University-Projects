package com.example.falldetective;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.skyfishjy.library.RippleBackground;
import static android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;

public class HomeActivity extends AppCompatActivity {

    private RippleBackground rippleBackground;
    private MaterialButton startButton;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1002;
    private static final int FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 1003;
    private static final int POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE = 1004;
    private static final int ACCESS_NOTIFICATION_POLICY_PERMISSION_REQUEST_CODE = 1005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        rippleBackground = findViewById(R.id.ripple);

        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (overlay() && ignoreBattery()) {

                    if (!isServiceRunning(ForegroundService.class)) {

                        startButton.setIconResource(R.drawable.baseline_pause_24);

                        rippleBackground.startRippleAnimation();


                        boolean permissions = getPermissions();

                        if (!permissions) {
                            getPermissions();
                        } else {
                            turnOnLocation();
                            startForegroundService();
                        }
                    } else {
                        stopForegroundService();
                    }
                }

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isServiceRunning(ForegroundService.class)) {
            rippleBackground.startRippleAnimation();
            startButton.setIconResource(R.drawable.baseline_pause_24);
        } else {
            startButton.setIconResource(R.drawable.baseline_play_arrow_24);
            rippleBackground.stopRippleAnimation();
        }
    }


    private void stopForegroundService() {
        startButton.setIconResource(R.drawable.baseline_play_arrow_24);
        rippleBackground.stopRippleAnimation();
        Intent serviceIntent = new Intent(HomeActivity.this, ForegroundService.class);
        this.stopService(serviceIntent);
    }

    private void startForegroundService() {
        startButton.setIconResource(R.drawable.baseline_pause_24);
        rippleBackground.startRippleAnimation();
        Intent serviceIntent = new Intent(HomeActivity.this, ForegroundService.class);
        this.startService(serviceIntent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {

        ActivityManager manager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean getPermissions(){

        boolean permission = true;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.FOREGROUND_SERVICE}, FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, ACCESS_NOTIFICATION_POLICY_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        return permission;
    }

    private void turnOnLocation(){

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Aviso")
                    .setMessage("Para sua maior segurança a localização necessita de estar ativada?")
                    .setCancelable(false)
                    .setPositiveButton("Ligar", (DialogInterface.OnClickListener) (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancelar", (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.dismiss();
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }


    private boolean overlay(){
        boolean overlay = true;
        if (!Settings.canDrawOverlays(this)) {
            overlay = false;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);

        }
        return overlay;
    }


    private boolean ignoreBattery(){
        PowerManager pm = (PowerManager) this.getSystemService(android.content.Context.POWER_SERVICE);
        boolean overlay = true;
        if(!pm.isIgnoringBatteryOptimizations(this.getPackageName())) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(intent);

        }
        return overlay;
    }
}
