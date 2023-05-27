package com.weather.api.weatherapi.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeatherData {

    private int visibility;
    private double temp;
    private double minTemperature;
    private int humidity;
    private int pressure;
    private double feelsLike;
    private double maxTemperature;
    private String country;
    private String name;
    private double latitude;
    private double longitude;
    private double windSpeed;
}










