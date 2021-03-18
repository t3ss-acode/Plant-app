package com.example.plant_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant_app.model.Plant;
import com.example.plant_app.model.PlantList;

import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private String SINGLE_DAY_IN = " day";
    private String MORE_DAYS_IN = " days";
    private String SINGLE_DAY_AGO = " day ago";
    private String MORE_DAYS_AGO = " days ago";

    private List<Plant> plants = PlantList.getInstance();


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView waterInXDaysTextView;
        public TextView wateredXDaysAgoTextView;

        public TextView nutrientsInXDaysTextView;
        public TextView nutrientsXDaysAgoTextView;
        public TextView nutrientsInTextView;
        public TextView nutrientsAgoTextView;

        public ViewHolder(View v) {
            super(v);
        }
    }


    @NonNull
    @Override
    public PlantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_item, parent, false);
        final ViewHolder vh = new ViewHolder(itemView);

        vh.nameTextView = itemView.findViewById(R.id.plant_name_text);
        vh.waterInXDaysTextView = itemView.findViewById(R.id.water_in_x_days_text);
        vh.wateredXDaysAgoTextView = itemView.findViewById(R.id.watered_x_days_ago_text);

        vh.nutrientsInXDaysTextView = itemView.findViewById(R.id.nutrients_in_x_days_text);
        vh.nutrientsXDaysAgoTextView = itemView.findViewById(R.id.nutrients_x_days_ago_text);
        vh.nutrientsInTextView = itemView.findViewById(R.id.nutrients_in_text);
        vh.nutrientsAgoTextView = itemView.findViewById(R.id.given_nutrients_text);

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        Plant plant = plants.get(position);

        //set the plant data for every view item
        vh.nameTextView.setText(plant.getName());
        vh.waterInXDaysTextView.setText(checkIfSingleDayIn(plant.getWaterReminder()));
        vh.wateredXDaysAgoTextView.setText(checkIfSingleDayAgo(plant.getLastWatered()));
        if(plant.getNutrientsReminder() != -1) {
            vh.nutrientsInTextView.setVisibility(View.VISIBLE);
            vh.nutrientsInXDaysTextView.setVisibility(View.VISIBLE);
            vh.nutrientsInXDaysTextView.setText(checkIfSingleDayIn(plant.getNutrientsReminder()));
            vh.nutrientsAgoTextView.setVisibility(View.VISIBLE);
            vh.nutrientsXDaysAgoTextView.setVisibility(View.VISIBLE);
            vh.nutrientsXDaysAgoTextView.setText(checkIfSingleDayAgo(plant.getLastNutrients()));
        }
    }

    private String checkIfSingleDayIn(int days) {
        if(days > 1) {
            return " " + days + MORE_DAYS_IN;
        }
        return " " + days + SINGLE_DAY_IN;
    }

    private String checkIfSingleDayAgo(int days) {
        if(days == -1)
            return " -" + MORE_DAYS_AGO;
        if(days > 1)
            return " " + days + MORE_DAYS_AGO;
        return " " + days + SINGLE_DAY_AGO;
    }


    @Override
    public int getItemCount() {
        return plants.size();
    }
}
