package com.weatherforecast.jsonparser;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weatherforecast.model.CityList;
import com.weatherforecast.model.CityWeatherResult;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class JsonParser {
    Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    Gson gson = new Gson();

    public CityList readJSON() {

        Type type = new TypeToken<CityList>() {
        }.getType();

        CityList cityTypeList = gson.fromJson(loadJSONFromAsset(), type);
        return cityTypeList;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("citylist.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public CityWeatherResult readServerJSON(String jsonString) {
        Type type = new TypeToken<CityWeatherResult>() {
        }.getType();

        CityWeatherResult cityWeatherResult = gson.fromJson(jsonString, type);
        return cityWeatherResult;
    }
}
