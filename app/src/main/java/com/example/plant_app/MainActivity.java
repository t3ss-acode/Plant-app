package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.plant_app.model.NotificationReceiver;
import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;
import com.example.plant_app.storePlants.DeserializeFromFile;
import com.example.plant_app.storePlants.SerializeToFile;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_LOG_TAG = "log_main";
    private static final String SAVED_DATA_LOG_TAG = "log_saved_data";

    private static final String FILENAME = "saved_plants";

    // data
    private List<Plant> plantList;

    // ui
    private PlantAdapter mPlantAdapter;


    private final static String default_notification_channel_id = "default";
    final Calendar myCalendar = Calendar.getInstance ();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantList = PlantList.getInstance();

        // Set up the recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mPlantAdapter = new PlantAdapter();
        recyclerView.setAdapter(mPlantAdapter);


        Log.d(MAIN_LOG_TAG, "what the hell");


        //Keep content of screen rotates
        if (savedInstanceState != null) {
            /*
            approvedTimeTextView.setText(savedInstanceState.getString("approved_text"));
            latTextView.setText(savedInstanceState.getString("lat_text"));
            lonTextView.setText(savedInstanceState.getString("lon_text"));

            if (savedInstanceState.getBoolean("loaded_visible")) {
                loadedDataTextView.setText(savedInstanceState.getString("reply_text"));
                loadedDataTextView.setVisibility(View.VISIBLE);
            }

             */
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if there is saved data
        File file = new File(this.getFilesDir(), FILENAME);
        if(file.length() > 0 && plantList.isEmpty())
            new DeserializeFromFile(mPlantAdapter).execute(this.getFilesDir());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPlantAdapter.notifyDataSetChanged();

        String m = "main: " + plantList.toString();
        Log.d(MAIN_LOG_TAG, m);

        myAlarm();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
        // Volley, cancel pending requests
        mRequestQueue.cancelAll(this);
        //volley stop pending request. Ta bort resten i kön
        */

        Log.d("serede", ("in main, list size: " + plantList.size()));
        if (plantList.size() > 0) {
            Log.d("serede", "in main in if");
            SerializeToFile.SavePlants(this.getFilesDir());
        }
        Log.d("serede", "in main, after if");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*

        //Save the state of the text boxes
        outState.putString("approved_text", approvedTimeTextView.getText().toString());
        outState.putString("lat_text", latTextView.getText().toString());
        outState.putString("lon_text", lonTextView.getText().toString());

        if (loadedDataTextView.getVisibility() == View.VISIBLE) {
            outState.putBoolean("loaded_visible", true);
            outState.putString("reply_text", loadedDataTextView.getText().toString());
        }

         */
    }


    public void toIdentifyPlant(View view) {
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        //If there is internet connection, allow access to IdPlant activity
        if (networkInfo != null && networkInfo.isConnected()) {
            Intent intent = new Intent(this, IdPlantActivity.class);
            startActivity(intent);
        } else {
            toast("Connect to the internet to use this feature");
        }
    }

    public void toAddPlant(View view) {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }


    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }









    public void myAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 13);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
}