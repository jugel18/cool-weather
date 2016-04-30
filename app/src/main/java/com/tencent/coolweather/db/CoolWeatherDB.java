package com.tencent.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tencent.coolweather.model.City;
import com.tencent.coolweather.model.County;
import com.tencent.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/4/29.
 */
public class CoolWeatherDB {
    public static final String DB_NAME = "cool_weather";
    public static final int VERSION = 1;
    private static CoolWeatherDB coolWeatherDB;
    private SQLiteDatabase db;

    private CoolWeatherDB(Context context) {
        MyHelper myHelper = new MyHelper(context, DB_NAME, null, VERSION);
        db = myHelper.getWritableDatabase();
    }

    public synchronized static CoolWeatherDB getInstance(Context context) {
        if(coolWeatherDB == null)
            coolWeatherDB = new CoolWeatherDB(context);
        return coolWeatherDB;
    }

    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues cv = new ContentValues();
            cv.put("province_name", province.getProvinceName());
            cv.put("province_code", province.getProvinceCode());
            db.insert("province", null, cv);
        }
    }

    public List<Province> loadProvince() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("province",null,null,null,null,null,null);
        if(cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode((cursor.getString(cursor.getColumnIndex("province_code"))));
                list.add(province);
            }while(cursor.moveToNext());
            if(cursor != null)

                cursor.close();
        }
        return  list;
    }

    public void saveCity(City city) {
       if (city != null) {
           ContentValues cv = new ContentValues();
           cv.put("city_name", city.getCityName());
           cv.put("city_code", city.getCityCode());
           cv.put("province_id", city.getProvinceId());
           db.insert("city", null, cv);
       }

    }
    public List<City> loadCity(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("city",null, "province_id = ?", new String[] {String.valueOf(provinceId)}, null,null,null);
        Log.e("coolDB"," got cursor");
        if(cursor.moveToFirst()) {
            do{
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")))  ;
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }while(cursor.moveToNext());
            if(cursor != null)
                cursor.close();
        }
        Log.e("coolDB", "before return list");
        return  list;
    }

    public void saveCounty(County county) {
        ContentValues cv = new ContentValues();
        cv.put("county_name", county.getCountyName());
        cv.put("county_code", county.getCountyCode());
        cv.put("city_id",county.getCityId());
        db.insert("county",null, cv);
    }

    public List<County> loadCounty(int cityId) {
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("county", null, "city_id = ?", new String[] {String.valueOf(cityId)}, null, null, null);
        if(cursor.moveToFirst()) {
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }while(cursor.moveToNext());

            if(cursor != null)
                cursor.close();
        }
        return list;
    }
}
