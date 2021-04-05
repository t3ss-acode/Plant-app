package com.example.plant_app.notificationStuff;

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

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFY_RECEIVER_LOG_TAG = "log_notify_receiver";

    // Notification channel ID.
    private static final String WATER_CHANNEL_ID = "water_notification_channel";

    // Notification ID.
    // NOTIFCATION_ID = the plants id

    private NotificationManager mNotifyManager;



    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("plantId", -1);
        String name = intent.getStringExtra("plant_name");
        if(name != null && id != -1) {
            Intent repeatingIntent = new Intent(context, MainActivity.class);
            repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            createNotificationChannel(context);

            // Build the notification with all of the parameters using helper method
            NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context, id, name);

            if(notifyBuilder != null) {
                notifyBuilder.setChannelId(WATER_CHANNEL_ID);

                // Deliver the notification.
                mNotifyManager.notify(id, notifyBuilder.build());
                Log.d(NOTIFY_RECEIVER_LOG_TAG, "notification done");
            }else {
                Log.d(NOTIFY_RECEIVER_LOG_TAG, "Plant with that id not found");
            }
        }else {
            Log.d(NOTIFY_RECEIVER_LOG_TAG, "Didnt get id");
        }
    }


    public void createNotificationChannel(Context context) {

        // Create a notification manager object.
        mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel(WATER_CHANNEL_ID,
                    "Water Notification",
                    NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 400, 200});
            notificationChannel.setDescription("Notification from PlApp");

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context, int plantId, String name) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, plantId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Log.d("log_saved_data", "in alarm: water updated");
        // Build the notification with all of the parameters.
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(context, WATER_CHANNEL_ID)
                .setContentTitle("Water " + name)
                .setContentText("Time to water you flowers!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }
}
