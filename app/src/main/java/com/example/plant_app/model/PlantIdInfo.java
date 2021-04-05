package com.example.plant_app.model;

import java.io.File;
import java.util.List;

public class PlantIdInfo {

    private String name;
    private double probability;
    private List<String> commonNames;
    private String description;
    private String link;


    public PlantIdInfo(String name, double probability, List<String> commonNames, String description, String link) {
        this.name = name;
        this.probability = probability;
        this.commonNames = commonNames;
        this.description = description;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public double getProbability() {
        return probability;
    }
    public String getProbabilityString() {
        double percent = (100 * probability);
        //System.out.println(String.format("%.0f%%",percent));

        return String.format("%.0f%%", percent);
    }

    public List<String> getCommonNames() {
        return commonNames;
    }
    public String getCommonNamesString() {
        String str = "";
        for(int i=0; i < commonNames.size(); i++) {
            str = str + commonNames.get(i);
            if(i+1 != commonNames.size())
                str = str + ", ";
        }
        return str;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", probability: " + probability + ", common names: " + commonNames.toString()
                + ", description: " + description + ", link: " + link;
    }
}
