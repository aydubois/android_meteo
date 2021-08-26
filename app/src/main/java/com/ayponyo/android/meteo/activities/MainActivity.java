package com.ayponyo.android.meteo.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ayponyo.android.meteo.R;
import com.ayponyo.android.meteo.Util;
import com.ayponyo.android.meteo.databinding.ActivityMainBinding;
import com.ayponyo.android.meteo.models.City;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewCityName;
    private TextView mTextViewErrorNetwork;
    private TextView mTextViewDesc;
    private TextView mTextViewTemp;
    private ImageView mImageView;
    private Button mButtonFavorites;
    private LinearLayout mLinearLayoutMain;

    private ActivityMainBinding mBinding;
    /*private TextInputEditText mTextInputMessage;*/

    private City mCurrentCity;

    private OkHttpClient mHttpClient;
    private Handler mHandler;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_main);*/
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        City city = new City("TEST", "FEFAE efae ","3", 23);
        mBinding.setCity(city);

        mBinding.setImagefr(getDrawable(R.drawable.imagefr));
        mBinding.setImageno(getDrawable(R.drawable.imageno));
        setContentView(mBinding.getRoot());
        mHandler = new Handler();

        /* recovery of the template tags */
        mTextViewCityName = findViewById(R.id.text_view_city_name);
        mTextViewTemp = findViewById(R.id.text_view_temp);
        mTextViewDesc = findViewById(R.id.text_view_desc);
        mImageView = findViewById(R.id.image_view);
        mTextViewErrorNetwork = findViewById(R.id.text_view_error_network);
        mButtonFavorites = findViewById(R.id.button_favorites);
        mLinearLayoutMain = findViewById(R.id.linear_layout_main);
        /*mTextInputMessage = findViewById(R.id.text_input_message);*/


        /* check if network is connected */
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("CONNECT_TEST", "connected");
            /* just a test with toast */
            Toast.makeText(this,mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this,mTextViewCityName.getText(), Toast.LENGTH_SHORT).show();

            /* add action on the template */
            mTextViewCityName.setText(R.string.city_name);
            mButtonFavorites.setOnClickListener(mFavoriteListener);


            /* initialisation http client */
            mHttpClient = new OkHttpClient();
            Log.d("API_TEST", "before request");
            sendRequestAPI();
        } else {

            /* set error message */
            Log.d("CONNECT_TEST", "no connection");
            mTextViewErrorNetwork.setVisibility(View.VISIBLE);
            mButtonFavorites.setVisibility(View.GONE);
            mLinearLayoutMain.setVisibility(View.GONE);
        }

    }
    private final View.OnClickListener mFavoriteListener =
            v -> {
                /* change activity */
                /*String textMessage = Objects.requireNonNull(mTextInputMessage.getText()).toString();*/
                Intent intent = new Intent(this, FavoriteActivity.class);
                /*intent.putExtra(Util.key_message, textMessage);*/
                startActivity(intent);
            };

    private void sendRequestAPI(){
        Request request = new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?lat=47.390026&lon=0.688891&appid="+Util.API_KEY).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("API_TEST", "ioups ... "+ e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String stringJson = response.body().string();
                    Log.d("API_TEST", stringJson);

                    mHandler.post(new Runnable() {
                        public void run() {
                            renderCurrentWeather(stringJson, 1);
                        }
                    });
                }
            }
        });
        Request request2 = new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q=Montreal&appid="+Util.API_KEY).build();
        mHttpClient.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("API_TEST", "ioups ... "+ e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String stringJson = response.body().string();
                    Log.d("API_TEST", stringJson);

                    mHandler.post(new Runnable() {
                        public void run() {
                            renderCurrentWeather(stringJson,2);
                        }
                    });
                }
            }
        });
    }

    private void renderCurrentWeather(String sJson, int numRequete){
        try{
            City newCity = new City(sJson);
            mTextViewCityName.setText(newCity.getmName());
            mTextViewDesc.setText(newCity.getmDescription());
            mTextViewTemp.setText(newCity.getmTemperature());
            mImageView.setImageResource(newCity.getmWeatherIcon());

            if(numRequete == 1){

                mBinding.setCity(newCity);
                /*mBinding.executePendingBindings();*/
                mBinding.setLifecycleOwner(this);
                Log.d("TEST_BINDING", mBinding.getCity().toString());
                Log.d("TEST_BINDING", "text" + mBinding.test.getText().toString());
            }
            if(numRequete == 2){
                mBinding.setCity2(newCity);
                mBinding.setLifecycleOwner(this);
            }
        }catch(JSONException e){
            Log.d("ERROR_JSON_CITY", e.getMessage());
            Log.d("ERROR_JSON_CITY", e.getLocalizedMessage());
            Log.d("ERROR_JSON_CITY", e.getCause().toString());
        }
    }

}