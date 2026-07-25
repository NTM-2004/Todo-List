package com.example.todolist.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.todolist.model.Task;

public class DeadlineNotificationManager {

    public static final String CHANNEL_ID = "todo_deadline_channel";
    public static final String CHANNEL_NAME = "Nhắc nhở deadline";
    private static final long NOTIFY_BEFORE_MS = 60 * 60 * 1000L; // 1 giờ trước

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Nhắc nhở khi task sắp đến hạn");
            channel.enableVibration(true);
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            if (nm != null) nm.createNotificationChannel(channel);
        }
    }

    public static void scheduleDeadlineNotification(Context context, Task task) {
        if (task.getDeadline() == null || task.getDeadline() <= 0 || !task.isNotifyEnabled()) return;

        long notifyTime = task.getDeadline() - NOTIFY_BEFORE_MS;
        if (notifyTime <= System.currentTimeMillis()) return; // already past

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DeadlineReceiver.class);
        intent.putExtra("taskId", task.getId());
        intent.putExtra("taskTitle", task.getTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, task.getId(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
            }
        }
    }

    public static void cancelNotification(Context context, int taskId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DeadlineReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, taskId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager != null) alarmManager.cancel(pendingIntent);
    }
}
