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

public class NotificationChanel {
    public static final String CHANNEL_ID = "kosandra_channel_id";
    private static final CharSequence CHANNEL_NAME = "Записи клиентов";
    private static final String CHANNEL_DESCRIPTION = "Предупреждающее уведомление о записях клиентов";
    private static final int REQUEST_CODE = 99;

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

    public static void createNotificationChannelAPI33(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
        }
    }
}
