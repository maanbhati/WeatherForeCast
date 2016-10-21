package com.weatherforecast.model;

/**
 * Created by Mansingh.Bhati on 10/20/2016.
 */
public class CityWeatherResult {

    public final City city;
    public final String cod;
    public final double message;
    public final long cnt;
    public final List list[];

    public CityWeatherResult(City city, String cod, double message, long cnt, List[] list) {
        this.city = city;
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    public static final class City {
        public final long id;
        public final String name;
        public final Coord coord;
        public final String country;
        public final long population;
        public final Sys sys;

        public City(long id, String name, Coord coord, String country, long population, Sys sys) {
            this.id = id;
            this.name = name;
            this.coord = coord;
            this.country = country;
            this.population = population;
            this.sys = sys;
        }

        public static final class Coord {
            public final double lon;
            public final double lat;

            public Coord(double lon, double lat) {
                this.lon = lon;
                this.lat = lat;
            }
        }

        public static final class Sys {
            public final long population;

            public Sys(long population) {
                this.population = population;
            }
        }
    }

    public static final class List {
        public final long dt;
        public final Main main;
        public final Weather weather[];
        public final Clouds clouds;
        public final Wind wind;
        public final Sys sys;
        public final String dt_txt;
        public final Rain rain;

        public List(long dt, Main main, Weather[] weather, Clouds clouds, Wind wind, Sys sys, String dt_txt, Rain rain) {
            this.dt = dt;
            this.main = main;
            this.weather = weather;
            this.clouds = clouds;
            this.wind = wind;
            this.sys = sys;
            this.dt_txt = dt_txt;
            this.rain = rain;
        }

        public static final class Main {
            public final double temp;
            public final double temp_min;
            public final double temp_max;
            public final double pressure;
            public final double sea_level;
            public final double grnd_level;
            public final long humidity;
            public final String temp_kf;

            public Main(double temp, double temp_min, double temp_max, double pressure, double sea_level, double grnd_level, long humidity, String temp_kf) {
                this.temp = temp;
                this.temp_min = temp_min;
                this.temp_max = temp_max;
                this.pressure = pressure;
                this.sea_level = sea_level;
                this.grnd_level = grnd_level;
                this.humidity = humidity;
                this.temp_kf = temp_kf;
            }
        }

        public static final class Weather {
            public final long id;
            public final String main;
            public final String description;
            public final String icon;

            public Weather(long id, String main, String description, String icon) {
                this.id = id;
                this.main = main;
                this.description = description;
                this.icon = icon;
            }
        }

        public static final class Clouds {
            public final long all;

            public Clouds(long all) {
                this.all = all;
            }
        }

        public static final class Wind {
            public final double speed;
            public final double deg;

            public Wind(double speed, double deg) {
                this.speed = speed;
                this.deg = deg;
            }
        }

        public static final class Sys {
            public final String pod;

            public Sys(String pod) {
                this.pod = pod;
            }
        }

        public static final class Rain {

            public Rain() {
            }
        }
    }
}
