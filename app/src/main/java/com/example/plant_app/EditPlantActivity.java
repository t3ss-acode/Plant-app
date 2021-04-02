package com.example.plant_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;
import com.example.plant_app.storePlants.PlantIdKeeper;

public class EditPlantActivity extends AppCompatActivity {

    private static final String EDIT_LOG_TAG = "log_edit";

    private static final String INCORRECT_INPUT_NAME = "Please enter a name";
    private static final String INCORRECT_INPUT_NUMBER = "Please enter a positive number";
    private static final String ERROR_ADDING_PLANT = "ERROR: Unable to add plant entry";

    private Plant mSelectedPlant;

    private TextView mNoPlantView;
    private TextView mNameView;
    private EditText mWaterDaysView;
    private EditText mNutrientsDaysView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);

        mNoPlantView = findViewById(R.id.editNoPlantTextView);
        mNameView = findViewById(R.id.editNameEditText);
        mWaterDaysView = findViewById(R.id.editWaterEditText);
        mNutrientsDaysView = findViewById(R.id.editNutrientsEditText);


        Intent intent = getIntent();
        // Get the selected device from the intent
        int position = intent.getIntExtra(MainActivity.SELECTED_PLANT, -1);
        Log.d(EDIT_LOG_TAG, "" + position);
        if (position == -1) {
            mNoPlantView.setVisibility(View.VISIBLE);
            toast("No plant found");
        } else {
            mSelectedPlant = PlantList.getInstance().get(position);
            Log.d(EDIT_LOG_TAG, mSelectedPlant.toString());

            mNameView.setText(mSelectedPlant.getName());
            mWaterDaysView.setHint(Integer.toString(mSelectedPlant.getWaterReminder()));
            if(mSelectedPlant.getNutrientsReminder() != -1)
                mNutrientsDaysView.setHint(Integer.toString(mSelectedPlant.getNutrientsReminder()));
        }
    }

    public void updatePlant(View view) {
        String name = mNameView.getText().toString();
        String waterReminder = mWaterDaysView.getText().toString();
        String nutrientsReminder = mNutrientsDaysView.getText().toString();


        // TODO: Move this bit of logic from here and in add plant to its own class

        // Check that a name has been entered
        if (!name.matches("")) {
            mSelectedPlant.setName(name);
        }


        // Check that the number can be parsed to an integer and is a positive number
        if(!waterReminder.matches("")) {
            int waterNumber;
            try {
                waterNumber = Integer.parseInt(waterReminder);
                if (waterNumber <= 0) {
                    toast(INCORRECT_INPUT_NUMBER);
                    return;
                }
            } catch (Exception e) {
                toast(INCORRECT_INPUT_NUMBER);
                return;
            }
        }


        // If something has been entered, check that it is a positive integer
        if(!nutrientsReminder.matches("")) {
            int nutrientsNumber = -1;
            if (!nutrientsReminder.matches("")) {
                try {
                    nutrientsNumber = Integer.parseInt(nutrientsReminder);
                    if (nutrientsNumber <= 0) {
                        toast(INCORRECT_INPUT_NUMBER);
                        return;
                    }
                } catch (Exception e) {
                    toast(INCORRECT_INPUT_NUMBER);
                    return;
                }
            }
        }

        // TODO: Update notification after I actually get it to work

        mNameView.setText("");
        mWaterDaysView.setText("");
        mNutrientsDaysView.setText("");

        finish();
    }

    public void deletePlant(View view) {

        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this plant entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PlantList.getInstance().remove(mSelectedPlant);

                        mNameView.setText("");
                        mWaterDaysView.setText("");
                        mNutrientsDaysView.setText("");

                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}