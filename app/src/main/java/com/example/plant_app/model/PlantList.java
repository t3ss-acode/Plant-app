package com.example.plant_app.model;

import com.example.plant_app.model.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantList {

    private static List<Plant> plants;

    // private constructor to force the use of getInstance() to get an/the object
    private PlantList() {}

    public static List<Plant> getInstance()
    {
        if (plants == null)
            plants = new ArrayList<>();
        return plants;
    }
}
