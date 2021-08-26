package com.ayponyo.android.meteo.activities;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayponyo.android.meteo.R;
import com.ayponyo.android.meteo.Util;
import com.ayponyo.android.meteo.adapters.FavoriteAdapter;
import com.ayponyo.android.meteo.database.DatabaseHelper;
import com.ayponyo.android.meteo.databinding.ActivityFavoritesBinding;
import com.ayponyo.android.meteo.models.City;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavoriteActivity extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private RecyclerView mRecyclerViewCityFavorite;
    private FavoriteAdapter mAdapter;
    private FloatingActionButton mFloatingButtonSearch;
    private CoordinatorLayout mCoordinatorLayoutFavorite;
    private ArrayList<City> mCities;

    private OkHttpClient mHttpClient;
    private Handler mHandler;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* implementation scrolling template */
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new DatabaseHelper(this);


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.favorites).toUpperCase());

        FloatingActionButton fab = binding.floatingButtonSearch;
        fab.setOnClickListener( view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        mCoordinatorLayoutFavorite = findViewById(R.id.coordinator_layout_favorite);
        /* just a test with received data */
       /* Bundle extras = getIntent().getExtras();
        String textMessage = extras.getString(Util.key_message);*/


        /* add favorite cities */
        /*City city1 = new City("Montréal", "Légères pluies", "22°C", R.drawable.weather_rainy_grey);
        City city2 = new City("New York", "Ensoleillé", "22°C", R.drawable.weather_sunny_grey);
        City city3 = new City("Paris", "Nuageux", "24°C", R.drawable.weather_foggy_grey);
        City city4 = new City("Toulouse", "Pluies modérées", "20°C", R.drawable.weather_rainy_grey);
        mCities.add(city1);
        mCities.add(city2);
        mCities.add(city3);
        mCities.add(city4);*/
        mCities = db.getAllCities();

        mHandler = new Handler();
        mHttpClient = new OkHttpClient();
        /*ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            sendRequestAPI();
        }else{

        }*/

        /* implementation action on the recycler */
        mRecyclerViewCityFavorite = findViewById(R.id.recycler_view_city_favourite);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerViewCityFavorite.setLayoutManager(layoutManager);
        mAdapter =  new FavoriteAdapter(this, mCities);
        mRecyclerViewCityFavorite.setAdapter(mAdapter);

        /* implementation swipe */
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new
              ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT)
              {
                  @Override
                  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
                          RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                      return false;
                  }
                  @Override
                  public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int
                          direction) {
                      FavoriteAdapter.ViewHolder viewH = (FavoriteAdapter.ViewHolder) viewHolder;
                      /*int position = viewH.getLayoutPosition();*/
                      int position = viewH.getBindingAdapterPosition();
                      City cityRemoved = mCities.get(position);
                      mCities.remove(position);
                      db.deleteCityInBase(cityRemoved.mIdDataBase);
                      mAdapter.notifyDataSetChanged();

                      Snackbar.make(mCoordinatorLayoutFavorite, cityRemoved.mName + "est supprimé", Snackbar.LENGTH_LONG).setAction(R.string.cancel, new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              mCities.add(position,cityRemoved);
                              db.addCityInBase(cityRemoved);
                              mAdapter.notifyDataSetChanged();

                          }
                      }).show();
                  }
              });
        itemTouchHelper.attachToRecyclerView(mRecyclerViewCityFavorite);

        /* add action button add fav. */
        mFloatingButtonSearch = findViewById(R.id.floating_button_search);
        mFloatingButtonSearch.setOnClickListener(mClickSearchButton);

    }

    private final View.OnClickListener mClickSearchButton =
            v -> {
                /* opening a modal with edit text & 2 buttons */
                final AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);
                builder.setTitle(R.string.add_city);

                View view = LayoutInflater.from(FavoriteActivity.this).inflate(R.layout.dialog_add_favorite, null);
                final EditText editTextCity = view.findViewById(R.id.edit_text_new_city);
                builder.setView(view);

                builder.setPositiveButton(R.string.ok, (dialog, WhichButton) -> {
                    String newCityName = editTextCity.getText().toString();
                    /*Log.d("JSON_TEST", newCityName);*/
                    sendRequestAPI(newCityName);
                    dialog.cancel();
                });
                builder.setNegativeButton(R.string.cancel, (dialog, WhichButton) -> dialog.cancel());
                builder.create().show();

            };
    private final void sendRequestAPI(String name){
       /* Log.d("JSON_TEST","go request");*/
        Request request = new Request.Builder().url("http://api.openweathermap.org/data/2.5/weather?q="+name+"&appid="+Util.API_KEY).build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("API_TEST", "ioups ... "+ e.getMessage());
                mHandler.post(new Runnable() {
                    public void run() {
                        sendMessageError();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String stringJson = response.body().string();

                    mHandler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void run() {
                            renderCurrentWeather(stringJson);

                        }
                    });
                }else{
                    mHandler.post(new Runnable() {
                        public void run() {
                            sendMessageError();
                        }
                    });
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void renderCurrentWeather(String sJson){
        try{
            City newCity = new City(sJson);
           /* Gson gson = new Gson();
            City newCity = gson.fromJson(sJson, City.class);*/
            if(!newCity.isAlreadyPresent(mCities)){
                mCities.add(newCity);
                db.addCityInBase(newCity);
                mAdapter.notifyDataSetChanged();
            }

        }catch(JSONException e){
            Log.d("ERROR_JSON_CITY",sJson);
            Log.d("ERROR_JSON_CITY", e.getMessage());
            sendMessageError();
        }
    }
    private void sendMessageError(){
        Toast.makeText(this,R.string.error_search_city, Toast.LENGTH_SHORT).show();
        Log.d("TEST_ERROR", getString(R.string.error_search_city));
    }


}