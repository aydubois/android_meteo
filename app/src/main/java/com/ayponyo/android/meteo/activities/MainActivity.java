package com.ayponyo.android.meteo.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ayponyo.android.meteo.APIManager;
import com.ayponyo.android.meteo.R;
import com.ayponyo.android.meteo.Util;
import com.ayponyo.android.meteo.databinding.ActivityMainBinding;
import com.ayponyo.android.meteo.models.City;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements APIManager {

    private ActivityMainBinding mBinding;
    private City mCity = new City();
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* initialisation binding */
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        /* ajout des datas */
        mBinding.setCity(mCity);
        mBinding.setImagefr(getDrawable(R.drawable.imagefr));
        mBinding.setImageno(getDrawable(R.drawable.imageno));

        setContentView(mBinding.getRoot());

        if (Util.isConnected(this)) {
            mBinding.setIsConnected(true);
            /*updateCoordinates();*/
            mBinding.buttonFavorites.setOnClickListener(mFavoriteListener);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            // method to get the location
            // https://www.geeksforgeeks.org/how-to-get-user-location-in-android/
            getLastLocation();

        } else {
            mBinding.setIsConnected(false);

        }

    }
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // location enabled ?
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                // last location ?
                mFusedLocationClient.getLastLocation().addOnCompleteListener( task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        callApi(location);
                    }
                });
            } else {
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // request permissions
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Util.REQUEST_CODE);

        }
    }
    private void callApi(Location location){
        mCurrentLocation = location;
        String lat = String.valueOf(mCurrentLocation.getLatitude());
        String lng = String.valueOf(mCurrentLocation.getLongitude());
        newCall(getCityWeatherByCoord(lat, lng));
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // request for location - once
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            callApi(locationResult.getLastLocation());
        }
    };

    private final View.OnClickListener mFavoriteListener = v -> {
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
            };

    @Override
    public void manageResponseAPI(String json) {
        Log.d("JSON", json);
        mCity = Util.gson.fromJson(json, City.class);
        mBinding.setCity(mCity);
    }

    @Override
    public void manageResponseFailAPI(IOException e) {
        Log.d("API_TEST", "ioups ... "+ e.getMessage());
    }
}