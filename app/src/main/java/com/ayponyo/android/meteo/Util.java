package com.ayponyo.android.meteo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ayponyo.android.meteo.models.City;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Util {
    /* keys for extras */
    public static final String key_message = "message";
    public static final String API_KEY = Keys.API_KEYS;
    public static final String PREFS_NAME = "cities";
    public static final String PREFS_FAVORITE_CITIES = "favorite_cities";
    public static final Gson gson = new Gson();
    /* capitalize first letter of word/sentence */
    public static String capitalize(String s) {
        if (s == null || s.length() == 0 ) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }else{
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }

/*    *//*
     * Méthode qui initialise l'icon blanc présent dans la MainActivity
     * *//*
    public static int setWeatherIcon(int actualId, long sunrise, long sunset) {
        Log.d("ICON", actualId+" "+ sunrise+" "+ sunset);
        int id = actualId / 100;
        int icon = R.drawable.weather_sunny_white;

        if (actualId == 800) {
            long currentTime = new Date().getTime();

            if (currentTime >= sunrise && currentTime < sunset) {
                icon = R.drawable.weather_sunny_white;
            } else {
                icon = R.drawable.weather_clear_night_white;
            }
        } else {
            switch (id) {
                case 2:
                    icon = R.drawable.weather_thunder_white;
                    break;
                case 3:
                    icon = R.drawable.weather_drizzle_white;
                    break;
                case 7:
                    icon = R.drawable.weather_foggy_white;
                    break;
                case 8:
                    icon = R.drawable.weather_cloudy_white;
                    break;
                case 6:
                    icon = R.drawable.weather_snowy_white;
                    break;
                case 5:
                    icon = R.drawable.weather_rainy_white;
                    break;
            }
        }
        Log.d("ICON", icon+"");
        return icon;
    }*/

    public static int setWeatherIcon(int actualId) {

        int id = actualId / 100;
        int icon = R.drawable.weather_sunny_grey;

        if (actualId != 800) {
            switch (id) {
                case 2:
                    icon = R.drawable.weather_thunder_grey;
                    break;
                case 3:
                    icon = R.drawable.weather_drizzle_grey;
                    break;
                case 7:
                    icon = R.drawable.weather_foggy_grey;
                    break;
                case 8:
                    icon = R.drawable.weather_cloudy_grey;
                    break;
                case 6:
                    icon = R.drawable.weather_snowy_grey;
                    break;
                case 5:
                    icon = R.drawable.weather_rainy_grey;
                    break;
            }
        }

        return icon;
    }
    public static boolean isConnected(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;

    }
    public static String convertKelvinToCelsuis(double kelvin){
        return Math.round((kelvin - 273.15) * 100.0) / 100.0+" °C";
    }

    public static  void clearFavouriteCities(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Util.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
        editor.apply();
    }
    public static void saveFavouriteCities(Context context, ArrayList<City> cities) {
        SharedPreferences preferences = context.getSharedPreferences(Util.PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        JSONArray jsonArrayCities = new JSONArray();
        for (int i = 0; i < cities.size(); i++) {
            jsonArrayCities.put(gson.toJson(cities.get(i)));
        }

        editor.putString(Util.PREFS_FAVORITE_CITIES, jsonArrayCities.toString());
        Log.d("PREFERENCES", "SAVE === >  "+jsonArrayCities.toString());
        editor.apply();
    }

    public static ArrayList<City> initFavoriteCities(Context context) {
        ArrayList<City> cities = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Util.PREFS_NAME,
                Context.MODE_PRIVATE);
        Log.d("PREFERENCES", "PREFERENCES +++>  "+preferences.getString(Util.PREFS_FAVORITE_CITIES,""));
        if(preferences.getString(Util.PREFS_FAVORITE_CITIES,"").length() > 0){
            try {
                JSONArray jsonArray = new JSONArray(preferences.getString(Util.PREFS_FAVORITE_CITIES,""));
                for (int i = 0; i < jsonArray.length(); i++) {
                    /*JSONObject jsonObjectCity = new JSONObject(jsonArray.getString(i));*/
                    City city = gson.fromJson(jsonArray.getString(i), City.class);
/*                    Log.d("PREFERENCES", city.toString());*/
                    cities.add(city);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return cities;
    }



}
