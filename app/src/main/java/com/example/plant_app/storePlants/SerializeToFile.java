package com.example.plant_app.storePlants;

import android.util.Log;

import com.example.plant_app.model.PlantList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SerializeToFile {
    private static final String SAVED_DATA_LOG_TAG = "log_saved_data";

    private static final String FILENAME = "saved_plants";

    public SerializeToFile() {
    }

    public static boolean SavePlants(File directory) {
        ArrayList<Object> data = new ArrayList<>();

        data.add(PlantList.getInstance());
        data.add(PlantIdKeeper.getCurrentId());

        try {
            File file = new File(directory, FILENAME);
            file.createNewFile(); // if file already exists will do nothing
            FileOutputStream fileOut = new FileOutputStream(file);

            //write the list to the file
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(data);

            objectOut.close();
            fileOut.close();

            Log.d(SAVED_DATA_LOG_TAG, "Successfully written to file");
            Log.d(SAVED_DATA_LOG_TAG, ("serialize list: " + data.toString()));

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(SAVED_DATA_LOG_TAG, "unable to serialize to file: ");
            return false;
        }
    }
}
