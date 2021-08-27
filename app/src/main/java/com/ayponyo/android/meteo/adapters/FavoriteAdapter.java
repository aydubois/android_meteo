package com.ayponyo.android.meteo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ayponyo.android.meteo.databinding.ItemFavoriteCityBinding;
import com.ayponyo.android.meteo.models.City;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<City> mCities;

    public FavoriteAdapter(Context context, ArrayList<City> cities) {
        mContext = context;
        mCities = cities;

    }

    public void  updateData(ArrayList<City> mCities) {
        this.mCities.clear();
        this.mCities.addAll(mCities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemFavoriteCityBinding itemFavoriteCityBinding = ItemFavoriteCityBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemFavoriteCityBinding);
    }


    @Override
    public void onBindViewHolder( FavoriteAdapter.ViewHolder holder, int position) {
        /* add data into template */
        City city = mCities.get(position);
        Log.d("PREFERENCES","TEST ===> "+city.getName());
        holder.mBinding.setCityfav(city);
        holder.mBinding.executePendingBindings();
        /*holder.mCityFav = city;*/

    }



    @Override
    public int getItemCount() {
        return mCities.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public ItemFavoriteCityBinding mBinding;
        /*public City mCityFav;*/

        public ViewHolder(ItemFavoriteCityBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

        }

    }
}

