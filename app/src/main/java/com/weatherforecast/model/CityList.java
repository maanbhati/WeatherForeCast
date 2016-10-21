package com.weatherforecast.model;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class CityList {
    private Searchdata[] Searchdata;

    public Searchdata[] getSearchdata() {
        return Searchdata;
    }

    public void setSearchdata(Searchdata[] Searchdata) {
        this.Searchdata = Searchdata;
    }

    @Override
    public String toString() {
        return "ClassPojo [Searchdata = " + Searchdata + "]";
    }
}
