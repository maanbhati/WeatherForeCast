package com.weatherforecast.model;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class Searchdata {
    private Coord coord;

    private String _id;

    private String name;

    private String country;

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "[coord = " + coord + ", _id = " + _id + ", name = " + name + ", country = " + country + "]";
    }
}
