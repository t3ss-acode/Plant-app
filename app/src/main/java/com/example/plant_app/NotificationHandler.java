package com.example.plant_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHandler {

    private static final String NOTIFY_HANDLER_LOG_TAG = "log_notify_handler";

    private static final String WATER_CHANNEL_ID = "water_notification_channel";
    //NOTIFICATION_ID is the unique id for the plant

    private NotificationManager mNotifyManager;



    public void createNotificationChannel(Context context, int plantId)
    {
        mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    WATER_CHANNEL_ID,
                    "Water Notification",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 400, 200});
            notificationChannel.setDescription("Notification from PlApp");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(context, plantId);

        if(notifyBuilder != null)
            mNotifyManager.notify(plantId, notifyBuilder.build());
        else
            Log.d(NOTIFY_HANDLER_LOG_TAG, "Unable to create builder. No plant with that id");
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context, int plantId) {

        Plant plant = getPlantFromId(plantId);

        if(plant != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, WATER_CHANNEL_ID)
                    .setContentTitle(("Water " + plant.getName()))
                    .setContentText("Time to water your flowers!")
                    .setSmallIcon(R.drawable.ic_launcher_foreground);
            return builder;
        }
        return null;
    }

    /**
     *
     * @param id
     * @return plant with correct id else returns null
     */
    private Plant getPlantFromId(int id) {
        List<Plant> plants = PlantList.getInstance();
        for(int i=0; i < plants.size(); i++) {
            if(plants.get(i).getId() == id)
                return plants.get(i);
        }
        return null;
    }

}
