package com.example.kosandra;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.kosandra.databinding.ActivityMainBinding;
import com.example.kosandra.notification.NotificationChanel;
import com.example.kosandra.notification.NotificationWorker;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;
    private LocalTime notificationTime = LocalTime.of(9, 0, 0);
    private final String REQUEST_CODE = "WORK_KOSANDRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationChanel.createNotificationChannel(getApplicationContext());
        NotificationChanel.createNotificationChannelAPI33(MainActivity.this);
        initWorkManager();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_client, R.id.navigation_material, R.id.navigation_income, R.id.navigation_records)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initWorkManager(){
        LocalTime nowTime = LocalTime.now();
        Duration diff = Duration.between(nowTime, notificationTime);

        long minutesDifference = diff.toMinutes();

        if (minutesDifference < 0) {
            minutesDifference += 24 * 60;
        }
        Log.d("time", String.valueOf(minutesDifference));
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay (minutesDifference ,TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(REQUEST_CODE, ExistingPeriodicWorkPolicy.UPDATE, workRequest);
    }
}