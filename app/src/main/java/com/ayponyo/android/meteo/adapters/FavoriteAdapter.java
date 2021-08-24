package com.ayponyo.android.meteo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ayponyo.android.meteo.R;
import com.ayponyo.android.meteo.models.City;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<City> mCities;

    public FavoriteAdapter(Context context, ArrayList<City> cities) {
        mContext = context;
        mCities = cities;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_city, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder( FavoriteAdapter.ViewHolder holder, int position) {
        /* add data into template */
        City city = mCities.get(position);
        holder.mTextViewCityNameFavorite.setText(city.getmName());
        holder.mTextViewDescFavorite.setText(city.getmDescription());
        holder.mTextViewTempFavorite.setText(city.getmTemperature());
        holder.mImageViewFavorite.setImageResource(city.getmWeatherIcon());
        holder.mCity = city;
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        public TextView mTextViewCityNameFavorite;
        public TextView mTextViewDescFavorite;
        public TextView mTextViewTempFavorite;
        public ImageView mImageViewFavorite;
        public City mCity;

        public ViewHolder(View view) {
            super(view);
            mTextViewCityNameFavorite = view.findViewById(R.id.text_view_city_name_favorite);
            mTextViewDescFavorite = view.findViewById(R.id.text_view_desc_favorite);
            mTextViewTempFavorite = view.findViewById(R.id.text_view_temp_favorite);
            mImageViewFavorite = view.findViewById(R.id.image_view_favorite);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {

            /* add action on template item => delete city */
            Log.d("TEST_LONG_CLICK", "uiiiii");
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage(R.string.delete_city_favorite);

            builder.setPositiveButton(R.string.yes, (dialog, WhichButton) -> {
                mCities.remove(mCity);
                FavoriteAdapter.this.notifyDataSetChanged();
                dialog.cancel();
            });

            builder.setNegativeButton(R.string.no, (dialog, WhichButton) -> dialog.cancel());
            builder.create().show();
            return false;
        }
    }
}

