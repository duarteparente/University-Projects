package com.example.falldetective;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.skyfishjy.library.RippleBackground;

public class HomeFragment extends Fragment {

    private RippleBackground rippleBackground;
    private MaterialButton startButton;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1002;
    private static final int FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE = 1003;
    private static final int POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE = 1004;
    private static final int ACCESS_NOTIFICATION_POLICY_PERMISSION_REQUEST_CODE = 1005;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rippleBackground = view.findViewById(R.id.ripple);

        startButton = view.findViewById(R.id.startButton);

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

        return view;
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
        Intent serviceIntent = new Intent(requireContext(), ForegroundService.class);
        requireContext().stopService(serviceIntent);
    }

    private void startForegroundService() {
        startButton.setIconResource(R.drawable.baseline_pause_24);
        rippleBackground.startRippleAnimation();
        Intent serviceIntent = new Intent(requireContext(), ForegroundService.class);
        requireContext().startService(serviceIntent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        Activity activity = getActivity();
        if (activity != null) {
            ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            if (manager != null) {
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (serviceClass.getName().equals(service.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean getPermissions(){

        boolean permission = true;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.FOREGROUND_SERVICE}, FOREGROUND_SERVICE_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, ACCESS_NOTIFICATION_POLICY_PERMISSION_REQUEST_CODE);
            permission = false;
        }
        return permission;
    }

    private void turnOnLocation(){

        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
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
        if (!Settings.canDrawOverlays(getContext())) {
            overlay = false;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            startActivity(intent);

        }
        return overlay;
    }


    private boolean ignoreBattery(){
        PowerManager pm = (PowerManager) getContext().getSystemService(android.content.Context.POWER_SERVICE);
        boolean overlay = true;
        if(!pm.isIgnoringBatteryOptimizations(getContext().getPackageName())) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getContext().getPackageName()));
            startActivity(intent);

        }
        return overlay;
    }
}