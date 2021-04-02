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

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String NOTIFY_RECEIVER_LOG_TAG = "log_notify_receiver";

    // Notification channel ID.
    private static final String WATER_CHANNEL_ID = "water_notification_channel";

    // Notification ID.
    // NOTIFCATION_ID = the plants id
    //private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;

    private List<Plant> plants;



    @Override
    public void onReceive(Context context, Intent intent) {
        /*

        plants = PlantList.getInstance();

        Intent repeatingIntent= new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String notifyId = getNotificationId();

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

         */

        int plantId = intent.getIntExtra("plantId", -1);
        if(plantId != -1) {

            plants = PlantList.getInstance();

            Intent repeatingIntent = new Intent(context, MainActivity.class);
            repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, plantId,
                    repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            createNotificationChannel(context);

            // Build the notification with all of the parameters using helper method
            NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context, plantId);

            if(notifyBuilder != null) {
                notifyBuilder.setChannelId(WATER_CHANNEL_ID);

                // Deliver the notification.
                mNotifyManager.notify(plantId, notifyBuilder.build());
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

    private NotificationCompat.Builder getNotificationBuilder(Context context, int plantId) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context, plantId,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Plant plant = getPlantFromId(plantId);

        if(plant != null) {
            plant.setWaterIn(plant.getWaterReminder()-1);
            plant.setLastWatered(0);
            Log.d("log_saved_data", "in alarm: water updated");
            // Build the notification with all of the parameters.
            NotificationCompat.Builder notifyBuilder = new NotificationCompat
                    .Builder(context, WATER_CHANNEL_ID)
                    .setContentTitle("Water " + plant.getName())
                    .setContentText("Time to water you flowers!")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);
            return notifyBuilder;
        }
        return null;
    }



    private void updateNotification(Context context, int plantId) {
        // Build the notification with all of the parameters using helper method
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context, plantId);

        notifyBuilder.setContentTitle("plant name: ");

        // Deliver the notification.
        mNotifyManager.notify(plantId, notifyBuilder.build());
    }


    /**
     *
     * @param id
     * @return plant with correct id else returns null
     */
    private Plant getPlantFromId(int id) {
        for(int i=0; i < plants.size(); i++) {
            if(plants.get(i).getId() == id)
                return plants.get(i);
        }
        return null;
    }


}
