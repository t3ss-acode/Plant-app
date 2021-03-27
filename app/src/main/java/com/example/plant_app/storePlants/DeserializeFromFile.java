package com.example.plant_app.storePlants;

import android.os.AsyncTask;
import android.util.Log;

import com.example.plant_app.PlantAdapter;
import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
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
            Log.d(SAVED_DATA_LOG_TAG, ("file length" + file.length()));

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
            String m = "number " + mPlantId;
            Log.d(SAVED_DATA_LOG_TAG, m);
            m = "id " + PlantIdKeeper.getCurrentId();
            Log.d(SAVED_DATA_LOG_TAG, m);

            mPlantAdapter.get().notifyDataSetChanged();

            Log.d(SAVED_DATA_LOG_TAG, "Views have been filled");
        }else {
            Log.d(SAVED_DATA_LOG_TAG, "No deserialized object");
        }
    }
}
