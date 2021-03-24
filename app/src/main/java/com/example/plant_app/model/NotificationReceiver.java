package com.example.plant_app.model;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.plant_app.MainActivity;
import com.example.plant_app.R;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFICATION_ID = "notify_plant_";
    private static final String NOTIFICATION_CHANNEL_ID = "plant_notification_channel_id";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent repeatingIntent= new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        getNotificationId();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Water X")
                .setContentText("Time to water you flowers!")
                .setAutoCancel(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);;

        Log.d("NOTIFICATIONS PLEASE", "doin notification stuff");

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "Plant app notification channel", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 400, 200});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }
        assert notificationManager != null;


        notificationManager.notify(0, builder.build());

        Log.d("NOTIFICATIONS PLEASE", "its built");






        /*


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }
    */

        /*
        NotificationHelper notificationHelper = new NotificationHelper(context);
        notificationHelper.createNotification();

         */
    }

    private String getNotificationId() {
        return "";
    }
}
