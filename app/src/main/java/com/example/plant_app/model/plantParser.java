package com.example.plant_app.model;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class plantParser extends AsyncTask<JSONObject, Void, List<String>> {

    private static final String PLANT_PARSER_LOG_TAG = "log_plant_parser";

    private List<Object> plantNames;


    public plantParser() {
        plantNames = new ArrayList<>();
    }
    @Override
    protected List<String> doInBackground(JSONObject... jsonObjects) {
        try {
            //clear the list and add the new items to the list
            //plantNames.addAll()
            getPlants(jsonObjects[0]);
            /*
            mWeatherList.get().clear();
            mWeatherList.get().addAll(getWeatherList(jsonObjects[0]));

            approvedTime = getApprovedTime(jsonObjects[0]);

            Coordinates.setApprovedTimeString(approvedTime);
            Coordinates.setApprovedTimeMillis(System.currentTimeMillis());
            Coordinates.setLatitude(lastLat);
            Coordinates.setLongitude(lastLon);

             */

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return mWeatherList.get();
        return null;
    }

    private void getPlants(JSONObject plantObj) throws JSONException {
        Log.d(PLANT_PARSER_LOG_TAG, "JSON: " + plantObj);

        List<Object> plnh = new ArrayList<>();

        //Get to the array holding the weather data
        //JSONArray timeSeries = weatherObj.getJSONArray(TIMESERIES);

        //One object for every hour for 10 days
        /*
        for(int i = 0; i < timeSeries.length(); i++) {
            JSONObject parametersAtTime = timeSeries.getJSONObject(i);
            weathers.add(getWeather(parametersAtTime));
        }

        return weathers;

         */
    }

    /*
    //Update the UI with the parsed data
    @Override
    protected void onPostExecute(List<Weather> weathers) {
        super.onPostExecute(weathers);

        mWeatherAdapter.get().notifyDataSetChanged();

        mLastApprovedTime.get().setText(approvedTime);
        mLonTextView.get().setText(lastLon);
        mLatTextView.get().setText(lastLat);
    }

     */


}
