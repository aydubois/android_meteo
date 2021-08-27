package com.ayponyo.android.meteo;

import android.app.DownloadManager;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public interface APIManager {
    OkHttpClient mHttpClient = new OkHttpClient();
    Handler mHandler = new Handler();

    default Request getCityWeatherByName(String name){
        return new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q="+name+"&appid="+Util.API_KEY).build();
    }

    default Request getCityWeatherByIdCity(int id){
        return new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?id="+id+"&appid="+Util.API_KEY).build();
    }

    default Request getCityWeatherByCoord(String lat, String lng){
        return new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lng+"&appid="+Util.API_KEY).build();
    }


    default void newCall(Request request){

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                manageResponseFailAPI(e);
                Log.d("API_TEST", "ioups ... "+ e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String stringJson = response.body().string();
                    Log.d("API_TEST", stringJson);

                    mHandler.post(() -> manageResponseAPI(stringJson));
                }
            }
        });
    }
    void manageResponseAPI(String json);
    void manageResponseFailAPI(IOException e);
}
