package com.ayponyo.android.meteo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.ayponyo.android.meteo.models.City;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "db_weather";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CITY = "city";
    private static final String KEY_ID = "id";
    private static final String KEY_ID_CITY = "id_city";
    private static final String KEY_NAME = "name";
    private static final String KEY_TEMP = "temperature";
    private static final String KEY_DESC = "description";
    private static final String KEY_RES_ICON = "res_icon";
    private static final String KEY_LNG = "longitude";
    private static final String KEY_LAT = "latitude";
    private static final String CREATE_TABLE_CITY ="CREATE TABLE "+ TABLE_CITY+ "("+ KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ID_CITY + " INTEGER," + KEY_NAME + " TEXT," + KEY_TEMP + " TEXT," + KEY_DESC + " TEXT,"
            + KEY_RES_ICON + " INTEGER," + KEY_LAT + " DECIMAL (3, 10),"
            + KEY_LNG + " DECIMAL (3, 10))";

    private final String[] allColumns = { KEY_ID,KEY_ID_CITY, KEY_NAME, KEY_TEMP, KEY_DESC, KEY_RES_ICON, KEY_LNG, KEY_LAT };


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        onCreate(db);
    }

    public int addCityInBase(City city){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues datas = new ContentValues();
        datas.put(KEY_ID_CITY, city.getSys().getId());
        datas.put(KEY_NAME, city.getName());
        datas.put(KEY_TEMP, city.getMain().getTemp());
        datas.put(KEY_DESC, city.getWeather().get(0).getDescription());
        /*datas.put(KEY_RES_ICON, city.getmWeatherIcon());*/
        datas.put(KEY_LAT, city.getCoord().getLat());
        datas.put(KEY_LNG, city.getCoord().getLon());

        int idRow = (int) db.insert(TABLE_CITY, null, datas);

        db.close();

        return idRow;
    }

    public void deleteCityInBase(int idRow){
        SQLiteDatabase db = getWritableDatabase();

        String[] value = {String.valueOf(idRow)};
        db.delete(TABLE_CITY,KEY_ID+" = ? ", value);

        db.close();
    }

    public ArrayList<City> getAllCities(){
        ArrayList<City> cities = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CITY,allColumns, null, null, null, null ,null  );
        cursor.moveToFirst();
        
        while (!cursor.isAfterLast()) {
            City city = new City();
            city.setId( cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            city.getSys().setId( cursor.getInt(cursor.getColumnIndex(KEY_ID_CITY)));
            city.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            city.getWeather().get(0).setDescription((cursor.getString(cursor.getColumnIndex(KEY_DESC))));
            /*Log.d("TEST_SQL", cursor.getString(cursor.getColumnIndex(KEY_TEMP)));*/
            city.getMain().setTemp(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_TEMP))));
            city.getCoord().setLat(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_LAT))));
            city.getCoord().setLon(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_LNG))));
            /*city.setmWeatherIcon(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_RES_ICON))));*/

            cities.add(city);
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }
}
