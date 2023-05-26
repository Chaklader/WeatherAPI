package com.weather.api.weatherapi;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherData {
    private double temperature;
    private double tempMin;
    private int humidity;
    private int pressure;
    private double feelsLike;
    private double tempMax;
    private int visibility;
    private int timezone;
    private Clouds clouds;
    private Sys sys;
    private long dt;
    private Coord coord;
    private Weather[] weather;
    private String name;
    private int cod;
    private int id;
    private String base;
    private Wind wind;
}

@Data
@Builder
class Clouds {
    private int all;
}

@Data
@Builder
class Sys {
    private String country;
    private long sunrise;
    private long sunset;
    private int id;
    private int type;
}

@Data
@Builder
class Coord {
    private double lon;
    private double lat;
}

@Data
@Builder
class Weather {
    private String icon;
    private String description;
    private String main;
    private int id;
}

@Data
@Builder
class Wind {
    private int deg;
    private double speed;
}
