package com.ayponyo.android.meteo;

import java.util.Date;

public class Util {
    /* keys for extras */
    public static final String key_message = "message";
    public static final String API_KEY = Keys.API_KEYS;
    /* capitalize first letter of word/sentence */
    public static String capitalize(String s) {
        if (s == null || s.length() == 0 ) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }else{
            return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
    }

    /*
     * Méthode qui initialise l'icon blanc présent dans la MainActivity
     * */
    public static int setWeatherIcon(int actualId, long sunrise, long sunset) {

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

        return icon;
    }

    public static String convertKelvinToCelsuis(int kelvin){
        return String.valueOf(Math.round((kelvin - 273.15) * 100.0) / 100.0)+" °C";
    }
}
