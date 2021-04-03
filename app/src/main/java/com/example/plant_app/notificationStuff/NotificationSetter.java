package com.example.plant_app.notificationStuff;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.plant_app.notificationStuff.NotificationReceiver;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class NotificationSetter {

    /**
     * Creates a notification that will notify every day*interval
     *
     * @param context
     * @param id
     * @param name
     * @param waterInterval
     * @return true if it was successful
     */
    public boolean createNotification(Context context, int id, String name, int waterInterval) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 21);
            calendar.set(Calendar.MINUTE, 40);
            calendar.set(Calendar.SECOND, 0);

            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("plant_name", name);
            intent.putExtra("plantId", id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    (AlarmManager.INTERVAL_DAY * waterInterval), pendingIntent);

            return true;
        }catch(Exception e) {
            return false;
        }
    }


    public boolean deleteNotification(Context context, int id, String name) {
        try {
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("plant_name", name);
            intent.putExtra("plantId", id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // The id of the channel.
                String idStr = Integer.toString(id);
                notificationManager.deleteNotificationChannel(idStr);
                return true;
            }
            return false;

        }catch(Exception e) {
            return false;
        }
    }
}
