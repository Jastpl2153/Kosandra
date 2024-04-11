package com.example.kosandra.notification;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.example.kosandra.MainActivity;

/**
 * This class represents a utility class for creating notification channels in Android.
 * <p>
 * It provides methods to create notification channels based on the Android version.
 */
public class NotificationChanel {
    /**
     * The unique identifier for the notification channel.
     */
    public static final String CHANNEL_ID = "kosandra_channel_id";
    /**
     * The human-readable name of the notification channel.
     */
    private static final CharSequence CHANNEL_NAME = "Записи клиентов";
    /**
     * The description of the notification channel.
     */
    private static final String CHANNEL_DESCRIPTION = "Предупреждающее уведомление о записях клиентов";
    /**
     * The request code used for API level 33.
     */
    private static final int REQUEST_CODE = 99;

    /**
     * Creates a notification channel for Android versions Oreo (API 26) and above.
     *
     * @param context The context of the application.
     */
    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(CHANNEL_DESCRIPTION);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Creates a notification channel for API level 33 (TIRAMISU) and above.
     *
     * @param activity The activity requesting the notification channel.
     */
    public static void createNotificationChannelAPI33(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
        }
    }
}
