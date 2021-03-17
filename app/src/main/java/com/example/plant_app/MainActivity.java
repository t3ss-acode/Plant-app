package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_LOG_TAG = "log_main";

    // data
    private List<Plant> plantList;

    // ui


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plantList = PlantList.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String m = "main: " + plantList.toString();
        Log.d(MAIN_LOG_TAG, m);
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void toIdentifyPlant(View view) {
        toast("ID PLANT");

        Intent intent = new Intent(this, IdPlantActivity.class);
        startActivity(intent);
    }



    public void toAddPlant(View view) {
        toast("NEW PLANT");

        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }


    /*


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

     */
}