package com.tencent.coolweather.model;

/**
 * Created by lenovo on 2016/4/29.
 */
public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private int provinceId;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCityName() {
        return  cityName;
    }
    public void setCityName(String name) {
        cityName = name;
    }
    public String getCityCode() {
        return  cityCode;
    }
    public void setCityCode(String code) {
        cityCode = code;
    }
    public int getProvinceId() {
        return provinceId;
    }
    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
