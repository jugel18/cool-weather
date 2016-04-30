package com.tencent.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.tencent.coolweather.R;
import com.tencent.coolweather.db.CoolWeatherDB;
import com.tencent.coolweather.model.City;
import com.tencent.coolweather.model.County;
import com.tencent.coolweather.model.Province;
import com.tencent.coolweather.util.HttpURLConnectionListener;
import com.tencent.coolweather.util.HttpUtil;
import com.tencent.coolweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/4/29.
 */
public class MainActivity extends Activity {
    public TextView title;
    public ListView listView;
    public ArrayAdapter<String> adapter;
    public List<String> datalist = new ArrayList<>();
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    public static int level;
    public Province selectedProvince;
    public City selectedCity;
    public County selectedCounty;
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private CoolWeatherDB db;
    private ProgressDialog progressDialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        db = CoolWeatherDB.getInstance(this);
        title = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (level == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCity();
                }else if(level == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryCounty();
                }
            }
        });

        queryProvince();

    }

    public void queryProvince() {
        provinceList = db.loadProvince();
        datalist.clear();
        if(provinceList.size() > 0) {
            for(Province p : provinceList)
                datalist.add(p.getProvinceName());
            adapter.notifyDataSetChanged();
            title.setText("请选择省份");
            listView.requestLayout();
            listView.setSelection(0);
            level = LEVEL_PROVINCE;
        }
        else {
            queryFromServer(null, "province");
        }
    }

    public void queryCity() {
        cityList = db.loadCity(selectedProvince.getId());
        Log.e("MainActivity.this", "citylist ?");
        if(cityList.size() > 0) {
            datalist.clear();
            for(City c : cityList)
                datalist.add(c.getCityName());
            adapter.notifyDataSetChanged();
            listView.requestLayout();
            title.setText(selectedProvince.getProvinceName());
            listView.setSelection(0);
            level = LEVEL_CITY;
            Log.e("MainActivity.this", "queryed city");
        }else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }
    public void queryCounty() {
        countyList = db.loadCounty(selectedCity.getId());
        datalist.clear();
        if(countyList.size() > 0) {
            for(County c : countyList)
                datalist.add(c.getCountyName());
            adapter.notifyDataSetChanged();
            title.setText(selectedCity.getCityName());
            listView.setSelection(0);
            level = LEVEL_COUNTY;
        }else {
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }

    public void queryFromServer(final String code, final String type) {
       String address;
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }


        HttpUtil.sendHttpRequest(address, new HttpURLConnectionListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if("province".equals(type)) {
                    result = Utility.handleProvinceResponse(db, response);
                }
                else if("city".equals(type)) {
                    result = Utility.handleCityResponse(db, response, selectedProvince.getId());

                }
                else if("county".equals((type))) {
                    result = Utility.handleCountyResponse(db, response, selectedCity.getId());
                }

                if(result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if("province".equals(type)) {
                                queryProvince();
                            }
                            else if("city".equals(type)) {
                                Log.e("MainActivity.this", "queryCity");
                                queryCity();
                            }
                            else if("county".equals(type)) {
                                Log.e("MainActivity.this", "queryCounty");
                                queryCounty();
                            }


                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(MainActivity.this, "加载失败，请重试",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();
    }

    public void closeProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void onBackPressed() {
        if(level == LEVEL_COUNTY) {
            queryCity();
        }
        else if(level == LEVEL_CITY) {
            queryProvince();
        }
        else if(level == LEVEL_PROVINCE) {
            finish();
        }
    }

}
