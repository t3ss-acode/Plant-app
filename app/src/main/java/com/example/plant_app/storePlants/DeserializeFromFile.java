package com.example.plant_app.storePlants;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.plant_app.adapters.PlantAdapter;
import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DeserializeFromFile extends AsyncTask<File, Void, ArrayList<Object>> {
    private static final String SAVED_DATA_LOG_TAG = "log_saved_data";

    private static final String FILENAME = "saved_plants";

    private WeakReference<List<Plant>> mPlantList;
    private WeakReference<PlantAdapter> mPlantAdapter;


    public DeserializeFromFile(PlantAdapter adapter) {
        this.mPlantList = new WeakReference<>(PlantList.getInstance());
        mPlantAdapter = new WeakReference<>(adapter);
    }


    @Override
    protected ArrayList<Object> doInBackground(File... files) {
        return loadPlants(files[0]);
    }

    private ArrayList<Object> loadPlants(File directory) {
        ArrayList<Object> deserializedData = new ArrayList<>();

        try {
            File file = new File(directory, FILENAME);
            Log.d(SAVED_DATA_LOG_TAG, ("file length " + file.length()));

            //Get access to the file
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            //Read the list from the file
            deserializedData = (ArrayList<Object>) objectIn.readObject();

            objectIn.close();
            fileIn.close();

        } catch(IOException ex) {
            Log.d(SAVED_DATA_LOG_TAG, "Unable to read saved data");
        } catch(ClassNotFoundException ex) {
            Log.d(SAVED_DATA_LOG_TAG, "ClassNotFoundException is caught");
        }

        Log.d(SAVED_DATA_LOG_TAG, "Object has been deserialized");
        return deserializedData;
    }



    @Override
    protected void onPostExecute(ArrayList<Object> deserializedData) {
        super.onPostExecute(deserializedData);

        //Fill the variables with the data
        if(deserializedData.size() > 0) {
            mPlantList.get().clear();
            mPlantList.get().addAll((List<Plant>) deserializedData.get(0));

            int mPlantId = (int) deserializedData.get(1);
            PlantIdKeeper.setCurrentId(mPlantId);

            LocalDate lastDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                lastDate = (LocalDate) deserializedData.get(2);
                updateWaterCountdowns(lastDate);
            }else {
                Log.d(SAVED_DATA_LOG_TAG, "Unable to update water countdowns");
            }


            mPlantAdapter.get().notifyDataSetChanged();

            Log.d(SAVED_DATA_LOG_TAG, "Views have been filled");
        }else {
            Log.d(SAVED_DATA_LOG_TAG, "No deserialized object");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateWaterCountdowns(LocalDate lastDate) {
        Log.d(SAVED_DATA_LOG_TAG, "in update");
        LocalDate currentDate = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(lastDate, currentDate);

        Log.d(SAVED_DATA_LOG_TAG, ("between: " + daysBetween));

        int waterIn;
        int lastWatered;
        boolean wateredPasses = false;

        for(int i=0; i < mPlantList.get().size(); i++) {
            waterIn = mPlantList.get().get(i).getWaterIn();
            lastWatered = mPlantList.get().get(i).getLastWatered();

            for(int j=0; j < daysBetween; j++) {
                waterIn--;
                lastWatered++;

                if(waterIn < 0) {
                    waterIn = mPlantList.get().get(i).getWaterReminder() -1;
                    lastWatered = 1;
                    wateredPasses = true;
                }
            }

            mPlantList.get().get(i).setWaterIn(waterIn);

            if(mPlantList.get().get(i).getLastWatered() != -1) {
                mPlantList.get().get(i).setLastWatered(lastWatered);
            }else if(mPlantList.get().get(i).getLastWatered() == -1 && wateredPasses) {
                mPlantList.get().get(i).setLastWatered(lastWatered);
            }
        }
    }
}
