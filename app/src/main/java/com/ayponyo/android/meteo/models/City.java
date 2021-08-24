package com.ayponyo.android.meteo.models;


import android.util.Log;

import com.ayponyo.android.meteo.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class City {
    public String mName;
    public String mDescription;
    public double mTemperature;
    public int mWeatherIcon;
    public int mIdCity;
    public double mLongitude;
    public double mLatitude;
    public int mWeatherResIconWhite;

    public City (String name, String description, String temperature, int weatherIcon){
        mName = name;
        mDescription = description;
        mTemperature = parseInt(temperature);
        mWeatherIcon = weatherIcon;
    }

    public City (String sJson) throws JSONException {
        JSONObject json = new JSONObject(sJson);

        mName = json.getString("name");
        mIdCity = json.getInt("id");
      /*  JSONObject jsonO = new JSONObject(stringJson);
        JSONArray jsonArray = jsonO.getJSONArray("Search");*/
        /*JSONArray coord = json.getJSONArray("coord");
        Log.d("TEST_JSON", coord.get(0).toString());
        Log.d("TEST_JSON", coord.get(1).toString());*/
        JSONObject coord = json.getJSONObject("coord");
        mLongitude = coord.getDouble("lon");
        mLatitude = coord.getDouble("lat");

        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
        mDescription = weather.getString("description");

        JSONObject main = new JSONObject(json.getString("main"));
        mTemperature = Util.convertKelvinToCelsuis(main.getInt("temp"));

        JSONObject sys = new JSONObject(json.getString("sys"));
        mWeatherResIconWhite =  weather.getInt("id");
        long sunset = sys.getLong("sunset");
        long sunrise = sys.getLong("sunrise");
        mWeatherIcon = Util.setWeatherIcon(mWeatherResIconWhite, sunrise, sunset);
    }


    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmTemperature() {
        return mTemperature +" °C";
    }

    public int getmWeatherIcon() {
        return mWeatherIcon;
    }

    public int getmIdCity() {
        return mIdCity;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public int getmWeatherResIconWhite() {
        return mWeatherResIconWhite;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        String name = ((City) obj).getmName();
        return this.getmName().equals(name) && this.getmIdCity() == ((City) obj).getmIdCity() ;
    }
    
    public boolean isAlreadyPresent(ArrayList<City> cities){
        for ( City city :cities) {
            if(this.equals(city)){
                return true;
            }
        }
        return false;
    }
}
