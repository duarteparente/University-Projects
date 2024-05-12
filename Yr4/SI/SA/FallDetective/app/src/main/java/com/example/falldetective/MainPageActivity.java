package com.example.falldetective;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainPageActivity extends AppCompatActivity {

    //private DrawerLayout drawerLayout;
    //private ImageButton openDrawer;
    //private NavigationView navigationView;

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.bottomHome){
                    loadFragment(new HomeFragment());
                } else if (id == R.id.bottomFallRecords) {
                    loadFragment(new FallRecordsFragment());
                } else if (id == R.id.bottomProfile) {
                    loadFragment(new ProfileFragment());
                } else {
                    loadFragment(new ConfigsFragment());
                }
                return true;
            }
        });

        loadFragment(new HomeFragment());

    }


    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }
}

        /*
        drawerLayout = findViewById(R.id.drawerLayout);
        openDrawer = findViewById(R.id.openDrawer);
        navigationView = findViewById(R.id.NavigationView);

        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /*
                switch (item.getItemId()) {
                    case R.id.home:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                        break;
                    case R.id.fallRecords:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                        break;
                    case R.id.profile:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                        break;
                    case R.id.configs:

                        break;
                    case R.id.aboutUs:

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                        break;
                    case R.id.logout:

                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        }); */