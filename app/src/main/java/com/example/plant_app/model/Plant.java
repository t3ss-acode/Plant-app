package com.example.plant_app.model;

import com.example.plant_app.storePlants.PlantIdKeeper;

public class Plant implements java.io.Serializable {

    private String name;
    private int waterReminder;
    private int waterIn;
    private int lastWatered = -1;
    private int nutrientsReminder = -1;
    private int nutrientsIn = -1;
    private int lastNutrients = -1;
    private int id;


    public Plant(String name, int waterReminder) {
        this.name = name;
        this.waterReminder = waterReminder;
        waterIn = waterReminder;
        id = PlantIdKeeper.getPlantId();
    }

    public Plant(String name, int waterReminder, int nutrientsReminder) {
        this.name = name;
        this.waterReminder = waterReminder;
        waterIn = waterReminder;
        this.nutrientsReminder = nutrientsReminder;
        nutrientsIn = nutrientsReminder;
        id = PlantIdKeeper.getPlantId();
    }


    public String getName() {
        return name;
    }

    /**
     * return false if unable to change name
     * @param name
     */
    public boolean setName(String name) {
        try{
            this.name = name;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getWaterReminder() {
        return waterReminder;
    }

    public boolean setWaterReminder(int waterReminder) {
        try{
            this.waterReminder = waterReminder;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getWaterIn() {
        return waterIn;
    }

    public boolean setWaterIn(int waterIn) {
        try{
            this.waterIn = waterIn;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getLastWatered() {
        return lastWatered;
    }

    public boolean setLastWatered(int lastWatered) {
        try{
            this.lastWatered = lastWatered;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getNutrientsReminder() {
        return nutrientsReminder;
    }

    public boolean setNutrientsReminder(int nutrientsReminder) {
        try{
            this.nutrientsReminder = nutrientsReminder;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getNutrientsIn() {
        return nutrientsIn;
    }

    public boolean setNutrientsIn(int nutrientsIn) {
        try{
            this.nutrientsIn = nutrientsIn;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getLastNutrients() {
        return lastNutrients;
    }

    public boolean setLastNutrients(int lastNutrients) {
        try{
            this.lastNutrients = lastNutrients;
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "[Name: " +  name + ", Water reminder: " + waterReminder + ", Water in: " + waterIn +
                ", Nutrients reminder: " + nutrientsReminder + ", Nutrients in: " + nutrientsIn +
                ", Id: " + id + "]";
    }
}
