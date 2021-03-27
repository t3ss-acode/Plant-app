package com.example.plant_app.storePlants;

import com.example.plant_app.model.Plant;

import java.util.List;

public class PlantIdKeeper {

    private static int currentId;

    private PlantIdKeeper() { }

    /**
     * @return Increments then returns currentId
     */
    public static int getPlantId() {
        currentId++;
        return currentId;
    }

    public static int getCurrentId() {
        return currentId;
    }

    protected static void setCurrentId(int id) {
        currentId = id;
    }

    /**
     * Double checks so the currentId the highest id value
     * @param plants
     * @return true if currentId was changed
     */
    public boolean checkIdWithPlants(List<Plant> plants) {
        if(plants.isEmpty())
            return false;
        if(currentId != 0)
            return false;
        currentId = highestId(plants);
        return true;
    }

    private int highestId(List<Plant> plants) {
        int highest = 0;
        for(int i=0; i < plants.size(); i++) {
            if(plants.get(i).getId() > highest)
                highest = plants.get(i).getId();
        }
        return highest;
    }
}
