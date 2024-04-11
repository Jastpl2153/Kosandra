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

/**
 * The MainActivity class is the main activity of the application. It initializes the necessary components such as
 * binding, app bar configuration, notification time, and request code. It also sets up the navigation controller for
 * the app.
 */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;// Binding for the main activity layout
    private AppBarConfiguration appBarConfiguration; // Configuration for the app bar
    private LocalTime notificationTime = LocalTime.of(9, 0, 0);// Time for notification trigger
    private final String REQUEST_CODE = "WORK_KOSANDRA";// Request code for WorkManager

    /**
     * This method is called when the activity is starting. It initializes the notification channel, sets up the
     * WorkManager, inflates the layout, and configures the navigation controller.
     *
     * @param savedInstanceState A Bundle containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationChanel.createNotificationChannel(getApplicationContext());// Creating notification channel
        NotificationChanel.createNotificationChannelAPI33(MainActivity.this);// Creating notification channel for API level 33
        initWorkManager();// Initializing WorkManager

        binding = ActivityMainBinding.inflate(getLayoutInflater());// Inflating the layout
        setContentView(binding.getRoot());// Setting the content view

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_client, R.id.navigation_material, R.id.navigation_income, R.id.navigation_records)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    /**
     * This method is called when the user selects Navigate Up in the action bar. It navigates back to the previous screen.
     *
     * @return True if navigation was handled by the action bar, false otherwise
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Initializes the WorkManager to schedule periodic work for sending notifications.
     */
    private void initWorkManager() {
        LocalTime nowTime = LocalTime.now(); // Current time
        Duration diff = Duration.between(nowTime, notificationTime); // Calculating time difference

        long minutesDifference = diff.toMinutes();// Converting difference to minutes

        if (minutesDifference < 0) {
            minutesDifference += 24 * 60;// Adjusting for negative time difference
        }

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(minutesDifference, TimeUnit.MINUTES)// Setting initial delay for WorkRequest
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork(REQUEST_CODE, ExistingPeriodicWorkPolicy.UPDATE, workRequest);// Enqueueing unique periodic work
    }
}