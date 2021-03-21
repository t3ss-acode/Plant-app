package com.example.plant_app.model;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.plant_app.PlantIdInfoAdapter;
import com.example.plant_app.R;

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

        /*
        JSON: {
            "id":11367613,
            "custom_id":null,
            "meta_data":{"latitude":null,"longitude":null,"date":"2021-03-20","datetime":"2021-03-20"},
            "uploaded_datetime":1.616283028070543E9,
            "finished_datetime":1.616283029514167E9,
            "images":[{
                "file_name":"5f08219dc4fd4be98ea009ea389f2bce.jpg",
                "url":"https:\/\/plant.id\/media\/images\/5f08219dc4fd4be98ea009ea389f2bce.jpg"}],
            "suggestions":[
            {
                "id":84204960,
                "plant_name":"Kalanchoe blossfeldiana",
                "plant_details":{
                    "scientific_name":"Kalanchoe blossfeldiana",
                    "structured_name":{"genus":"kalanchoe","species":"blossfeldiana"},
                    "common_names":["Flaming Katy","Kalanchoe","Christmas kalanchoe","florist kalanchoe","Madagascar widow's-thrill"],
                    "url":"https:\/\/en.wikipedia.org\/wiki\/Kalanchoe_blossfeldiana",
                    "name_authority":"Kalanchoe blossfeldiana Poelln.",
                    "wiki_description":{
                        "value":"Kalanchoe blossfeldiana is a herbaceous and commonly cultivated house plant of the genus Kalanchoe native to Madagascar. It is known by the English common names flaming Katy, Christmas kalanchoe, florist kalanchoe and Madagascar widow's-thrill.",
                        "citation":"https:\/\/en.wikipedia.org\/wiki\/Kalanchoe_blossfeldiana",
                        "license_name":"CC BY-SA 3.0",
                        "license_url":"https:\/\/creativecommons.org\/licenses\/by-sa\/3.0\/"},
                    "taxonomy":{
                        "class":"Magnoliopsida",
                        "family":"Crassulaceae",
                        "genus":"Kalanchoe",
                        "kingdom":"Plantae",
                        "order":"Saxifragales",
                        "phylum":"Magnoliophyta"},
                    "synonyms":["Kalanchoe coccinea","Kalanchoe coccinea var. blossfeldiana"]},
                "probability":0.9677596899366447,
                "confirmed":false
            },
            {
                "id":84204961,
                "plant_name":"Kalanchoe",
                "plant_details":{"scientific_name":"Kalanchoe","structured_name":{"genus":"kalanchoe"},"common_names":["Kalanchoë"],"url":"https:\/\/en.wikipedia.org\/wiki\/Kalanchoe","name_authority":"Kalanchoe Adans.","wiki_description":{"value":"Kalanchoe , or kal-un-KOH-ee, or kal-un-kee, also written Kalanchöe or Kalanchoë, is a genus of about 125 species of tropical, succulent flowering plants in the family Crassulaceae, mainly native to Madagascar and tropical Africa. Kalanchoe was one of the first plants to be sent into space, sent on a resupply to the Soviet Salyut 1 space station in 1979.","citation":"https:\/\/en.wikipedia.org\/wiki\/Kalanchoe","license_name":"CC BY-SA 3.0","license_url":"https:\/\/creativecommons.org\/licenses\/by-sa\/3.0\/"},"taxonomy":{"class":"Magnoliopsida","family":"Crassulaceae","genus":"Kalanchoe","kingdom":"Plantae","order":"Saxifragales","phylum":"Magnoliophyta"},"synonyms":["Bryophyllum","Crassuvia","Geaya","Kitchingia","Meristostylus","Physocalycium","Vereia"]},
                "probability":0.01368565241206563,
                "confirmed":false
            }
            ],
        "modifiers":["crops_fast","similar_images"],
        "secret":"E2jUk1IQzfGtBV9",
        "fail_cause":null,
        "countable":true,
        "feedback":null,
        "is_plant_probability":0.998221614,
        "is_plant":true}
*/

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
