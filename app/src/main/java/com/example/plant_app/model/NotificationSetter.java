package com.example.plant_app.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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

    public boolean updateNotification(Context context, int id, String name, int waterInterval) {
        return true;
    }
}
