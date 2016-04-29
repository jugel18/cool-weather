package com.tencent.coolweather.model;

/**
 * Created by lenovo on 2016/4/29.
 */
public class Province {
    private int id;
    private String provinceName;
    private String provinceCode;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProvinceName() {
        return  provinceName;
    }
    public void setProvinceName(String name) {
        provinceName = name;
    }
    public String getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(String code) {
        this.provinceCode = code;
    }
}
