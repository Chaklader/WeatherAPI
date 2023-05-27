package com.weather.api.weatherapi.controller.dto;

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










