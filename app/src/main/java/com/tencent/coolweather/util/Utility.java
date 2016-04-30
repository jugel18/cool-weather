package com.tencent.coolweather.util;

import android.text.TextUtils;

import com.tencent.coolweather.db.CoolWeatherDB;
import com.tencent.coolweather.model.City;
import com.tencent.coolweather.model.County;
import com.tencent.coolweather.model.Province;

/**
 * Created by lenovo on 2016/4/29.
 */
public class Utility {
    public synchronized  static boolean handleProvinceResponse(CoolWeatherDB db, String response) {
       if(!TextUtils.isEmpty(response)) {
           String[] provinces = response.split(",");
           for (String p : provinces) {
               Province province = new Province();
               String[] pr = p.split("\\|");
               province.setProvinceCode(pr[0]);
               province.setProvinceName(pr[1]);
               db.saveProvince(province);

           }
           return true;

       }
        return false;
    }

    public  static boolean handleCityResponse(CoolWeatherDB db, String response, int provinceId) {
        if(!TextUtils.isEmpty(response)) {
            String[] cities = response.split(",");
            for(String c : cities) {
                String[] ci = c.split("\\|");
                City city = new City();
                city.setCityCode(ci[0]);
                city.setCityName(ci[1]);
                city.setProvinceId(provinceId);
                db.saveCity(city);

            }
            return  true;
        }
        return  false;
    }

    public static boolean handleCountyResponse(CoolWeatherDB db, String response, int cityId) {
        if(!TextUtils.isEmpty(response)) {
            String[] counties = response.split(",");
            for(String c : counties) {
                String[] co = c.split("\\|");
                County county = new County();
                county.setCountyCode(co[0]);
                county.setCountyName((co[1]));
                county.setCityId(cityId);
                db.saveCounty(county);


            }
            return true;
        }
        return false;
    }

}
