package com.example.plant_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plant_app.R;
import com.example.plant_app.model.PlantIdInfo;
import com.example.plant_app.model.PlantIdInfoList;

import java.util.List;

public class PlantIdInfoAdapter extends RecyclerView.Adapter<PlantIdInfoAdapter.ViewHolder> {

    private List<PlantIdInfo> plantInfos = PlantIdInfoList.getInstance();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView probabilityTextView;
        public TextView commonNamesTextView;
        public TextView descriptionTextView;
        public TextView linkTextView;

        public ViewHolder(View v) {
            super(v);
        }
    }

    @NonNull
    @Override
    public PlantIdInfoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.plant_id_info_item, parent, false);
        final PlantIdInfoAdapter.ViewHolder vh = new PlantIdInfoAdapter.ViewHolder(itemView);

        vh.nameTextView = itemView.findViewById(R.id.plant_info_name_text);
        vh.probabilityTextView = itemView.findViewById(R.id.probability_text);
        vh.commonNamesTextView = itemView.findViewById(R.id.common_names_text);
        vh.descriptionTextView = itemView.findViewById(R.id.description_text);
        vh.linkTextView = itemView.findViewById(R.id.link_text);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        PlantIdInfo plantInfo = plantInfos.get(position);

        //set the plant data for every view item
        vh.nameTextView.setText(plantInfo.getName());
        vh.probabilityTextView.setText(plantInfo.getProbabilityString());
        vh.commonNamesTextView.setText(plantInfo.getCommonNamesString());
        vh.descriptionTextView.setText(plantInfo.getDescription());
        vh.linkTextView.setText(plantInfo.getLink());
    }

    @Override
    public int getItemCount() {
        return plantInfos.size();
    }
}
