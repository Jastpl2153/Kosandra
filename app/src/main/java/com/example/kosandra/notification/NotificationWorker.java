package com.example.kosandra.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.kosandra.MainActivity;
import com.example.kosandra.R;
import com.example.kosandra.db.KosandraDataBase;
import com.example.kosandra.entity.Record;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;

public class NotificationWorker extends Worker {
    private static final int NOTIFICATION_THREE_DAY_ID = 1;
    private static final int NOTIFICATION_TOMORROW_ID = 2;
    private KosandraDataBase dataBase;
    private String TITLE_NOTIFICATION = "Внимание запись!";
    private String CONTENT_NOTIFICATION_THREE_DAY = "Не забудте у вас через 3 дня ";
    private String CONTENT_NOTIFICATION_TOMORROW = "Не забудте у вас завтра ";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dataBase = KosandraDataBase.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Record> records = dataBase.recordsDAO().getAllRecordList();
            if (records != null) {
                boolean clientVisitThreeDay = false;
                int countClientVisitThreeDay = 0;
                boolean clientVisitTomorrow = false;
                int countClientVisitTomorrow = 0;
                LocalDate now = LocalDate.now();
                for (Record record : records) {
                    if (now.plusDays(3).isEqual(record.getVisitDate())) {
                        clientVisitThreeDay = true;
                        countClientVisitThreeDay++;
                    }
                    if (now.plusDays(1).isEqual(record.getVisitDate())) {
                        clientVisitTomorrow = true;
                        countClientVisitTomorrow++;
                    }
                }
                buildAndNotifyThreeDayNotification(clientVisitThreeDay, countClientVisitThreeDay);
                buildAndNotifyTomorrowNotification(clientVisitTomorrow, countClientVisitTomorrow);
            }
        });
        return Result.success();
    }

    private void buildAndNotifyThreeDayNotification(boolean clientVisitThreeDay, int countClientVisitThreeDay) {
        if (clientVisitThreeDay) {
            Notification.Builder builder = buildNotification();
            builder.setContentText(CONTENT_NOTIFICATION_THREE_DAY + (countClientVisitThreeDay == 1 ? "клиент" : "клиенты"));
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_THREE_DAY_ID, builder.build());
        }
    }

    private void buildAndNotifyTomorrowNotification(boolean clientVisitTomorrow, int countClientVisitTomorrow) {
        if (clientVisitTomorrow) {
            Notification.Builder builder = buildNotification();
            builder.setContentText(CONTENT_NOTIFICATION_TOMORROW + (countClientVisitTomorrow == 1 ? "клиент" : "клиенты")).build();
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_TOMORROW_ID, builder.build());
        }
    }

    @NonNull
    private Notification.Builder buildNotification() {
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        return new Notification.Builder(getApplicationContext(), NotificationChanel.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(TITLE_NOTIFICATION)
                .setContentIntent(pendingIntent);
    }
}
