package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.util.List;

public class AddPlantActivity extends AppCompatActivity {

    private static final String ADD_PLANT_LOG_TAG = "log_addplant";

    private static final String INCORRECT_INPUT_NAME = "Please enter a name";
    private static final String INCORRECT_INPUT_NUMBER = "Please enter a positive number";
    private static final String ERROR_ADDING_PLANT = "ERROR: Unable to add plant entry";

    // data
    private List<Plant> plantList;

    //ui
    private EditText mNameEditText;
    private EditText mNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);


        plantList = PlantList.getInstance();

        mNameEditText = (EditText) findViewById(R.id.nameEditText);
        mNumberEditText = (EditText) findViewById(R.id.numberEditText);
    }


    public void addPlant(View view) {
        String name = mNameEditText.getText().toString();
        String numberStr = mNumberEditText.getText().toString();


        // Check that a name has been entered
        if (name.matches("")) {
            toast(INCORRECT_INPUT_NAME);
            return;
        }

        // Check that the nuumber can be parsed to an integer and is a positive number
        int number;
        try{
            number = Integer.valueOf(numberStr);
            if (number <= 0) {
                toast(INCORRECT_INPUT_NUMBER);
                return;
            }
        }catch(Exception e) {
            toast(INCORRECT_INPUT_NUMBER);
            return;
        }


        try{
            plantList.add(new Plant(name, number));

            String m = "add: " + plantList.toString();
            Log.d(ADD_PLANT_LOG_TAG, m);

        }catch(Exception e) {
            toast(ERROR_ADDING_PLANT);
            return;
        }
    }



    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}