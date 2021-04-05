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

import com.example.plant_app.model.InputChecker;
import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;
import com.example.plant_app.notificationStuff.NotificationSetter;
import com.example.plant_app.util.MsgUtil;

public class EditPlantActivity extends AppCompatActivity {

    private static final String EDIT_LOG_TAG = "log_edit";

    private static final String INCORRECT_INPUT_NAME = "Please enter a name";
    private static final String INCORRECT_INPUT_NUMBER = "Please enter a positive number";

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
        if (position == -1) {
            mNoPlantView.setVisibility(View.VISIBLE);
            MsgUtil.toast(this, "No plant found");
        } else {
            mSelectedPlant = PlantList.getInstance().get(position);

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

        boolean incorrectInput = false;
        InputChecker checker = new InputChecker();

        // Check that a name has been entered
        if(checker.isEmpty(name)) {
            MsgUtil.toast(this, INCORRECT_INPUT_NAME);
            incorrectInput = true;
            return;
        }

        // Check that the number can be parsed to an integer and is a positive number
        int waterNr = -1;
        if(!checker.isEmpty(waterReminder)) {
            waterNr = checker.checkNumber(waterReminder);
            if(waterNr == -1) {
                MsgUtil.toast(this, INCORRECT_INPUT_NUMBER);
                incorrectInput = true;
                return;
            }
        }

        // If something has been entered, check that it is a positive integer
        int nutrientsNr = -1;
        if(!checker.isEmpty(nutrientsReminder)) {
            nutrientsNr = checker.checkNumber(nutrientsReminder);
            if(nutrientsNr == -1) {
                MsgUtil.toast(this, INCORRECT_INPUT_NUMBER);
                incorrectInput = true;
                return;
            }
        }


        if(incorrectInput == false) {
            Log.d(EDIT_LOG_TAG, mSelectedPlant.toString());
            mSelectedPlant.setName(name);
            if(waterNr != -1)
                mSelectedPlant.setWaterReminder(waterNr);
            if(nutrientsNr != -1)
                mSelectedPlant.setNutrientsReminder(nutrientsNr);

            NotificationSetter notiSetter = new NotificationSetter();
            // Create and update are the same so no specific update method
            notiSetter.createNotification(getApplicationContext(), mSelectedPlant.getId(), name, mSelectedPlant.getWaterReminder());

            Log.d(EDIT_LOG_TAG, mSelectedPlant.toString());

            mNameView.setText("");
            mWaterDaysView.setText("");
            mNutrientsDaysView.setText("");


            finish();
        }
    }

    public void deletePlant(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this plant entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        NotificationSetter notiSetter = new NotificationSetter();

                        notiSetter.deleteNotification(getApplicationContext(),
                                mSelectedPlant.getId(), mSelectedPlant.getName());

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
}