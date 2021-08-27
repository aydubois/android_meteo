package com.ayponyo.android.meteo.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ayponyo.android.meteo.APIManager;
import com.ayponyo.android.meteo.R;
import com.ayponyo.android.meteo.Util;
import com.ayponyo.android.meteo.databinding.ActivityMainBinding;
import com.ayponyo.android.meteo.models.City;
import com.google.gson.Gson;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements APIManager {

    private ActivityMainBinding mBinding;
    private City mCity = new City();
    private Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* initialisation binding */
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        /* ajout des datas */
        /* peuvent être sous forme d'objets / de drawables / int / string / list ... */
        mBinding.setCity(mCity);
        mBinding.setImagefr(getDrawable(R.drawable.imagefr));
        mBinding.setImageno(getDrawable(R.drawable.imageno));

        setContentView(mBinding.getRoot());


        if(Util.isConnected(this)){
            mBinding.setIsConnected(true);
            newCall(getCityWeatherByName("Tours"));

            /* possibilité d'accéder aux balises via les id */
            mBinding.buttonFavorites.setOnClickListener(mFavoriteListener);
        } else {
            /* encore plus simple que les id ^_^ */
            mBinding.setIsConnected(false);

            //mBinding.textViewErrorNetwork.setVisibility(View.VISIBLE);
            //mBinding.buttonFavorites.setVisibility(View.GONE);
            //mBinding.linearLayoutMain.setVisibility(View.GONE);
        }

    }
    private final View.OnClickListener mFavoriteListener = v -> {
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
            };

    @Override
    public void manageResponseAPI(String json) {
        Log.d("JSON", json);
        mCity = gson.fromJson(json, City.class);
        mBinding.setCity(mCity);
    }

    @Override
    public void manageResponseFailAPI(IOException e) {
        Log.d("API_TEST", "ioups ... "+ e.getMessage());
    }
}