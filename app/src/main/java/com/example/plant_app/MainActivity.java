package com.example.plant_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.plant_app.storePlants.DeserializeFromFile;
import com.example.plant_app.storePlants.SerializeToFile;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_LOG_TAG = "log_main";
    private static final String SAVED_DATA_LOG_TAG = "log_saved_data";

    private static final String FILENAME = "saved_plants";

    // data
    private List<Plant> plantList;

    // ui
    private PlantAdapter mPlantAdapter;


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
        Intent intent = new Intent(this, IdPlantActivity.class);
        startActivity(intent);
    }

    public void toAddPlant(View view) {
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }


    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}