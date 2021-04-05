package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.plant_app.model.InputChecker;
import com.example.plant_app.notificationStuff.NotificationSetter;
import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;
import com.example.plant_app.util.MsgUtil;

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


        InputChecker checker = new InputChecker();
        // Check that a name has been entered
        if(checker.isEmpty(name)) {
            MsgUtil.toast(this, INCORRECT_INPUT_NAME);
            return;
        }

        // Check that the number can be parsed to an integer and is a positive number
        int waterNr = checker.checkNumber(waterNrStr);
        if(waterNr == -1) {
            MsgUtil.toast(this, INCORRECT_INPUT_NUMBER);
            return;
        }

        // If something has been entered, check that it is a positive integer
        int nutrientsNr = -1;
        if(!checker.isEmpty(nutrientsNrStr)) {
            nutrientsNr = checker.checkNumber(nutrientsNrStr);
            if(nutrientsNr == -1) {
                MsgUtil.toast(this, INCORRECT_INPUT_NUMBER);
                return;
            }
        }

        // Depending on if nutrients reminder has been given, use different constructors
        try{
            if(nutrientsNr != -1)
                plantList.add(new Plant(name, waterNr, nutrientsNr));
            else
                plantList.add(new Plant(name, waterNr));
        }catch(Exception e) {
            MsgUtil.toast(this, ERROR_ADDING_PLANT);
            return;
        }


        // Add notification
        boolean notiSuccess = new NotificationSetter().createNotification(
                getApplicationContext(), getAddedPlantId(), name, waterNr);

        if(!notiSuccess) {
            MsgUtil.toast(this, ERROR_NOTIFICATION);
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
}