package com.example.plant_app.model;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plant_app.adapters.PlantIdInfoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PlantParser extends AsyncTask<JSONObject, Void, List<PlantIdInfo>> {

    private static final String PLANT_PARSER_LOG_TAG = "log_plant_parser";

    private WeakReference<PlantIdInfoAdapter> mPlantInfoAdapter;
    private WeakReference<TextView> loadingTextView;
    private WeakReference<RecyclerView> mRecyclerView;
    private List<PlantIdInfo> plantInfos;



    public PlantParser(PlantIdInfoAdapter adapter, TextView loadingTextView, RecyclerView recyclerView) {
        plantInfos = PlantIdInfoList.getInstance();
        this.mPlantInfoAdapter = new WeakReference<>(adapter);
        this.loadingTextView = new WeakReference<>(loadingTextView);
        this.mRecyclerView = new WeakReference<>(recyclerView);
    }
    @Override
    protected List<PlantIdInfo> doInBackground(JSONObject... jsonObjects) {
        try {

            //clear the list and add the new items to the list
            plantInfos.clear();
            plantInfos.addAll(getPlants(jsonObjects[0]));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return plantInfos;
    }

    private List<PlantIdInfo> getPlants(JSONObject plantObj) throws JSONException {
        Log.d(PLANT_PARSER_LOG_TAG, "JSON: " + plantObj);
        JSONArray suggestions = plantObj.getJSONArray("suggestions");

        List<PlantIdInfo> plantInfoList = new ArrayList<>();
        for(int i=0; i < suggestions.length(); i++) {
            JSONObject suggestion = suggestions.getJSONObject(i);
            plantInfoList.add(getPlantInfo(suggestion));
        }

        return plantInfoList;
    }

    private PlantIdInfo getPlantInfo(JSONObject jsonObj) throws JSONException {



        String name = jsonObj.getString("plant_name");
        Log.d(PLANT_PARSER_LOG_TAG, name);


        JSONObject plantDetails = jsonObj.getJSONObject("plant_details");
        JSONArray commonNamesJson = plantDetails.getJSONArray("common_names");
        List<String> commonNames = new ArrayList<>();
        for(int i=0; i < commonNamesJson.length(); i++) {
            commonNames.add(commonNamesJson.getString(i));
        }
        Log.d(PLANT_PARSER_LOG_TAG, commonNames.toString());


        JSONObject wikiDescription = plantDetails.getJSONObject("wiki_description");
        String description = wikiDescription.getString("value");
        Log.d(PLANT_PARSER_LOG_TAG, description);


        String link = plantDetails.getString("url");
        Log.d(PLANT_PARSER_LOG_TAG, link);


        Double probability = jsonObj.getDouble("probability");
        Log.d(PLANT_PARSER_LOG_TAG, probability.toString());

        PlantIdInfo plantIdInfo = new PlantIdInfo(name, probability, commonNames, description, link);

        return plantIdInfo;
    }



    //Update the UI with the parsed data
    @Override
    protected void onPostExecute(List<PlantIdInfo> plantIdInfos) {
        super.onPostExecute(plantIdInfos);

        loadingTextView.get().setVisibility(View.INVISIBLE);
        mRecyclerView.get().setVisibility(View.VISIBLE);

        mPlantInfoAdapter.get().notifyDataSetChanged();
    }
}
