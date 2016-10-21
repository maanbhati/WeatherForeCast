package com.weatherforecast.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class ListDataModel implements Serializable {
    private String CityName;
    private List<DayDataModel> dayDataModels;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public List<DayDataModel> getDayDataModels() {
        return dayDataModels;
    }

    public void setDayDataModels(List<DayDataModel> dayDataModels) {
        this.dayDataModels = dayDataModels;
    }
}
