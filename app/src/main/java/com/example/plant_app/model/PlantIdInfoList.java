package com.example.plant_app.model;

import java.util.ArrayList;
import java.util.List;

public class PlantIdInfoList {
    private static List<PlantIdInfo> plantInfos;

    // private constructor to force the use of getInstance() to get an/the object
    private PlantIdInfoList() {}

    public static List<PlantIdInfo> getInstance()
    {
        if (plantInfos == null)
            plantInfos = new ArrayList<>();
        return plantInfos;
    }
}
