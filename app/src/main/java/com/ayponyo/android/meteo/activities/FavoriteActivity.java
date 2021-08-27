package com.ayponyo.android.meteo.activities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayponyo.android.meteo.APIManager;
import com.ayponyo.android.meteo.R;
import com.ayponyo.android.meteo.Util;
import com.ayponyo.android.meteo.adapters.FavoriteAdapter;
import com.ayponyo.android.meteo.databinding.ActivityFavoritesBinding;
import com.ayponyo.android.meteo.models.City;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;
import java.util.ArrayList;


public class FavoriteActivity extends AppCompatActivity implements APIManager {
    private ActivityFavoritesBinding binding;
    private RecyclerView mRecyclerViewCityFavorite;
    private FavoriteAdapter mAdapter;
    private FloatingActionButton mFloatingButtonSearch;
    private CoordinatorLayout mCoordinatorLayoutFavorite;
    private ArrayList<City> mCities;

    @Override
    protected void onResume() {
        super.onResume();
        mCities = Util.initFavoriteCities(this);
        refreshWeather();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Util.clearFavouriteCities(this);*/


        /* implementation scrolling template */
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getString(R.string.favorites).toUpperCase());

        FloatingActionButton fab = binding.floatingButtonSearch;
        fab.setOnClickListener( view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        mCoordinatorLayoutFavorite = binding.coordinatorLayoutFavorite;



        mCities = Util.initFavoriteCities(this);
        if(mCities.size() > 0)
            refreshWeather();


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

                      int position = viewH.getBindingAdapterPosition();
                      City cityRemoved = mCities.get(position);
                      mCities.remove(position);

                      Util.saveFavouriteCities(FavoriteActivity.this, mCities);
                      mAdapter.updateData(mCities);

                      /*mAdapter.notifyDataSetChanged();*/

                      Snackbar.make(mCoordinatorLayoutFavorite, cityRemoved.getName() + " est supprimé.", Snackbar.LENGTH_LONG).setAction(R.string.cancel, new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {
                              mCities.add(position, cityRemoved);
                              Util.saveFavouriteCities(FavoriteActivity.this, mCities);
                              mAdapter.updateData(mCities);

                            /* mAdapter.notifyDataSetChanged();*/

                          }
                      }).show();
                  }
              });
        itemTouchHelper.attachToRecyclerView(mRecyclerViewCityFavorite);

        /* add action button add fav. */
        binding.floatingButtonSearch.setOnClickListener(mClickSearchButton);

    }
    private void refreshWeather(){
        for (City city : mCities){
            newCall(getCityWeatherByIdCity(city.getId()));
        }
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
                    newCall(getCityWeatherByName(newCityName));

                    dialog.cancel();
                });
                builder.setNegativeButton(R.string.cancel, (dialog, WhichButton) -> dialog.cancel());
                builder.create().show();

            };


    @Override
    public void manageResponseAPI(String json) {
        City newCity = Util.gson.fromJson(json, City.class);
        Log.d("REFRESH",!newCity.isAlreadyPresent(mCities) + " " );
        if(!newCity.isAlreadyPresent(mCities)){
            Log.d("REFRESH"," no present " );

            mCities.add(newCity);
            Util.saveFavouriteCities(this, mCities);
            mAdapter.updateData(mCities);

        }else{
            Log.d("REFRESH", "present");
            int position = newCity.getPosition( mCities);
            if(position != -1){
                mCities.get(position).setWeather(newCity.getWeather());
                mCities.get(position).setMain(newCity.getMain());
                mAdapter.updateData(mCities);

            }
        }
    }

    @Override
    public void manageResponseFailAPI(IOException e) {
        Toast.makeText(this,R.string.error_search_city, Toast.LENGTH_SHORT).show();

    }
}