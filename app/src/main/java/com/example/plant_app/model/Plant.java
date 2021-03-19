package com.example.plant_app.model;

public class Plant implements java.io.Serializable {

    private String name;
    private int waterReminder;
    private int lastWatered = -1;
    private int nutrientsReminder = -1;
    private int lastNutrients = -1;


    public Plant(String name, int waterReminder) {
        this.name = name;
        this.waterReminder = waterReminder;
    }

    public Plant(String name, int waterReminder, int nutrientsReminder) {
        this.name = name;
        this.waterReminder = waterReminder;
        this.nutrientsReminder = nutrientsReminder;
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


    @Override
    public String toString() {
        return "[Name: " +  name + ", Water in: " + waterReminder + ", Last watered: " + lastWatered + "]";
    }
}
