package com.bkacad.nnt.cityapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnGetData;
    private List<City> dataCity;
    private MyAdapter myAdapter;
    private ListView lvCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetData = findViewById(R.id.btn_main_get_city);
        lvCity = findViewById(R.id.lv_main_city);

        dataCity = new ArrayList<>();
        myAdapter = new MyAdapter(this, dataCity);
        lvCity.setAdapter(myAdapter);

        // Khi click vào button -> lấy dữ liệu từ API về
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myUrl = "https://dataservice.accuweather.com/locations/v1/topcities/100?apikey=xxx&language=vi-VN";
                StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
                        response -> {
                            try{
                                // Xử lý dữ liệu
                                JSONArray jsonArray = new JSONArray(response);
                                dataCity.clear();

                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String cityName = jsonObject.getString("EnglishName");
                                    String cityArea = jsonObject.getJSONObject("Region").getString("LocalizedName");
                                    String cityLatitude = jsonObject.getJSONObject("GeoPosition").getString("Latitude");
                                    String cityLongitude = jsonObject.getJSONObject("GeoPosition").getString("Longitude");
                                    City city = new City(cityName, cityArea, cityLatitude, cityLongitude);
                                    dataCity.add(city);
                                }
                                myAdapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        volleyError -> Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show()
                );
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                requestQueue.add(myRequest);
            }
        });
        // Khi click vao item view
        lvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, dataCity.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}