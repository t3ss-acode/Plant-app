package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plant_app.model.NotificationReceiver;
import com.example.plant_app.model.NotificationSetter;
import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddPlantActivity extends AppCompatActivity {

    private static final String ADD_PLANT_LOG_TAG = "log_addplant";

    private static final String INCORRECT_INPUT_NAME = "Please enter a name";
    private static final String INCORRECT_INPUT_NUMBER = "Please enter a positive number";
    private static final String ERROR_ADDING_PLANT = "ERROR: Unable to add plant entry";
    private static final String ERROR_NOTIFICATION = "ERROR: Unable to set notification";

    // data
    private List<Plant> plantList;

    //ui
    private EditText mNameEditText;
    private EditText mWaterNrEditText;
    private EditText mNutrientsNrEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);


        plantList = PlantList.getInstance();

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mWaterNrEditText = (EditText) findViewById(R.id.waterNumberEditText);
        mNutrientsNrEditText = (EditText) findViewById(R.id.nutrientsNumberEditText);
    }


    public void addPlant(View view) {
        String name = mNameEditText.getText().toString();
        String waterNrStr = mWaterNrEditText.getText().toString();
        String nutrientsNrStr = mNutrientsNrEditText.getText().toString();


        // Check that a name has been entered
        if (name.matches("")) {
            toast(INCORRECT_INPUT_NAME);
            return;
        }

        // Check that the number can be parsed to an integer and is a positive number
        int waterNumber;
        try{
            waterNumber = Integer.parseInt(waterNrStr);
            if (waterNumber <= 0) {
                toast(INCORRECT_INPUT_NUMBER);
                return;
            }
        }catch(Exception e) {
            toast(INCORRECT_INPUT_NUMBER);
            return;
        }


        // If something has been entered, check that it is a positive integer
        int nutrientsNumber = -1;
        if(!nutrientsNrStr.matches("")) {
            try {
                nutrientsNumber = Integer.parseInt(nutrientsNrStr);
                if (nutrientsNumber <= 0) {
                    toast(INCORRECT_INPUT_NUMBER);
                    return;
                }
            } catch (Exception e) {
                toast(INCORRECT_INPUT_NUMBER);
                return;
            }
        }



        try{
            if(nutrientsNumber != -1)
                plantList.add(new Plant(name, waterNumber, nutrientsNumber));
            else
                plantList.add(new Plant(name, waterNumber));
        }catch(Exception e) {
            toast(ERROR_ADDING_PLANT);
            return;
        }


        // Add notification
        /*
        try{

            Log.d(ADD_PLANT_LOG_TAG, Integer.toString(getAddedPlantId()));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 20);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            int id = getAddedPlantId();

            Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
            intent.putExtra("plant_name", name);
            intent.putExtra("plantId", id);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    (AlarmManager.INTERVAL_DAY*waterNumber), pendingIntent);

        }catch(Exception e) {
            toast(ERROR_NOTIFICATION);
            return;
        }

         */

        boolean notiSuccess = new NotificationSetter().createNotification(
                getApplicationContext(), getAddedPlantId(), name, waterNumber);

        if(!notiSuccess) {
            toast(ERROR_NOTIFICATION);
            return;
        }

        // Empty text and go back to main
        mNameEditText.setText("");
        mWaterNrEditText.setText("");
        mNutrientsNrEditText.setText("");

        finish();
    }


    private int getAddedPlantId() {
        return plantList.get(plantList.size()-1).getId();
    }





    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}